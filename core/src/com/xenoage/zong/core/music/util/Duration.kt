package com.xenoage.zong.core.music.util

import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math.Fraction.Companion.fr
import com.xenoage.zong.core.music.util.DurationType.*


/**
 * This file contains methods that
 * provide information about a duration,
 * e.g. how much flags or which symbol it needs.
 */

/** Musical duration, represented by a [Fraction] */
typealias Duration = Fraction

/** Basic duration types.  */
enum class DurationType {
	Whole,
	Half,
	Quarter,
	Eighth,
	_16th,
	_32th,
	_64th,
	_128th,
	_256th
}

/** The type of notehead that must be used for this duration */
val Duration.noteheadType: DurationType
	get() {
		val length = numerator * 4 / denominator //looseness does not matter
		return when {
			(length >= 4) -> Whole
			(length >= 2) -> Half
			else -> Quarter
		}
	}

/**
 * The type of rest that must be used for this duration.
 * If this duration doesn't fit to a type exactly, the next bigger
 * type is returned (so that tuplets are handled correctly) - TODO: better strategy needed?
 */
val Duration.restType: DurationType
	get() {
		val length = numerator * 4f / denominator //looseness does not matter
		return when {
			(length <= 1 / 64f) -> _256th
			(length <= 1 / 32f) -> _128th
			(length <= 1 / 16f) -> _64th
			(length <= 1 / 8f) -> _32th
			(length <= 1 / 4f) -> _16th
			(length <= 1 / 2f) -> Eighth
			(length <= 1) -> Quarter
			(length <= 2) -> Half
			else -> Whole
		}
	}

/**
 * The number of flags of a unbeamed chord with this duration.
 * If this duration is equal or greater than 1/4, 0 is returned.
 */
val Duration.flagsCount: Int
	get() {
		var n = numerator
		var d = denominator
		//quick test for common cases
		if (n == 1) {
			if (d <= 4)
				return 0 //4th or bigger
			when (d) {
				8 -> return 1 //8th
				16 -> return 2 //16th
				32 -> return 3 //32th
				64 -> return 4 //64th
			}
		}
		//otherwise: compute the value
		while (n > 2) {
			d /= 2 //one flag less (e.g. from 64th to 32nd)
			n /= 2 //get rid of the dot (e.g. from 3/64 to 1/32)
		}
		return when {
			(d >= 256) -> 6
			(d >= 128) -> 5
			(d >= 64) -> 4
			(d >= 32) -> 3
			(d >= 16) -> 2
			(d >= 8) -> 1
			else -> 0
		}
	}


/**
 * Gets the duration of this base duration with the
 * given number of dots.
 */
fun Duration.prolong(dots: Int): Duration {
	var ret = this
	if (dots == 1)
		ret *= _3_2 //+ 50%
	else if (dots == 2)
		ret *= _7_4 //+ 50% + 25%
	return ret
}


/**
 * The number of dots of this fraction.
 * At maximum two dots are returned, otherwise 0 is returned.
 */
val Duration.dots: Int
	get() {
		//try to find dots:
		//find the base fraction
		var dotDenominator = 256 //start with 256th notes
		while (dotDenominator > 1 && this > fr(1, dotDenominator))
			dotDenominator /= 2
		//find the dots
		return when (numerator) {
			3 -> 1 //1/dotDenominator with one dot
			7 -> 2 //1/dotDenominator with two dots
			else -> 0//more than 2 dots? Not handled.
		}
	}


/**
 * The base duration of this duration, that is the
 * duration without dots.
 */
val Duration.baseDuration: Duration
	get() = when(dots) {
		1 -> this * _2_3 //remove the added 50%
		2 -> this * _4_7 //remove the added (50% + 25%)
		else -> this
	}

/** More common durations */
val _3_2 = fr(3, 2)
val _2_3 = fr(2, 3)
val _7_4 = fr(7, 4)
val _4_7 = fr(4, 7)