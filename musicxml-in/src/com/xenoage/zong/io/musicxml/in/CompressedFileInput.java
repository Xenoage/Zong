package com.xenoage.zong.io.musicxml.in;

import static com.xenoage.utils.PlatformUtils.platformUtils;

import java.io.IOException;
import java.util.List;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.utils.exceptions.InvalidFormatException;
import com.xenoage.utils.io.BufferedInputStream;
import com.xenoage.utils.io.InputStream;
import com.xenoage.utils.io.ZipReader;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.zong.io.musicxml.FileType;
import com.xenoage.zong.io.musicxml.link.LinkAttributes;
import com.xenoage.zong.io.musicxml.opus.Opus;
import com.xenoage.zong.io.musicxml.opus.OpusItem;
import com.xenoage.zong.io.musicxml.opus.Score;

/**
 * This class reads a compressed MusicXML file.
 * 
 * Therefore, it is extracted in the main memory. Then,
 * the list of files (opus) or single files can be loaded.
 * When the class instance is cleaned up, the zip content
 * in the memory is cleaned up, too.
 * 
 * This class can also handle nested compressed MusicXML files,
 * as long as they contain a single score and not an opus.
 * 
 * @author Andreas Wenger
 */
public class CompressedFileInput {

	private OpusItem rootItem;
	private ZipReader zip;


	/**
	 * Creates a {@link CompressedFileInput} instance for the given
	 * compressed MusicXML data.
	 */
	public CompressedFileInput(InputStream inputStream)
		throws IOException {
		
		//load zip contents
		zip = platformUtils().createZipReader(inputStream);
		if (zip == null) {
			throw new IOException("Zip reader is unsupported");
		}
		
		//parse META-INF/container.xml
		String rootfilePath = null;
		try {
			InputStream containerStream = zip.openFile("META-INF/container.xml");
			XmlReader containerReader = platformUtils().createXmlReader(containerStream);
			rootfilePath = readRootFilePath(containerReader);
			containerStream.close();
		} catch (Exception ex) {
			throw new IOException(
				"Compressed MusicXML file has no (well-formed) META-INF/container.xml", ex);
		}

		//load root file
		try {
			BufferedInputStream rootStream = new BufferedInputStream(zip.openFile(rootfilePath));
			rootStream.mark();
			FileType type = FileTypeReader.getFileType(rootStream);
			rootStream.reset();
			rootStream.unmark();
			if (type == null)
				throw new IllegalStateException("Unknown root file type");
			switch (type) {
				case Compressed:
					throw new IllegalStateException("Root file may (currently) not be compressed");
				case XMLOpus:
					rootItem = new OpusFileInput().readOpusFile(rootStream);
					break;
				case XMLScorePartwise:
				case XMLScoreTimewise:
					rootItem = new Score(new LinkAttributes(rootfilePath), null);
			}
			rootStream.close();
		} catch (IOException ex) {
			throw new IOException("Could not load root file", ex);
		}
	}
	
	@NonNull private String readRootFilePath(XmlReader containerReader)
		throws IOException {
		XmlReader r = containerReader;
		r.openNextChildElement(); //root element
		while (r.openNextChildElement()) {
			if (r.getElementName().equals("rootfiles")) { //rootfiles element
				while (r.openNextChildElement()) {
					if (r.getElementName().equals("rootfile")) { //rootfile element
						String fullPath = r.getAttribute("full-path");
						if (fullPath == null)
							throw new IOException("full-path of rootfile not found");
						return fullPath;
					}
					r.closeElement();
				}
			}
			r.closeElement();
		}
		throw new IOException("rootfile not found");
	}

	/**
	 * Gets the item which is the main document in this compressed
	 * MusicXML file: Either a {@link Score} or an {@link Opus}.
	 */
	public OpusItem getRootItem() {
		return rootItem;
	}

	/**
	 * Returns true, if the file does not only contain a single score but
	 * a whole opus.
	 */
	public boolean isOpus() {
		return (rootItem instanceof Opus);
	}

	/**
	 * Gets a (flattened) list of all filenames in this opus. If this file
	 * contains no opus but a single score, the filename of the single score
	 * is returned.
	 */
	public List<String> getScoreFilenames()
		throws IOException {
		List<String> ret = CollectionUtils.alist();
		if (isOpus()) {
			getScoreFilenames(
				//new OpusFileInput().resolveOpusLinks((Opus) rootItem, zip, null), ret); //TODO
				(Opus) rootItem, ret);
		}
		else {
			ret.add(((Score) rootItem).getLink().getHref());
		}
		return ret;
	}

	/**
	 * Loads and returns the {@link com.xenoage.zong.core.Score} at the given path.
	 */
	public com.xenoage.zong.core.Score loadScore(String path)
		throws InvalidFormatException, IOException {
		BufferedInputStream bis = new BufferedInputStream(zip.openFile(path));
		//XML or compressed?
		bis.mark();
		FileType fileType = FileTypeReader.getFileType(bis);
		bis.reset();
		bis.unmark();
		if (fileType == null)
			throw new InvalidFormatException("Score has invalid format: " + path);
		com.xenoage.zong.core.Score ret = null;
		switch (fileType) {
			case Compressed:
				ret = loadCompressedScore(path);
				break;
			case XMLScorePartwise:
				ret = new MusicXmlScoreFileInput().read(bis, path);
				break;
			case XMLScoreTimewise:
				throw new IllegalStateException("score-timewise is currently not implemented");
			default:
				throw new InvalidFormatException("Score has invalid format: " + path);
		}
		bis.close();
		return ret;
	}

	private com.xenoage.zong.core.Score loadCompressedScore(String path)
		throws IOException {
		CompressedFileInput zipScore = new CompressedFileInput(zip.openFile(path));
		com.xenoage.zong.core.Score ret = zipScore.loadScore(((Score) zipScore.getRootItem()).getLink().getHref());
		zipScore.close();
		return ret;
	}

	private void getScoreFilenames(Opus resolvedOpus, List<String> acc) {
		for (OpusItem item : resolvedOpus.getItems()) {
			if (item instanceof Score)
				acc.add(((Score) item).getLink().getHref());
			else if (item instanceof Opus)
				getScoreFilenames((Opus) item, acc);
		}
	}
	
	public void close() {
		zip.close();
	}

}
