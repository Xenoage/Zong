package com.xenoage.zong.core.music.util

import com.xenoage.zong.core.music.util.Interval.Result.FalseHigh
import com.xenoage.zong.core.music.util.Interval.Result.FalseLow
import com.xenoage.zong.core.music.util.Interval.Result.True

import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.position.MP


/**
 * This enumeration is used when searching elements
 * relative to a given position.
 *
 *  * Before: ]-∞, position[
 *  * BeforeOrAt: ]-∞, position]
 *  * At: [position, position]
 *  * AtOrAfter: [position, +∞[
 *  * After: ]position, +∞[
 *
 * Meaning: "]...[" is exclusive, "[...]" is inclusive.
 */
enum class Interval {

	/** Before a position.  */
	Before,
	/** Before or exactly at a position.  */
	BeforeOrAt,
	/** Exactly at a position.  */
	At,
	/** After or exactly at a position.  */
	AtOrAfter,
	/** After a position.  */
	After;

	/**
	 * Returns true, if this interval is [Before], [BeforeOrAt] or [At].
	 */
	val isPrecedingOrAt: Boolean
		get() = this == Before || this == BeforeOrAt || this == At


	enum class Result {
		/** Not in the range, but before.  */
		FalseLow,
		/** Within the range.  */
		True,
		/** Not in the range, but afterwars.  */
		FalseHigh
	}


	/**
	 * Returns true, if the index is in the given interval
	 * relative to the given reference index, or if it is too low or too high.
	 */
	fun isInInterval(index: Int, referenceIndex: Int): Result {
		val compare = if (index > referenceIndex) 1 else if (index < referenceIndex) -1 else 0
		return isInInterval(compare)
	}


	/**
	 * Returns true, if the beat is in the given interval
	 * relative to the given reference beat, or if it is too low or too high.
	 */
	fun isInInterval(beat: Fraction, referenceBeat: Fraction): Result {
		val compare = beat.compareTo(referenceBeat)
		return isInInterval(compare)
	}


	/**
	 * Returns, if the given [MP] is in the given interval
	 * relative to the given reference [MP], or if it is too low or too high.
	 */
	fun isInInterval(bmp: MP, referenceMP: MP): Result {
		val compare = bmp.compareTo(referenceMP)
		return isInInterval(compare)
	}


	private fun isInInterval(compare: Int): Result {
		return when (this) {
			Before -> if (compare < 0) True else FalseHigh
			BeforeOrAt -> if (compare <= 0) True else FalseHigh
			At -> if (compare == 0) True else if (compare < 0) FalseLow else FalseHigh
			AtOrAfter -> if (compare >= 0) True else FalseLow
			After -> if (compare > 0) True else FalseLow
		}
	}

}
