package com.xenoage.zong.desktop.io.wav.out;

import com.xenoage.utils.document.io.FileOutput;
import com.xenoage.utils.io.OutputStream;
import com.xenoage.utils.jse.io.JseOutputStream;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.desktop.io.midi.out.JseMidiSequenceWriter;
import com.xenoage.zong.desktop.io.midi.out.SynthManager;
import com.xenoage.zong.io.midi.out.MidiConverter;
import com.xenoage.zong.io.midi.out.MidiSequence;

import javax.sound.midi.Sequence;
import java.io.IOException;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.zong.io.midi.out.MidiConverter.Options.optionsForFileExport;

/**
 * This class writes a Waveform Audio File Format (WAVE) file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class WavScoreFileOutput
	extends FileOutput<Score> {

	@Override public void write(Score score, int fileIndex, OutputStream stream)
		throws IOException {
		writeWav(score, stream);
	}
	
	/**
	 * Writes the given score as a WAV file into the given stream.
	 */
	public static void writeWav(Score score, OutputStream stream)
		throws IOException {
		//save WAVE file
		try {
			//create midi sequence
			MidiSequence<Sequence> sequence = MidiConverter.convertToSequence(
				score, optionsForFileExport, new JseMidiSequenceWriter());
			//for all instruments
			MidiToWaveRenderer.render(SynthManager.getSoundbank(), sequence.getSequence(), null,
				new JseOutputStream(stream));
		} catch (Exception ex) {
			INSTANCE.log(Companion.warning(ex));
			throw new IOException(ex);
		}
	}

}
