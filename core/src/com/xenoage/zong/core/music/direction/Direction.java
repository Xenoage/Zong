package com.xenoage.zong.core.music.direction;

import lombok.Data;

import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.format.Positioning;


/**
 * Base class for all directions, like dynamics,
 * word directions, segno or pedals.
 * 
 * Directions are either attached to chords (if they belong
 * to a certain chord and possibly following ones) or to the
 * column header, if they belong to all staves and voices, like segnos.
 *
 * @author Andreas Wenger
 */
@Data
public abstract class Direction
	implements MeasureElement, ColumnElement {

	/** Custom position, or null for the default position. */
	private Positioning positioning = null;

	/** Back reference: the element this direction is attached to, or null if not part of a score. */
	private DirectionContainer parent;

}
