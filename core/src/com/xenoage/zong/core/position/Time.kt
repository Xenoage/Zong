package com.xenoage.zong.core.position

import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math.`0/`

/**
 * Time in a score.
 * Like a [MP], but consists only of measure and beat.
 */
data class Time(
		/** The measure index */
		val measure: Int,
		/** The beat within the measure */
		val beat: Fraction
) : Comparable<Time> {

	/** Compares this [Time] with the given one */
	override fun compareTo(time: Time) = when {

		measure < time.measure -> -1
		measure > time.measure -> 1
		else -> beat.compareTo(time.beat)
	}

	override fun toString() =
			"[Measure = " + measure + ", Beat = " + beat.numerator + "/" + beat.denominator + "]"

	fun toStringCompact() =
			"m" + measure + ",b" + beat.numerator + "/" + beat.denominator

}

/** Creates a new [Time] at the given measure and beat. */
fun time(measure: Int, beat: Fraction) = Time(measure, beat)

/** Musical position with all values set to 0.  */
val time0 = Time(0, `0/`)