package com.xenoage.zong.io.musicxml.in;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import com.xenoage.utils.base.filter.Filter;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.StreamUtils;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.musicxml.FileType;
import com.xenoage.zong.io.musicxml.opus.Opus;


/**
 * This class reads single or multiple {@link Score}s from
 * a given file, which may be an XML score, an XML opus or
 * a compressed MXL file.
 * 
 * @author Andreas Wenger
 */
public class FileReader
{


	/**
	 * Loads a list of scores from the given file. XML scores,
	 * XML opera and compressed MusicXML files are supported.
	 * The given filter is used to select score files.
	 * 
	 * The given input stream is used to read the simple MusicXML file
	 * or the opus file. If it is a opus file, the given path must be
	 * set, so that the linked files can be found and opened, otherwise
	 * an empty list is returned.
	 */
	public static List<Score> loadScores(InputStream in, String path, Filter<String> scoreFileFilter)
		throws IOException
	{
		List<Score> ret = new LinkedList<Score>();
		//open stream
		BufferedInputStream bis = new BufferedInputStream(in);
		StreamUtils.markInputStream(bis);
		//file type
		FileType fileType = FileTypeReader.getFileType(bis);
		bis.reset();
		//open file
		if (fileType == FileType.XMLScorePartwise) {
			Score score = new MusicXMLScoreFileInput().read(bis, path);
			ret.add(score);
		} else if (fileType == FileType.XMLOpus) {
			//opus
			if (path == null) {
				//no path is given. we can not read the linked files.
				return new LinkedList<Score>();
			}
			String directory = FileUtils.getDirectoryName(path);
			OpusFileInput opusInput = new OpusFileInput();
			Opus opus = opusInput.readOpusFile(bis);
			opus = opusInput.resolveOpusLinks(opus, directory);
			List<String> filePaths = scoreFileFilter.filter(opus.getScoreFilenames());
			for (String filePath : filePaths) {
				String relativePath = directory + "/" + filePath;
				List<Score> scores = loadScores(new FileInputStream(relativePath), relativePath, scoreFileFilter);
				ret.addAll(scores);
			}
		} else if (fileType == FileType.Compressed) {
			CompressedFileInput zip = new CompressedFileInput(bis, FileUtils.getTempFolder());
			List<String> filePaths = scoreFileFilter.filter(zip.getScoreFilenames());
			for (String filePath : filePaths) {
				Score score = zip.loadScore(filePath);
				ret.add(score);
			}
			zip.close();
		} else {
			throw new IOException("Unknown file type");
		}
		return ret;
	}


}
