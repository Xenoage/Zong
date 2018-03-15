package com.xenoage.zong.core.music.key

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.Pitch
import com.xenoage.zong.core.position.MPContainer


/**
 * Interface for key signatures.
 *
 * Key signatures may appear for the whole measure column
 * (i.e. for all staves, in [ColumnHeader])
 * or also only within single measures.
 */
interface Key : MeasureElement, ColumnElement {

	/** The alterations from the notes from C (0) to B (6) (see [Pitch] constants). */
	val alterations: List<Int>

	/** Back reference: the parent element, or null, if not part of a score. */
	override var parent: MPContainer?

	/** Returns the nearest higher [Pitch] in the current key. */
	fun getNearestHigherPitch(pitch: Pitch): Pitch

	/** Returns the nearest lower [Pitch] in the current key. */
	fun getNearestLowerPitch(pitch: Pitch): Pitch

}
