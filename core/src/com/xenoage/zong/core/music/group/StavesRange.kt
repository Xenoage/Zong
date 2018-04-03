package com.xenoage.zong.core.music.group

import kotlin.math.max
import kotlin.math.min

/**
 * Range of adjacent staves.
 */
data class StavesRange (
		/** The index of the first staff of the range.  */
		var start: Int,
		/** The index of the last staff of the range.  */
		var stop: Int
) {

	init {
		if (stop - start < 0)
			throw IllegalArgumentException("must contain at least 1 staff")
	}

	/** The number of staves. */
	val count: Int
		get() = stop - start + 1

	/** Gets this range as an [IntRange]. */
	val range: IntRange
		get() = IntRange(start, stop)

	/** True, if the group contains the whole given group. */
	operator fun contains(staves: StavesRange): Boolean =
			start <= staves.start && staves.stop <= stop

	/** True, if the group intersects with the given group. */
	fun intersects(staves: StavesRange): Boolean =
			start <= staves.stop && staves.start <= stop

	/** True, if the group contains the staff
	 * with the given index.
	 */
	operator fun contains(index: Int): Boolean =
		index in start .. stop

	/**
	 * Adds the given staves to this range.
	 */
	fun extendBy(staves: StavesRange) {
		start = min(staves.start, start)
		stop = max(staves.stop, stop)
	}

	/**
	 * Shifts the indices by the given amount.
	 */
	fun shift(amount: Int) {
		start += amount
		stop += amount
	}

	/**
	 * Shifts the end index by the given amount.
	 */
	fun shiftEnd(amount: Int) {
		stop += amount
	}

	/**
	 * Inserts the given number of staves at the given position.
	 * If the position is before the start, the whole range is shifted.
	 * If the position is at or after the start, but at or before the end, only the end is shifted
	 * (i.e. the range is enlarged).
	 * If the position is after the end, nothing happens.
	 */
	fun insert(index: Int, stavesCount: Int) {
		if (index < start)
			shift(stavesCount) //shift start and end
		else if (index <= stop)
			shiftEnd(stavesCount) //shift only the end
	}

	/**
	 * Removes the given number of staves starting at the given position.
	 * If true is returned, the whole range was removed. If false is returned,
	 * this range is still alive and the start and stop values were modified.
	 */
	fun remove(index: Int, stavesCount: Int): Boolean {
		val lastIndex = index + stavesCount - 1
		if (index <= start && lastIndex >= stop) {
			//whole range is removed
			return true
		} else if (index >= start && lastIndex <= stop) {
			//all removed staves are within this range
			stop -= stavesCount
		} else if (lastIndex < start) {
			//removed range before this range. shift start and end
			shift(-stavesCount)
		} else if (index > stop) {
			//removed range after this range. nothing to do.
		} else if (index < start && lastIndex >= start) {
			//a upper part of the range was removed
			start = index
			stop = stop - lastIndex
		} else if (index <= stop) {
			//a lower part of the range was removed
			stop = index - 1
		}
		return false
	}

}
