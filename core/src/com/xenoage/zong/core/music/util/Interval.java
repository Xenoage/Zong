package com.xenoage.zong.core.music.util;

import static com.xenoage.zong.core.music.util.Interval.Result.FalseHigh;
import static com.xenoage.zong.core.music.util.Interval.Result.FalseLow;
import static com.xenoage.zong.core.music.util.Interval.Result.True;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.position.MP;


/**
 * This enumeration is used when searching elements
 * relative to a given position.
 * 
 * <ul>
 *   <li>Before: ]-∞, position[</li>
 *   <li>BeforeOrAt: ]-∞, position]</li>
 *   <li>At: [position, position]</li>
 *   <li>AtOrAfter: [position, +∞[</li>
 *   <li>After: ]position, +∞[</li>
 * </ul>
 * 
 * Meaning: "]...[" is exclusive, "[...]" is inclusive.
 * 
 * @author Andreas Wenger
 */
public enum Interval {

	/** Before a position. */
	Before,
	/** Before or exactly at a position. */
	BeforeOrAt,
	/** Exactly at a position. */
	At,
	/** After or exactly at a position. */
	AtOrAfter,
	/** After a position. */
	After;

	public enum Result {
		/** Not in the range, but before. */
		FalseLow,
		/** Within the range. */
		True,
		/** Not in the range, but afterwars. */
		FalseHigh;
	}


	/**
	 * Returns true, if the index is in the given interval
	 * relative to the given reference index, or if it is too low or too high.
	 */
	public Result isInInterval(int index, int referenceIndex) {
		int compare = (index > referenceIndex ? 1 : (index < referenceIndex ? -1 : 0));
		return isInInterval(compare);
	}


	/**
	 * Returns true, if the beat is in the given interval
	 * relative to the given reference beat, or if it is too low or too high.
	 */
	public Result isInInterval(Fraction beat, Fraction referenceBeat) {
		int compare = beat.compareTo(referenceBeat);
		return isInInterval(compare);
	}


	/**
	 * Returns, if the given {@link MP} is in the given interval
	 * relative to the given reference {@link MP}, or if it is too low or too high.
	 */
	public Result isInInterval(MP bmp, MP referenceMP) {
		int compare = bmp.compareTo(referenceMP);
		return isInInterval(compare);
	}


	private Result isInInterval(int compare) {
		switch (this) {
			case Before:
				return (compare < 0 ? True : FalseHigh);
			case BeforeOrAt:
				return (compare <= 0 ? True : FalseHigh);
			case At:
				return (compare == 0 ? True : (compare < 0 ? FalseLow : FalseHigh));
			case AtOrAfter:
				return (compare >= 0 ? True : FalseLow);
			case After:
				return (compare > 0 ? True : FalseLow);
		}
		throw new IllegalArgumentException("Unknown interval type: " + this);
	}


	/**
	 * Returns true, if this interval is {@link #Before}, {@link #BeforeOrAt} or {@link #At}.
	 */
	public boolean isPrecedingOrAt() {
		return this == Before || this == BeforeOrAt || this == At;
	}


}
