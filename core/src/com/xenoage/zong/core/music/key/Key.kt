package com.xenoage.zong.core.music.key

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.Pitch


/**
 * Interface for key signatures.
 */
interface Key : ColumnElement {

	/** The alterations from the notes from C (0) to B (6) (see [Pitch] constants). */
	val alterations: List<Int>

	/** Returns the nearest higher [Pitch] in the current key. */
	fun getNearestHigherPitch(pitch: Pitch): Pitch

	/** Returns the nearest lower [Pitch] in the current key. */
	fun getNearestLowerPitch(pitch: Pitch): Pitch

}
