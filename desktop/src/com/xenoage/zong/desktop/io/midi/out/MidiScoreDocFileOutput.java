package com.xenoage.zong.desktop.io.midi.out;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.io.FilenameUtils.numberFiles;
import static com.xenoage.utils.kernel.Range.range;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.FilenameUtils;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.documents.ScoreDoc;
import com.xenoage.zong.io.midi.out.MidiConverter;

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
	implements FileOutput<ScoreDoc> {
	
	@Override public void write(ScoreDoc document, OutputStream stream,
		String filePath)
		throws IOException {
		List<Sequence> sequences = alist();
		sequences.add(MidiConverter.convertToSequence(document.getScore(), false, false,
			new JseMidiSequenceWriter()).getSequence()); //TODO: currently there is only one score per document
		if (sequences.size() == 1 || filePath == null) {
			//simple case: just one score
			int type = MidiScoreFileOutput.getPreferredMidiType(sequences.get(0));
			MidiSystem.write(sequences.get(0), type, new JseOutputStream(stream));
		}
		else {
			//more scores: one MIDI file for each score
			List<String> filenames = numberFiles(filePath, sequences.size());
			for (int i : range(sequences)) {
				int type = MidiScoreFileOutput.getPreferredMidiType(sequences.get(i));
				MidiSystem.write(sequences.get(i), type, new FileOutputStream(filenames.get(i)));
			}
		}
	}

	/**
	 * Returns true, since multiple files may be required.
	 */
	@Override public boolean isFilePathRequired(ScoreDoc document) {
		return true;
	}

}
