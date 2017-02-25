package com.xenoage.zong.musiclayout.spacer.beam;

import com.xenoage.utils.annotations.Const;
import lombok.ToString;

import static java.lang.Math.*;

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
 * Since the slant is always measured in quarter spaces, to avoid rounding errors we
 * use an integer value for the number of quater spaces (QS).
 * 
 * @author Andreas Wenger
 */
@Const @ToString
public final class Slant {
	
	public static final Slant horizontalSlant = new Slant(0, 0, Direction.Horizontal);
	
	/** The smallest ("flattest") possible absolute slant in QS, e.g. 2 for one half-space up or down. */
	public final int minAbsQs;
	/** The biggest ("steepest") possible absolute slant in QS, e.g. 0 for a horizontal beam. */
	public final int maxAbsQs;
	/** The direction of the slant. */
	public final Direction direction;
	
	
	public static final Slant slant(float slantIs) {
		int slantAbsQs = abs(round(slantIs * 4));
		if (slantAbsQs == 0)
			return horizontalSlant;
		else
			return new Slant(slantAbsQs, slantAbsQs, slantIs > 0 ? Direction.Ascending : Direction.Descending);
	}
	
	public static final Slant slantIs(float absIs, Direction direction) {
		return slantIs(absIs, absIs, direction);
	}
	
	public static final Slant slantIs(float minAbsIs, float maxAbsIs, Direction direction) {
		int minAbsQs = round(minAbsIs * 4);
		int maxAbsQs = round(maxAbsIs * 4);
		return slantQs(minAbsQs, maxAbsQs, direction);
	}
	
	public static final Slant slantQs(int absQs, Direction direction) {
		return new Slant(absQs, absQs, direction);
	}
	
	public static final Slant slantQs(int minAbsQs, int maxAbsQs, Direction direction) {
		return new Slant(minAbsQs, maxAbsQs, direction);
	}
	
	private Slant(int minAbsQs, int maxAbsQs, Direction direction) {
		if (minAbsQs < 0 || maxAbsQs < minAbsQs)
			throw new IllegalArgumentException("Minimum slant must be > 0 (was " + minAbsQs + " qs) " +
				"and maximum slant (was " + minAbsQs + " qs) must be > minimum slant");
		this.minAbsQs = minAbsQs;
		this.maxAbsQs = maxAbsQs;
		this.direction = direction;
	}
	
	/**
	 * Returns true, iff the given slant in QS is within the range of this slant.
	 */
	public boolean contains(int slantQs) {
		if (direction == Direction.Descending)
			return (-maxAbsQs <= slantQs && slantQs <= -minAbsQs);
		else
			return (minAbsQs <= slantQs && slantQs <= maxAbsQs);
	}
	
	/**
	 * Limits the values to the given absolute value in quarter spaces.
	 */
	public Slant limitQs(int absQs) {
		return new Slant(min(absQs, minAbsQs), min(absQs, maxAbsQs), direction);
	}
	
	/**
	 * Gets the directed slant with the minimum absolute value in IS.
	 */
	public float getFlattestIs() {
		return minAbsQs * direction.getSign() / 4f;
		
	}
	
	/**
	 * Gets the minimum directed slant value.
	 */
	public float getMinIs() {
		return (direction == Direction.Ascending ? minAbsQs : maxAbsQs) * direction.getSign() / 4f;
	}
	
	/**
	 * Gets the maximum directed slant value.
	 */
	public float getMaxIs() {
		return (direction == Direction.Ascending ? maxAbsQs : minAbsQs) * direction.getSign() / 4f;
	}
	
}
