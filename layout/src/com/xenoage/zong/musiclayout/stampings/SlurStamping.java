package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.slur.Slur;

/**
 * Class for {@link Slur} stamping, that represents a slur
 * or a tie (both have the same shape).
 * 
 * A slur has a horizontal start and end position in mm,
 * and a vertical start and end position as a line position.
 * 
 * Additionally, there are two control points to form
 * the slur/tie as a bezier curve, each for the
 * start and the end point. These are given as a horizontal
 * offset in mm and a vertical offset in line position relative
 * to the start and end point respectively.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter
public final class SlurStamping
	extends Stamping {

	/** The stamped slur or tie. */
	public final Slur slur;
	/** The start position (left). */
	public final SP p1;
	/** The end position (right). */
	public final SP p2;
	/** The offset of the first control point relative to the start point. */
	public final SP c1;
	/** The offset of the second control point relative to the end point. */
	public final SP c2;
	/** The parent staff. */
	public final StaffStamping staff;


	@Override public StampingType getType() {
		return StampingType.SlurStamping;
	}

	@Override public Level getLevel() {
		return Level.Music;
	}
	
}
