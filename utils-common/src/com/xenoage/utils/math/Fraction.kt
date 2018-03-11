package com.xenoage.utils.math

import kotlin.math.abs

/**
 * This class represents a fraction of two integer values.
 *
 * It can for example be used to represent durations.
 * When possible, the fraction is cancelled automatically.
 */
class Fraction : Comparable<Fraction> {

	val numerator: Int
	val denominator: Int

	/** True, if this fraction is 0 */
	val is0: Boolean
		get() = numerator == 0

	/** True, if this fraction is greater than 0 */
	val isGreater0: Boolean
		get() = numerator > 0

	/**
	 * Creates a new fraction with the given numerator.
	 * The denominator is 1.
	 */
	constructor(number: Int) {
		this.numerator = number
		this.denominator = 1
	}

	/**
	 * Creates a new fraction with the given numerator
	 * and denominator.
	 */
	constructor(numerator: Int, denominator: Int) {
		var numerator = numerator
		var denominator = denominator
		if (denominator == 0)
			throw IllegalArgumentException("Denominator may not be 0")
		//if fraction is negative, always the numerator is negative
		val absNum = abs(numerator)
		val absDen = abs(denominator)
		if (numerator < 0 || denominator < 0) {
			numerator = if (numerator < 0 && denominator < 0) absNum else -1 * absNum
			denominator = absDen
		}
		val gcd = clampMin(gcd(absNum, absDen), 1)
		this.numerator = numerator / gcd
		this.denominator = denominator / gcd
	}

	/**
	 * Adds the given Fraction to this one and returns the result.
	 */
	fun add(fraction: Fraction?): Fraction {
		if (fraction == null)
			return this
		val numerator = this.numerator * fraction.denominator + this.denominator * fraction.numerator
		val denominator = this.denominator * fraction.denominator
		return fr(numerator, denominator)
	}

	/**
	 * Subtracts the given fraction from this one and returns the result..
	 */
	fun sub(fraction: Fraction?): Fraction {
		if (fraction == null)
			return this
		val numerator = this.numerator * fraction.denominator - this.denominator * fraction.numerator
		val denominator = this.denominator * fraction.denominator
		return fr(numerator, denominator)
	}

	/**
	 * Inverts this fraction and returns the result.
	 */
	fun invert() = fr(-numerator, denominator)

	/**
	 * Compares this fraction with the given one.
	 * @return  the value 0 if this fraction is
	 * equal to the given one; -1 if this fraction is numerically less
	 * than the given one; 1 if this fraction is numerically
	 * greater than the given one.
	 */
	override fun compareTo(fraction: Fraction): Int {
		val compare = this.sub(fraction)
		return when {
			compare.numerator < 0 -> -1
			compare.numerator == 0 -> 0
			else -> 1
		}
	}

	/**
	 * Returns this fraction as a String in the
	 * format "numerator/denominator", e.g. "3/4".
	 */
	override fun toString() = numerator.toString() + "/" + denominator

	/**
	 * Returns this fraction as a float value (which may
	 * have rounding errors).
	 */
	fun toFloat(): Float = 1f * numerator / denominator

	/**
	 * Divides this [Fraction] by the given one.
	 */
	fun divideBy(fraction: Fraction) =
			fr(this.numerator * fraction.denominator, this.denominator * fraction.numerator)

	/**
	 * Multiplies this [Fraction] with the given one.
	 */
	fun mult(fraction: Fraction) =
			fr(this.numerator * fraction.numerator, this.denominator * fraction.denominator)

	/**
	 * Multiplies this [Fraction] with the given scalar value.
	 */
	fun mult(scalar: Int) =
			fr(this.numerator * scalar, this.denominator)

	/**
	 * Returns true, if this fraction is less than the given one.
	 */
	fun isLessThan(fr: Fraction): Boolean = compareTo(fr) < 0

	/**
	 * Returns true, if this fraction is greater than the given one.
	 */
	fun isGreaterThan(fr: Fraction): Boolean = compareTo(fr) > 0

	companion object {

		/**
		 * Gets a comparator for fractions.
		 */
		var comparator = Comparator<Fraction> { obj, fraction -> obj.compareTo(fraction) }

		/**
		 * Creates a new fraction from the given string, which must have the format
		 * "x", "x/y" or "z+x/y" for x and y and z being an integer.
		 */
		fun fromString(s: String): Fraction {
			val plus = s.split("\\+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			if (plus.size == 1) {
				val div = s.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
				if (div.size == 1)
					return fr(s.toInt())
				else if (div.size == 2)
					return fr(div[0].toInt(), div[1].toInt())
			} else if (plus.size == 2) {
				return fr(plus[0].toInt()).add(fromString(plus[1]))
			}
			throw IllegalArgumentException("Invalid fraction: $s")
		}
	}

}

val `0/` = fr(0)
val `1/` = fr(1)
val `1/2` = fr(1, 2)
val `1/4` = fr(1, 4)
val `1/8` = fr(1, 8)
val `1/16` = fr(1, 16)
val `1/32` = fr(1, 32)
val `1/64` = fr(1, 64)

val `3/4` = fr(3, 4)

/**
 * Creates a new fraction with the given numerator
 * and denominator.
 */
fun fr(numerator: Int, denominator: Int) = Fraction(numerator, denominator)

/**
 * Creates a new fraction with the given numerator.
 * The denominator is 1.
 */
fun fr(number: Int) = Fraction(number)