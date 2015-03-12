package com.xenoage.zong.musiclayout.spacer.beam;

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.ToString;

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
@Const @AllArgsConstructor(access=PRIVATE) @ToString
public final class Slant {
	
	public static final Slant horizontalSlant = new Slant(0, 0);
	
	/** The smallest possible slant in IS, e.g. -0.5 for one half-space down. */
	public final float minIs;
	/** The biggest possible slant in IS, e.g. 0 for a horizontal beam. */
	public final float maxIs;
	
	
	public static final Slant slant(float slantIs) {
		return new Slant(slantIs, slantIs);
	}
	
	public static final Slant slantDir(float absIs, int sign) {
		return slantDir(absIs, absIs, sign);
	}
	
	public static final Slant slantDir(float minAbsIs, float maxAbsIs, int sign) {
		if (sign > 0)
			return new Slant(minAbsIs * sign, maxAbsIs * sign);
		else
			return new Slant(maxAbsIs * sign, minAbsIs * sign);	
	}
	
	/**
	 * Returns true, iff the given slant is within the range of this slant.
	 */
	public boolean contains(float slantIs) {
		return (minIs <= slantIs && slantIs <= maxIs);
	}
	
	/**
	 * Limits the values to the given absolute value.
	 */
	public Slant limit(float absIs) {
		float min = (abs(minIs) > absIs ? absIs * signum(minIs) : minIs);
		float max = (abs(maxIs) > absIs ? absIs * signum(maxIs) : maxIs);
		return new Slant(min, max);
	}
}
