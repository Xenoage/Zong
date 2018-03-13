package com.xenoage.zong.core.instrument

import com.xenoage.utils.lang.translate
import com.xenoage.zong.core.music.Pitch.pi

/**
 * Base class for an instrument.
 *
 * There are pitched instruments, like piano or trumpet,
 * and unpitched instruments, like drums.
 */
abstract class Instrument(
		/** The ID of the instrument (e.g. "instrument_piano") */
		var id: String
) {

	/** The international name of this instrument */
	var name: String = ""

	/** The international abbreviation of this instrument */
	var abbreviation: String? = null

	/** The groups in which the instrument is listed (e.g. woodwinds,
	 * percussion etc.), or empty if undefined. The empty list may be immutable.  */
	var groups: List<InstrumentGroup> = emptyList()

	/** The volume between 0 (silent) and 1 (full) */
	var volume: Float = 1f
		set(volume) {
			field = if (volume in 0..1) volume else
				throw IllegalArgumentException("Illegal volume value: $volume")
		}

	/** The panning between -1 (left) and 1 (right) */
	var pan: Float = 0f
		set(pan) {
			field = if (pan in -1..1) pan else
				throw IllegalArgumentException("Illegal pan value: $pan")
		}

	/**
	 * The localized name of this instrument. If it is undefined, the
	 * international name is returned.
	 */
	val localName: String
		get() = translate("${id}_Name", name)

	/**
	 * The localized abbreviation of this instrument. If it is undefined, the
	 * international abbreviation or name is returned.
	 */
	val localAbbreviation: String
		get() = translate("${id}_Abbr", abbreviation ?: name)

	/**
	 * Returns the localized groupnames in which the instrument is listed
	 * (e.g. woodwinds, percussion etc.).
	 */
	val localGroups: List<String>
		get() = groups.map { translate(it.id, it.name) }

	companion object {

		/** Default instrument: piano.  */
		val defaultInstrument = createDefaultInstrument()

		private fun createDefaultInstrument(): Instrument {
			val ret = PitchedInstrument("default")
			ret.midiProgram = 0
			ret.name = "Piano"
			ret.abbreviation = "Pno"
			ret.bottomPitch = pi(6, 0, 1)
			ret.topPitch = pi(0, 0, 8)
			return ret
		}
	}

}
