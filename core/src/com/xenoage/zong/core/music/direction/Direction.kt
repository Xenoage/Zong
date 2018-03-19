package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.Measure
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.position.MPElement


/**
 * Base class for all directions, like dynamics,
 * word directions or pedals.
 *
 * Directions are attached to measures or directly to voice elements.
 */
interface Direction : MPElement {

	/** Parent measure or voice element, or null if not attached. */
	override var parent: DirectionContainer?

	/** Custom position, or null for the default position.  */
	var positioning: Positioning?

}


/** Sets the parent of this given element to the given container and returns it. */
fun <T: Direction> T.setParent(container: DirectionContainer?): T {
	if (this != null) parent = container
	return this
}

/** Sets the parent of this given element (if any) to null and returns it. */
fun <T: Direction> T?.unsetParent(): T? {
	if (this != null) parent = null
	return this
}
