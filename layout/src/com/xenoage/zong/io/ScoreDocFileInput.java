package com.xenoage.zong.io;

import com.xenoage.utils.document.io.FileInput;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This is the interface for all classes that allow the creation of a
 * {@link ScoreDoc} from a file.
 * 
 * There may be a MusicXML reader and a MIDI reader for example.
 *
 * @author Andreas Wenger
 */
public interface ScoreDocFileInput
	extends FileInput<ScoreDoc> {

}
