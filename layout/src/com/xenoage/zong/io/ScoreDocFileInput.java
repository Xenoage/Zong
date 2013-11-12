package com.xenoage.zong.io;

import java.io.IOException;
import java.io.InputStream;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.exceptions.InvalidFormatException;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.documents.ScoreDoc;


/**
 * This is the interface for all classes that allow the creation of a
 * {@link ScoreDoc} or {@link Score} instance from a file.
 * 
 * There may be a MusicXML reader and a MIDI reader for example.
 *
 * @author Andreas Wenger
 */
public interface ScoreDocFileInput
	extends FileInput
{
  
  
  /**
   * Creates a {@link ScoreDoc} instance from the document behind the given input stream.
   * If the file path is known too, it can be given, otherwise it is null.
   */
  @Override public ScoreDoc read(InputStream inputStream, @MaybeNull String filePath)
    throws InvalidFormatException, IOException;
  

}
