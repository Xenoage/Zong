package com.xenoage.zong.core.music

import com.xenoage.zong.core.position.MPElement


/**
 * Measure elements are [MPElement]s, which can
 * appear in the middle of a measure and apply to
 * all voices of the measure.
 *
 * Examples are clefs and directions and keys.
 */
interface MeasureElement : MPElement {

	/** Back reference: The parent measure, or null if not part of a measure. */
	var parentMeasure: Measure?
		get() = parent as? Measure?
		set(value) { parent = value }

}
