package com.xenoage.zong.core.instrument;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.Pitch;


/**
 * Pitched instrument, like piano or trumpet.
 * 
 * @author Andreas Wenger
 */
@Data @EqualsAndHashCode(callSuper = true)
public class PitchedInstrument
	extends Instrument {

	/** The MIDI program used for playback */
	private int midiProgram = 0;
	/** The transposition information */
	@NonNull private Transpose transpose = Transpose.noTranspose;
	/** The bottommost playable note (in notation) */
	@MaybeNull private Pitch bottomPitch = null;
	/** The topmost playable note (in notation) */
	@MaybeNull private Pitch topPitch = null;
	/** The number of notes that can be played at the same time with this instrument, or 0 if there is no limit. */
	private int polyphonic = 0;

	public PitchedInstrument(String id) {
		super(id);
	}
	
	public void setMidiProgram(int midiProgram) {
		if (midiProgram < 0 || midiProgram > 128)
			throw new IllegalArgumentException("MIDI program must be between 0 and 127");
		this.midiProgram = midiProgram;
	}

}
