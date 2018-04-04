package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.Measure
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.position.MPContainer


/**
 * Base class for all directions, like dynamics,
 * word directions or pedals.
 *
 * Directions are attached to measures or directly to voice elements.
 */
abstract class Direction : MeasureElement {

	private var _parent: MPContainer? = null

	/** Back reference: The parent measure, or null if not part of a measure. */
	override var parentMeasure: Measure?
		get() = _parent as? Measure?
		set(value) { _parent = value }

	/** Back reference: The parent voice element, or null if not attached to a voice element. */
	var parentVoiceElement: VoiceElement?
		get() = _parent as? VoiceElement?
		set(value) { _parent = value }

	/** Custom position, or null for the default position.  */
	var positioning: Positioning? = null

}


/** Sets the parent of this given element to the given measure and returns it. */
fun <T: Direction> T.setParent(measure: Measure?): T {
	if (this != null) parentMeasure = measure
	return this
}

/** Sets the parent of this given element to the given voice element and returns it. */
fun <T: Direction> T.setParent(voiceElement: VoiceElement?): T {
	if (this != null) parentVoiceElement = voiceElement
	return this
}

/** Sets the parent of this given element (if any) to null and returns it. */
fun <T: Direction> T?.unsetParent(): T? {
	if (this != null) parentMeasure = null
	return this
}
