package com.xenoage.zong.musiclayout.spacer.beam;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;

/**
 * Possible values for the slant of a beam.
 * 
 * The slant is defined as the directed vertical distance between the outer LP
 * of the left stem and the outer LP of the right stem.
 * 
 * The slant may be fixed to one value or may be within an accepted range.
 * This allows corrections of the slant to apply to artistic ideals, like
 * avoiding white wedges within the staff (see Ross, p. 98, and Chlapik, p. 41).
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor(staticName="slant")
public final class Slant {
	
	public static final Slant horizontalSlant = new Slant(0, 0);
	
	/** The smallest possible slant in IS, e.g. -0.5 for one half-space down. */
	public float minIs;
	/** The biggest possible slant in IS, e.g. 0 for a horizontal beam. */
	public float maxIs;
	
	
	public static final Slant slant(float slantIs) {
		return new Slant(slantIs, slantIs);
	}
	
}
