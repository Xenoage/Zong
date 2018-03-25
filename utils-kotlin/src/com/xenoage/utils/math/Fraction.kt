package com.xenoage.utils.math

import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason
import com.xenoage.utils.annotations.Reason.MemorySaving
import com.xenoage.utils.math.Fraction.Companion.fr
import kotlin.math.abs

/**
 * This class represents a fraction of two integer values.
 *
 * It can for example be used to represent durations.
 * When possible, the fraction is cancelled automatically.
 */
class Fraction(
		val numerator: Int,
		val denominator: Int
) : Comparable<Fraction> {

	/** True, if this fraction is 0 */
	val is0: Boolean
		get() = numerator == 0

	/** True, if this fraction is greater than 0 */
	val isGreater0: Boolean
		get() = numerator > 0

	/**
	 * Adds the given Fraction to this one and returns the result.
	 */
	operator fun plus(fraction: Fraction): Fraction {
		if (fraction.numerator == 0) return this
		val numerator = this.numerator * fraction.denominator + this.denominator * fraction.numerator
		val denominator = this.denominator * fraction.denominator
		return fr(numerator, denominator)
	}

	/**
	 * Subtracts the given fraction from this one and returns the result..
	 */
	operator fun minus(fraction: Fraction): Fraction {
		if (fraction.numerator == 0) return this
		val numerator = this.numerator * fraction.denominator - this.denominator * fraction.numerator
		val denominator = this.denominator * fraction.denominator
		return fr(numerator, denominator)
	}

	/**
	 * Inverts this fraction and returns the result.
	 */
	fun invert() = fr(-numerator, denominator)

	/**
	 * Inverts this fraction and returns the result.
	 */
	operator fun unaryMinus() = invert()

	/**
	 * Compares this fraction with the given one.
	 * @return  the value 0 if this fraction is
	 * equal to the given one; -1 if this fraction is numerically less
	 * than the given one; 1 if this fraction is numerically
	 * greater than the given one.
	 */
	override fun compareTo(other: Fraction): Int {
		val compare = this - other
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
	operator fun div(fraction: Fraction) =
			fr(this.numerator * fraction.denominator, this.denominator * fraction.numerator)

	/**
	 * Multiplies this [Fraction] with the given one.
	 */
	operator fun times(fraction: Fraction) =
			fr(this.numerator * fraction.numerator, this.denominator * fraction.denominator)

	/**
	 * Multiplies this [Fraction] with the given scalar value.
	 */
	operator fun times(scalar: Int) =
			fr(this.numerator * scalar, this.denominator)


	companion object {

		/**
		 * Creates a new fraction with the given numerator and denominator.
		 * For the most common fractions like 1, 1/2 or 1/4, shared instances are used.
		 */
		@Optimized(MemorySaving)
		fun fr(numerator: Int, denominator: Int): Fraction {

			//check values
			var num = numerator
			var den = denominator
			if (den == 0)
				throw IllegalArgumentException("Denominator may not be 0")
			if (num == 0)
				return _0

			//if fraction is negative, always the numerator is negative
			val absNum = abs(num)
			val absDen = abs(den)
			if (num < 0 || den < 0) {
				num = if (numerator < 0 && denominator < 0) absNum else -1 * absNum
				den = absDen
			}

			//shorten fraction
			val gcd = gcd(absNum, absDen).clampMin(1)
			num /= gcd
			den /= gcd

			//reuse shared instances for common fractions
			when (den) {
				1 -> when (num) {
					1 -> return _1
				}
				2 -> when (num) {
					1 -> return _1_2
				}
				3 -> when (num) {
					3 -> return _1_3
				}
				4 -> when (num) {
					1 -> return _1_4
					3 -> return _3_4
				}
				8 -> when (num) {
					1 -> return _1_8
					3 -> return _3_8
				}
				16 -> when (num) {
					1 -> return _1_16
					3 -> return _3_16
				}
				32 -> when (num) {
					1 -> return _1_32
					3 -> return _3_32
				}
				64 -> when (num) {
					1 -> return _1_64
					3 -> return _1_64
				}
			}

			return Fraction(num, den)
		}

		/**
		 * Creates a new fraction with the given numerator.
		 * The denominator is 1.
		 */
		fun fr(number: Int) = fr(number, 1)

		/** The comparator for fractions. */
		val comparator = Comparator<Fraction> { obj, fraction -> obj.compareTo(fraction) }

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
				return fr(plus[0].toInt()) + fromString(plus[1])
			}
			throw IllegalArgumentException("Invalid fraction: $s")
		}
	}

}

val _0 = fr(0)

val _1 = fr(1)

val _1_2 = fr(1, 2)

val _1_3 = fr(1, 2)

val _1_4 = fr(1, 4)
val _3_4 = fr(3, 4)

val _1_8 = fr(1, 8)
val _3_8 = fr(3, 8)

val _1_16 = fr(1, 16)
val _3_16 = fr(3, 16)

val _1_32 = fr(1, 32)
val _3_32 = fr(1, 32)

val _1_64 = fr(1, 64)
val _3_64 = fr(1, 64)