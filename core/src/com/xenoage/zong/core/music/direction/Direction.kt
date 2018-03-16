package com.xenoage.zong.core.music.direction

import lombok.Data

import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.format.Positioning


/**
 * Base class for all directions, like dynamics,
 * word directions, segno or pedals.
 *
 * Directions are either attached to chords (if they belong
 * to a certain chord and possibly following ones) or to the
 * column header, if they belong to all staves and voices, like segnos.
 */
interface Direction : MeasureElement, ColumnElement {

	/** Custom position, or null for the default position.  */
	var positioning: Positioning? = null
		set(positioning) {
			field = this.positioning
		}

	/** Back reference: the element this direction is attached to, or null if not part of a score.  */
	override var parent: DirectionContainer? = null

}
