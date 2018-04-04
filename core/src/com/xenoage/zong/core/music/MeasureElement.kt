package com.xenoage.zong.core.music

import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.direction.Direction
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement


/**
 * Measure elements are [MPElement]s, which can
 * appear in the middle of a measure and apply to
 * all voices of the measure.
 *
 * [Clef]s, [Direction]s and [InstrumentChange]s are such elements.
 */
interface MeasureElement : MPElement {

	/** Back reference: The parent measure, or null if not part of a measure. */
	var parentMeasure: Measure?

	override val parent: MPContainer?
		get() = parentMeasure

}

/** Sets the parent of this given element to the given container and returns it. */
fun <T: MeasureElement> T.setParent(container: Measure?): T {
	if (this != null) parentMeasure = container
	return this
}

/** Sets the parent of this given element (if any) to null and returns it. */
fun <T: MeasureElement> T?.unsetParent(): T? {
	if (this != null) parentMeasure = null
	return this
}