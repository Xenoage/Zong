package com.xenoage.zong.core.music

import com.xenoage.zong.core.music.chord.Accidental
import com.xenoage.zong.core.music.clef.ClefType
import com.xenoage.zong.core.music.clef.clefTreble
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.key.TraditionalKey
import com.xenoage.zong.core.music.key.TraditionalKey.Mode
import java.util.*
import java.util.Collections.unmodifiableMap


/**
 * A music context provides information about the musical state at a specific position.
 *
 * These are the current clef, the key signature, the list of accidentals and the number of staff lines.
 */
class MusicContext(
		/** The current clef.  */
		val clef: ClefType,
		/** The current key signature.  */
		val key: Key,
		/** The list of current accidentals (key: pitch without alter, value: alter).  */
		val accidentals: Map<Pitch, Int>,
		/** The number of staff lines.  */
		val linesCount: Int
) {

	/** Creates a context with the given clef, key and list of accidentals. */
	constructor(clef: ClefType, key: Key, accidentals: Array<Pitch>, linesCount: Int) :
			this(clef, key,
					if(accidentals.size == 0) noAccidentals else accidentals.associate { it.withoutAlter() to it.alter },
					linesCount)

	/** Computes and returns the line position of the given pitch. */
	fun getLP(pitch: Pitch): LP =
		clef.getLP(pitch)

	/** Gets the accidental for the given pitch. When no accidental is needed, null is returned. */
	fun getAccidental(pitch: Pitch): Accidental? {
		//look, if this pitch is already set as an accidental
		val alter = accidentals[pitch.withoutAlter()]
		return if (alter != null) {
			if (alter == pitch.alter)
				null //we need no accidental
			else
				Accidental.fromAlter(pitch.alter) //we need to show the accidental
		}
		//look, if this alteration is already set in the key signature
		else if (key.alterations[pitch.step.ordinal] === pitch.alter)
			null //we need no accidental
		else
			Accidental.fromAlter(pitch.alter) //we need to show the accidental
	}

	companion object {
		val noAccidentals = mapOf<Pitch, Int>()

		val simpleMusicContext =
				MusicContext(clefTreble, TraditionalKey(0, Mode.Major), noAccidentals, 5)
	}

}
