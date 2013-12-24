package com.xenoage.zong.desktop.io.midi.out;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.remark;

import java.io.IOException;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.midi.out.MidiConverter;
import com.xenoage.zong.io.midi.out.MidiSequence;


/**
 * This class writes a Midi-file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class MidiScoreFileOutput
	implements FileOutput<Score>
{

	//ANDROID: using android-midi-lib
	//MidiFile midi = MidiConverterAndroid.convertToMidiFile(score, false, false);
	//midi.writeToFile(new File(filePath));

	@Override public void write(Score score, OutputStream stream, String filePath)
		throws IOException {
		MidiSequence<Sequence> sequence = MidiConverter.convertToSequence(score, false, false,
			new JseMidiSequenceWriter());
		int[] types = MidiSystem.getMidiFileTypes(sequence.getSequence());
		int type = 0;
		if (types.length != 0) {
			type = types[types.length - 1];
		}
		MidiSystem.write(sequence.getSequence(), type, new JseOutputStream(stream));
		log(remark("Midi file written in format " + type));
	}

	@Override public boolean isFilePathRequired(Score document) {
		return false;
	}

}
