package com.xenoage.zong.io;

import java.io.IOException;
import java.io.OutputStream;

import com.xenoage.zong.documents.ScoreDoc;


/**
 * This is the interface for all classes that allow the creation of a file
 * from a {@link ScoreDoc} instance.
 * 
 * There may be a MusicXML writer and a MIDI writer for example.
 *
 * @author Andreas Wenger
 */
public abstract class ScoreDocFileOutput
	implements FileOutput
{
	
	
	/**
   * Writes the given {@link ScoreDoc} instance into the given output stream or path.
   */
	public abstract void write(ScoreDoc doc, OutputStream stream, String filePath)
    throws UnsupportedOperationException, IOException;

	
	@Override public void write(Object doc, OutputStream stream, String filePath)
		throws IOException
	{
		if (doc instanceof ScoreDoc)
			write((ScoreDoc) doc, stream, filePath);
		else
			throw new IOException("Unsupported type");
	}
  

}
