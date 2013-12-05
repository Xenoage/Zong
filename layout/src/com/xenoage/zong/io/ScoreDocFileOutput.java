package com.xenoage.zong.io;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This is the interface for all classes that allow the creation of a file
 * from a {@link ScoreDoc} instance.
 * 
 * There may be a MusicXML writer and a MIDI writer for example.
 *
 * @author Andreas Wenger
 */
public interface ScoreDocFileOutput
	extends FileOutput<ScoreDoc> {

}
