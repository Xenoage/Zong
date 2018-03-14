package com.xenoage.zong.core.music.chord

import com.xenoage.zong.core.music.IS

/**
 * Class for a stem, that is belongs to a chord.
 */
data class Stem(
	/** The direction of the stem */
	val direction: StemDirection = StemDirection.Default,
	/** The length of the stem, measured from the outermost chord note at the stem side
	 * to the end of the stem, in interline spaces, or null for default. For example,
	 * a stem length of 3.5 IS is a common value for unbeamed chords. */
	val length: IS? = null
)

val defaultStem = Stem()
