package com.xenoage.zong.io.midi.out;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import com.leff.midi.MidiFile;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.ScoreFileOutput;


/**
 * This class writes a Midi-file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class MidiScoreFileOutput
	extends ScoreFileOutput
{

	@Override public void write(Score score, OutputStream outputStream, String filePath)
		throws IOException
	{
		/*SequenceContainer container = MidiConverterA.convertToSequence(score, false, false);
		int[] types = MidiSystem.getMidiFileTypes(container.sequence);
		int type = 0;
		if (types.length != 0) {
			type = types[types.length - 1];
		}
		MidiSystem.write(container.sequence, type, outputStream);
		log(remark("Midi file written in format " + type));*/
		MidiFile midi = MidiConverterAndroid.convertToMidiFile(score, false, false); //GOON
		midi.writeToFile(new File(filePath));
	}

}
