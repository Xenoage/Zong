package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.PlatformUtils.platformUtils;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.io.IOException;
import java.util.List;

import com.xenoage.utils.async.AsyncCallback;
import com.xenoage.utils.async.AsyncProducer;
import com.xenoage.utils.filter.Filter;
import com.xenoage.utils.io.BufferedInputStream;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.InputStream;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.musicxml.FileType;
import com.xenoage.zong.io.musicxml.opus.Opus;

/**
 * This class reads single or multiple {@link Score}s from
 * a given file, which may be an XML score, an XML opus or
 * a compressed MXL file.
 * 
 * The result is returned asynchronously, since multiple files
 * may have to be opened and only non-blocking IO is supported
 * by all platforms.
 * 
 * @author Andreas Wenger
 */
public class MusicXmlFileReader
	implements AsyncProducer<List<Score>> {

	private InputStream in;
	private String path;
	private Filter<String> scoreFileFilter;


	/**
	 * Reader for a list of scores from the given file. XML scores,
	 * XML opera and compressed MusicXML files are supported.
	 * The given filter is used to select score files.
	 * 
	 * The given input stream is used to read the simple MusicXML file
	 * or the opus file. If it is a opus file, the given path must be
	 * set, so that the linked files can be found and opened, otherwise
	 * an empty list is returned.
	 */
	public MusicXmlFileReader(InputStream in, String path, Filter<String> scoreFileFilter) {
		this.in = in;
		this.path = path;
		this.scoreFileFilter = scoreFileFilter;
	}

	@Override public void produce(AsyncCallback<List<Score>> callback) {
		List<Score> ret = alist();
		//open stream
		BufferedInputStream bis = new BufferedInputStream(in);
		try {
			bis.mark();
			//file type
			FileType fileType = FileTypeReader.getFileType(bis);
			bis.reset();
			bis.unmark();
			//open file
			if (fileType == FileType.XMLScorePartwise) {
				Score score = new MusicXmlScoreFileInput().read(bis, path);
				ret.add(score);
				callback.onSuccess(ret);
			}
			else if (fileType == FileType.XMLOpus) {
				//opus
				if (path == null) {
					//no path is given. we can not read the linked files.
					callback.onSuccess(ret);
				}
				else {
					//read files
					String directory = FileUtils.getDirectoryName(path);
					OpusFileInput opusInput = new OpusFileInput();
					Opus opus = opusInput.readOpusFile(bis);
					opus = opusInput.resolveOpusLinks(opus, null, directory);
					List<String> filePaths = scoreFileFilter.filter(opus.getScoreFilenames());
					collectScores(directory, filePaths, scoreFileFilter, ret, callback);
				}
			}
			else if (fileType == FileType.Compressed) {
				CompressedFileInput zip = new CompressedFileInput(bis);
				List<String> filePaths = scoreFileFilter.filter(zip.getScoreFilenames());
				for (String filePath : filePaths) {
					Score score = zip.loadScore(filePath);
					ret.add(score);
				}
				zip.close();
			}
			else {
				callback.onFailure(new IOException("Unknown file type"));
			}
		} catch (IOException ex) {
			//try to close input stream
			bis.close();
			//return failure
			callback.onFailure(ex);
		}
	}

	/**
	 * Processes the given list of filepaths. The scores and opuses are loaded
	 * and added to the accumulator list. When finished, the callback method for
	 * success or failure is called.
	 */
	private static void collectScores(final String directory, final List<String> filePaths,
		final Filter<String> scoreFileFilter, final List<Score> acc,
		final AsyncCallback<List<Score>> callback) {
		if (filePaths.size() > 0) {
			//another file to load
			String filePath = filePaths.remove(0);
			final String relativePath = directory + "/" + filePath;
			platformUtils().openFileAsync(relativePath, new AsyncCallback<InputStream>() {

				@Override public void onSuccess(InputStream stream) {
					//input stream opened, read file
					new MusicXmlFileReader(stream, relativePath, scoreFileFilter)
						.produce(new AsyncCallback<List<Score>>() {

							@Override public void onSuccess(List<Score> scores) {
								acc.addAll(scores);
								//async recursive call
								collectScores(directory, filePaths, scoreFileFilter, acc, callback);
							}

							@Override public void onFailure(Exception ex) {
								callback.onFailure(ex);
							}
						});
				}

				@Override public void onFailure(Exception ex) {
					callback.onFailure(ex);
				}
			});
		}
		else {
			//all files loaded
			callback.onSuccess(acc);
		}
	}

}
