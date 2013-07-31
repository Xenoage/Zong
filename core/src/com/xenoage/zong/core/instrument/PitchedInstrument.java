package com.xenoage.zong.core.instrument;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.xenoage.utils.base.annotations.Const;
import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NonNull;
import com.xenoage.zong.core.music.Pitch;


/**
 * Pitched instrument, like piano or trumpet.
 * 
 * @author Andreas Wenger
 */
@Const @Data @EqualsAndHashCode(callSuper = true) public final class PitchedInstrument
	extends Instrument {

	/** The MIDI program used for playback */
	private final int midiProgram;
	/** The transposition information */
	@NonNull private final Transpose transpose;
	/** The bottommost playable note (in notation) */
	@MaybeNull private final Pitch bottomPitch;
	/** The topmost playable note (in notation) */
	@MaybeNull private final Pitch topPitch;
	/** The number of notes that can be played at the same time with this instrument, or 0 if there is no limit. */
	private final int polyphonic;


	public PitchedInstrument(String id, InstrumentData data, int midiProgram, Transpose transpose, Pitch bottomPitch,
		Pitch topPitch, int polyphonic) {
		super(id, data);
		if (midiProgram < 0 || midiProgram > 128) {
			throw new IllegalArgumentException("MIDI program must be between 0 and 127");
		}
		this.midiProgram = midiProgram;
		this.transpose = transpose;
		this.bottomPitch = bottomPitch;
		this.topPitch = topPitch;
		this.polyphonic = polyphonic;
	}


}
