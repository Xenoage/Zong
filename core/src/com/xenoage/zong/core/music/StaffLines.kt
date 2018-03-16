package com.xenoage.zong.core.music

import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason
import com.xenoage.utils.annotations.Reason.MemorySaving

/**
 * Number of lines in a staff. In most cases 5-line staves are used.
 *
 * Instead of just using an integer value for the number of staff lines,
 * this class also provides information about [LP]s in the staff (like the bottom,
 * middle or top line [LP]), so that it does not have to be recomputed each time.
 */
class StaffLines private constructor(
		/** Number of staff lines.  */
		val count: Int
) {

	/** [LP] of the middle line. For an even number of staff lines, this will
	 * return the [LP] of the middle space. */
	val middleLp: LP
		get() = count - 1

	/** [LP] of the top line.  */
	val topLp: LP
		get() = (count - 1) * 2

	/** [LP] of the first bottom leger line [LP]. */
	val topLegerLp: LP
		get() = count * 2

	companion object {

		/** LP of the bottom line. This is always 0 by definition. */
		val bottomLp = 0

		/** LP of the first bottom leger line LP. This is always -2 by definition. */
		val bottomLegerLp = -2

		/** Normal 5-line staff.  */
		val staff5Lines = StaffLines(5)

		/** Staves from 1 to 10 lines.  */
		private val staffLines = arrayOf(
				StaffLines(1), StaffLines(2), StaffLines(3), StaffLines(4),
				StaffLines(5), StaffLines(6), StaffLines(7), StaffLines(8),
				StaffLines(9), StaffLines(10))

		/**
		 * Returns a [StaffLines] instance for the given number of staff lines.
		 * Shared instances are used for the common values.
		 */
		@Optimized(MemorySaving)
		operator fun invoke(count: Int): StaffLines =
			when {
				count < 1 -> throw IllegalArgumentException("Staff must have at least one line")
				count <= 10 -> staffLines[count - 1]
				else -> StaffLines(count)
			}

	}

}
