package com.xenoage.zong.core.music

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.position.MPElement


/**
 * Measure elements are [MPElement]s, which can
 * appear in the middle of a measure and apply to
 * all voices of the measure.
 *
 * [Clef]s and [InstrumentChange]s are such elements.
 */
interface MeasureElement : MPElement {

	/** Back reference: The parent measure, or null if not part of a measure. */
	override var parent: Measure?

}

/** Sets the parent of this given element to the given container and returns it. */
fun <T: MeasureElement> T.setParent(container: Measure?): T {
	if (this != null) parent = container
	return this
}

/** Sets the parent of this given element (if any) to null and returns it. */
fun <T: MeasureElement> T?.unsetParent(): T? {
	if (this != null) parent = null
	return this
}
