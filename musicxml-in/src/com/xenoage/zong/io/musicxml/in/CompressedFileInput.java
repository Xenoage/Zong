package com.xenoage.zong.io.musicxml.in;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.w3c.dom.Document;

import com.xenoage.utils.base.exceptions.InvalidFormatException;
import com.xenoage.utils.base.zip.ZipUtils;
import com.xenoage.utils.io.FileUtils;
import com.xenoage.utils.io.StreamUtils;
import com.xenoage.utils.xml.XMLReader;
import com.xenoage.zong.io.musicxml.FileType;
import com.xenoage.zong.io.musicxml.link.LinkAttributes;
import com.xenoage.zong.io.musicxml.opus.Opus;
import com.xenoage.zong.io.musicxml.opus.OpusItem;
import com.xenoage.zong.io.musicxml.opus.Score;


/**
 * This class reads a compressed MusicXML file.
 * 
 * Therefore, it is extracted to a temporary directory. Then,
 * the list of files (opus) or single files can be loaded.
 * When the class instance is cleaned up, the temporary files
 * are deleted automatically.
 * 
 * This class can also handle nested compressed MusicXML files,
 * as long as they contain a single score and not an opus.
 * 
 * Don't forget to call the <code>close</code> method when you
 * are finished. Though it will also be called when the class
 * is finalized, there is no guarantee that this will happen.
 * 
 * @author Andreas Wenger
 */
public class CompressedFileInput
{
	
	private File osTempFolder;
	private File tempFolder;
	private OpusItem rootItem;
	
	
	/**
	 * Creates a {@link CompressedFileInput} instance for the given
	 * compressed MusicXML data, which is extracted in a new (unique) subdirectory
	 * of the given folder.
	 */
	public CompressedFileInput(InputStream inputStream, File osTempFolder)
		throws IOException
	{
		this.osTempFolder = osTempFolder;
		//create temporary folder
		this.tempFolder = new File(osTempFolder, UUID.randomUUID().toString());
		if (!this.tempFolder.mkdir())
		{
			throw new IOException("Could not create temp folder: " + this.tempFolder);
		}
		//extract file contents there
		ZipUtils.extractAll(inputStream, this.tempFolder);
		//read root item
		//parse META-INF/container.xml
		Document doc;
		try
		{
			doc = XMLReader.readFile(new FileInputStream(new File(tempFolder, "META-INF/container.xml")));
		}
		catch (Exception ex)
		{
			throw new IllegalStateException(
				"Compressed MusicXML file has no (well-formed) META-INF/container.xml", ex);
		}
		//interpret XML document
		String rootfilePath;
		try
		{
			rootfilePath = XMLReader.element(XMLReader.element(XMLReader.root(doc),
				"rootfiles"), "rootfile").getAttribute("full-path");
		}
		catch (Exception ex)
		{
			throw new IllegalStateException("Invalid META-INF/container.xml", ex);
		}
		//load root file
		File rootfile = new File(tempFolder, rootfilePath);
		//buffered input stream for reuse
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(rootfile));
		StreamUtils.markInputStream(bis);
		try
		{
			FileType type = FileTypeReader.getFileType(bis);
			bis.reset();
			if (type == null)
				throw new IllegalStateException("Unknown root file type");
			switch (type)
			{
				case Compressed:
					throw new IllegalStateException("Root file may (currently) not be compressed");
				case XMLOpus:
					rootItem = new OpusFileInput().readOpusFile(bis);
					break;
				case XMLScorePartwise: case XMLScoreTimewise:
					rootItem = new Score(new LinkAttributes(rootfilePath), null);
			}
		}
		catch (IOException ex)
		{
			throw new IllegalStateException("Could not load root file", ex);
		}
	}
	
	
	/**
	 * Gets the item which is the main document in this compressed
	 * MusicXML file: Either a {@link Score} or an {@link Opus}.
	 */
	public OpusItem getRootItem()
	{
		return rootItem;
	}
	
	
	/**
	 * Returns true, if the file does not only contain a single score but
	 * a whole opus.
	 */
	public boolean isOpus()
	{
		return (rootItem instanceof Opus);
	}
	
	
	/**
	 * Gets a (flattened) list of all filenames in this opus. If this file
	 * contains no opus but a single score, the filename of the single score
	 * is returned.
	 */
	public List<String> getScoreFilenames()
		throws IOException
	{
		LinkedList<String> ret = new LinkedList<String>();
		if (isOpus())
		{
			getScoreFilenames(new OpusFileInput().resolveOpusLinks(
				(Opus) rootItem, tempFolder.getAbsolutePath()), ret);
		}
		else
		{
			ret.add(((Score)rootItem).getHref());
		}
		return ret;
	}
	
	
	/**
	 * Loads and returns the {@link com.xenoage.zong.core.Score} at the given path.
	 */
	public com.xenoage.zong.core.Score loadScore(String path)
		throws InvalidFormatException, IOException
	{
		BufferedInputStream bis = new BufferedInputStream(
			new FileInputStream(new File(tempFolder, path)));
		StreamUtils.markInputStream(bis);
		//XML or compressed?
		FileType fileType = FileTypeReader.getFileType(bis);
		bis.reset();
		if (fileType == null)
			throw new InvalidFormatException("Score has invalid format: " + path);
		switch (fileType)
		{
			case Compressed:
				return loadCompressedScore(path);
			case XMLScorePartwise:
				return new MusicXMLScoreFileInput().read(bis, path);
			case XMLScoreTimewise:
				throw new IllegalStateException("score-timewise is currently not implemented");
			default:
				throw new InvalidFormatException("Score has invalid format: " + path);
		}
	}
	
	
	private com.xenoage.zong.core.Score loadCompressedScore(String path)
		throws IOException
	{
		CompressedFileInput zip = new CompressedFileInput(
			new FileInputStream(new File(tempFolder, path)), osTempFolder);
		com.xenoage.zong.core.Score ret = zip.loadScore(((Score) zip.getRootItem()).getHref());
		zip.close();
		return ret;
	}
	
	
	private void getScoreFilenames(Opus resolvedOpus, LinkedList<String> acc)
	{
		for (OpusItem item : resolvedOpus.getItems())
		{
			if (item instanceof Score)
				acc.add(((Score)item).getHref());
			else if (item instanceof Opus)
				getScoreFilenames((Opus) item, acc);
		}
	}
	
	
	/**
	 * Call this method to close the compressed file, cleaning up the
	 * temporary files.
	 */
	public void close()
	{
		FileUtils.deleteDirectory(tempFolder);
	}
	
	
	@Override public void finalize()
	{
		close();
	}
	

}
