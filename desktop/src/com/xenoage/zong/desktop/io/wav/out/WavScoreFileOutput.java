package com.xenoage.zong.desktop.io.wav.out;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.utils.math.Fraction.fr;

import java.io.IOException;

import javax.sound.midi.Sequence;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.JseMidiSequenceWriter;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;
import com.xenoage.zong.io.midi.out.MidiConverter;
import com.xenoage.zong.io.midi.out.MidiSequence;

/**
 * This class writes a Waveform Audio File Format (WAVE) file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class WavScoreFileOutput
	implements FileOutput<Score> {

	@Override public void write(Score score, OutputStream stream, String filePath)
		throws IOException {
		writeWav(score, new JseOutputStream(stream));
	}
	
	/**
	 * Writes the given score as a WAV file into the given stream.
	 */
	public static void writeWav(Score score, java.io.OutputStream stream)
		throws IOException {
		//save WAVE file
		try {
			Fraction noteLength = fr(100, 100); //each note 100% length
			//create midi sequence
			MidiSequence<Sequence> sequence = MidiConverter.convertToSequence(
				score, false, false, noteLength, new JseMidiSequenceWriter());
			//for all instruments
			MidiToWaveRenderer.render(SynthManager.getSoundbank(), sequence.getSequence(), null,
				new JseOutputStream(stream));
		} catch (Exception ex) {
			log(warning(ex));
			throw new IOException(ex);
		}
	}

	@Override public boolean isFilePathRequired(Score document) {
		return false;
	}

}
