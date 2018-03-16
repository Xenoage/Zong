package com.xenoage.zong.core.music

import com.xenoage.zong.core.instrument.Instrument


/**
 * In most cases a part is simply a single staff assigned to a single instrument.
 *
 * It can also be group of staves that belong together, e.g. the two staves of a piano.
 * It can have more than one instrument, e.g. in percussion staves where several
 * instruments are notated in a single staff.
 */
class Part(
		/** The name of the part.  */
		val name: String,
		/** The abbreviation of the part, or null to use the name as the abbreviation.  */
		val abbreviation: String? = null,
		/** The number of staves in this part.  */
		val stavesCount: Int = 1,
		/** The instruments belonging to this part, or null to use the default instrument.  */
		val instruments: List<Instrument>? = null
) {

	init {
		if (stavesCount < 0)
			throw IllegalArgumentException("at least 1 staff is required")
	}

	/**
	 * Gets the first instrument in this part.
	 * If unset, the default instrument is returned.
	 */
	val firstInstrument: Instrument
		get() = instruments?.firstOrNull() ?: Instrument.defaultInstrument

}
