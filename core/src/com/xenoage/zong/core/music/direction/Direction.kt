package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.chord.Chord
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
