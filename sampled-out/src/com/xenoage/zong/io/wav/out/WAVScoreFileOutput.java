package com.xenoage.zong.io.wav.out;

import static com.xenoage.utils.log.Log.log;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.utils.math.Fraction.fr;

import java.io.IOException;
import java.io.OutputStream;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.ScoreFileOutput;
import com.xenoage.zong.io.midi.out.MidiConverter;
import com.xenoage.zong.io.midi.out.SequenceContainer;
import com.xenoage.zong.io.midi.out.SynthManager;
import com.xenoage.zong.io.sampled.out.MidiToWaveRenderer;


/**
 * This class writes a Waveform Audio File Format (WAVE) file from a given {@link Score}.
 * 
 * @author Andreas Wenger
 */
public class WAVScoreFileOutput
	extends ScoreFileOutput
{


	@Override public void write(Score score, OutputStream outputStream, String filePath)
		throws IOException
	{
		//save WAVE file
		try {
			Fraction noteLength = fr(10, 10); //each note 100% length
			//create midi sequence
			SequenceContainer sc = MidiConverter.convertToSequence(score, false, false, noteLength);
			//for all instruments
			MidiToWaveRenderer.render(SynthManager.getSoundbank(), sc.sequence, null, outputStream);
		} catch (Exception ex) {
			log(warning(ex));
			throw new IOException(ex);
		}
	}

}
