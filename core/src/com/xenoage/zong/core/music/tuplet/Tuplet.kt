package com.xenoage.zong.core.music.tuplet

import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.music.chord.Chord


/**
 * Information about a tuplet (e.g. a triplet).
 *
 * This class is inspired by MusicXML's time-modification element.
 *
 * For example, to represent a triplet containing a quarter note
 * and an eighth note, that has the total duration of two eighth notes,
 * `baseDuration` is 1/8, `actualNotes` is 3
 * (because there is space for three eighth notes within the tuplet)
 * and `normalNotes` is 2 (because the duration of
 * the tuplet is two eighth notes).
 *
 * This class also stores the references of its chords.
 */
class Tuplet(
		/** Back reference: The chords, containing their real duration, e.g. 1/12 for an eight triplet chord.  */
		val chords: List<Chord>,
		val actualNotes: Int,
		val normalNotes: Int,
		val baseDuration: Fraction
) {

	init {
		check(chords.size >= 1, { "Tuplet must contain at least one chord" })
	}

	/** True, iff the bracket shown above or below the tuplet is visible. */
	var isBracketVisible = true

}
