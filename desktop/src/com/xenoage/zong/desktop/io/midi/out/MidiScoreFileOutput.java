package com.xenoage.zong.desktop.io.midi.out;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.midi.out.MidiConverter;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import java.io.IOException;

import static com.xenoage.zong.io.midi.out.MidiConverter.Options.optionsForFileExport;

/**
 * This class writes a MIDI file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public class MidiScoreFileOutput
	extends FileOutput<Score>
{

	//ANDROID: using android-midi-lib
	//MidiFile midi = MidiConverterAndroid.convertToMidiFile(score, false, false);
	//midi.writeToFile(new File(filePath));

	@Override public void write(Score score, int fileIndex, OutputStream stream)
		throws IOException {
		writeMidi(score, stream);
	}
	
	/**
	 * Writes the given score as a MIDI file into the given stream.
	 */
	public static void writeMidi(Score score, OutputStream stream)
		throws IOException {
		Sequence sequence = MidiConverter.convertToSequence(score, optionsForFileExport,
				new JseMidiSequenceWriter()).getSequence();
		int type = MidiScoreFileOutput.getPreferredMidiType(sequence);
		MidiSystem.write(sequence, type, new JseOutputStream(stream));
	}

	/**
	 * Gets the preferred MIDI type. This is a "Standard-MIDI-File Type 1" file.
	 */
	public static int getPreferredMidiType(Sequence sequence) {
		int[] types = MidiSystem.getMidiFileTypes(sequence);
		int type = 0;
		if (types.length != 0) {
			type = types[types.length - 1];
		}
		return type;
	}
}
