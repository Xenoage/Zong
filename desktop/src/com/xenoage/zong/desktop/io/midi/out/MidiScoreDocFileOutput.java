package com.xenoage.zong.desktop.io.midi.out;

import java.io.IOException;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.FilenameUtils;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.zong.documents.ScoreDoc;

/**
 * This class writes one or more MIDI files from a given {@link ScoreDoc}.
 * 
 * If there is just one score in the document, a single file is created.
 * If the document has multiple scores, one MIDI file for each score is created and
 * named according to {@link FilenameUtils#numberFiles(String, int)}.
 * 
 * @author Andreas Wenger
 */
public class MidiScoreDocFileOutput
	extends FileOutput<ScoreDoc> {
	
	@Override public void write(ScoreDoc document, int fileIndex, OutputStream stream)
		throws IOException {
		MidiScoreFileOutput.writeMidi(document.getScore(), stream);
	}

}
