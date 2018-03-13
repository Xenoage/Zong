package com.xenoage.zong.core.instrument

import com.xenoage.zong.core.music.Pitch
import lombok.NonNull

/**
 * Pitched instrument, like piano or trumpet.
 */
class PitchedInstrument(
		id: String
) : Instrument(id) {

	/** The MIDI program used for playback  */
	var midiProgram = 0
		set(midiProgram) {
			field = if (midiProgram in 0..128) midiProgram else
				throw IllegalArgumentException("MIDI program must be between 0 and 127")
		}

	/** The transposition information  */
	var transpose: Transpose = Transpose.noTranspose

	/** The bottommost playable note (in notation)  */
	var bottomPitch: Pitch? = null

	/** The topmost playable note (in notation)  */
	var topPitch: Pitch? = null

	/**
	 * The number of notes that can be played at the same time on
	 * this instrument, or 0 if there is no limit.
	 */
	var polyphonic: Int = 0

}
