package com.xenoage.zong.core.instrument

/**
 * Transposition of an instrument.
 *
 * Like in MusicXML (following documentation copied), a transposition
 * contains the following data:
 *
 * The transposition is represented by chromatic steps
 * (required) and three optional elements: diatonic pitch
 * steps, octave changes, and doubling an octave down. The
 * chromatic and octave-change elements are numeric values
 * added to the encoded pitch data to create the sounding
 * pitch. The diatonic element is also numeric and allows
 * for correct spelling of enharmonic transpositions.
 */
data class Transpose(
	/** The number of chromatic steps to add to the pitch  */
	val chromatic: Int,
	/** The number of diatonic steps, or null for default  */
	val diatonic: Int? = null,
	/** Octave change (like -2 for 2 octaves down)  */
	val octaveChange: Int = 0,
	/** Copy pitch one octave down  */
	val isDoubleOctaveDown: Boolean = false
) {

	companion object {
		/** Instance with no transposition,  */
		val noTranspose = Transpose(0, 0, 0, false)
	}

}
