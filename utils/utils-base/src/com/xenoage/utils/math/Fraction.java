package com.xenoage.utils.math;

import java.util.Comparator;

/**
 * This class represents a fraction of two integer values.
 * 
 * It can for example be used to represent durations.
 * When possible, the fraction is cancelled automatically.
 *
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public final class Fraction
	implements Comparable<Fraction> {

	public static final Fraction _0 = fr(0);
	public static final Fraction _1 = fr(1);
	public static final Fraction _1$2 = fr(1, 2);
	public static final Fraction _1$4 = fr(1, 4);
	public static final Fraction _1$8 = fr(1, 8);
	public static final Fraction _1$16 = fr(1, 16);
	public static final Fraction _1$32 = fr(1, 32);
	public static final Fraction _1$64 = fr(1, 64);

	public static final Fraction _3$4 = fr(3, 4);

	private final int numerator;
	private final int denominator;

	public static Comparator<Fraction> comparator = Fraction::compareTo;


	/**
	 * Creates a new fraction with the given numerator.
	 * The denominator is 1.
	 */
	private Fraction(int number) {
		this.numerator = number;
		this.denominator = 1;
	}

	/**
	 * Creates a new fraction with the given numerator
	 * and denominator.
	 */
	private Fraction(int numerator, int denominator) {
		if (denominator == 0)
			throw new IllegalArgumentException("Denominator may not be 0");
		//if fraction is negative, always the numerator is negative
		int absNum = Math.abs(numerator);
		int absDen = Math.abs(denominator);
		if (numerator < 0 || denominator < 0) {
			if (numerator < 0 && denominator < 0) {
				numerator = absNum;
			}
			else {
				numerator = -1 * absNum;
			}
			denominator = absDen;
		}
		int gcd = MathUtils.clampMin(MathUtils.gcd(absNum, absDen), 1);
		this.numerator = numerator / gcd;
		this.denominator = denominator / gcd;
	}

	/**
	 * Creates a new fraction with the given numerator
	 * and denominator.
	 */
	public static Fraction fr(int numerator, int denominator) {
		return new Fraction(numerator, denominator);
	}

	/**
	 * Creates a new fraction with the given numerator.
	 * The denominator is 1.
	 */
	public static Fraction fr(int number) {
		return new Fraction(number);
	}

	/**
	 * Creates a new fraction from the given string, which must have the format
	 * "x", "x/y" or "z+x/y" for x and y and z being an integer.
	 */
	public static Fraction fromString(String s) {
		String[] plus = s.split("\\+");
		if (plus.length == 1) {
			String[] div = s.split("/");
			if (div.length == 1)
				return fr(Integer.parseInt(s));
			else if (div.length == 2)
				return fr(Integer.parseInt(div[0]), Integer.parseInt(div[1]));
		}
		else if (plus.length == 2) {
			return fr(Integer.parseInt(plus[0])).add(fromString(plus[1]));
		}
		throw new IllegalArgumentException("Invalid fraction: " + s);
	}

	/**
	 * Gets the numerator.
	 */
	public int getNumerator() {
		return numerator;
	}

	/**
	 * Gets the denominator.
	 */
	public int getDenominator() {
		return denominator;
	}

	/**
	 * Adds the given Fraction to this one and returns the result.
	 */
	public Fraction add(Fraction fraction) {
		if (fraction == null)
			return this;
		int numerator = this.numerator * fraction.denominator + this.denominator * fraction.numerator;
		int denominator = this.denominator * fraction.denominator;
		return fr(numerator, denominator);
	}

	/**
	 * Subtracts the given fraction from this one and returns the result..
	 */
	public Fraction sub(Fraction fraction) {
		if (fraction == null)
			return this;
		int numerator = this.numerator * fraction.denominator - this.denominator * fraction.numerator;
		int denominator = this.denominator * fraction.denominator;
		return fr(numerator, denominator);
	}

	/**
	 * Inverts this fraction and returns the result.
	 */
	public Fraction invert() {
		return fr(-numerator, denominator);
	}

	/**
	 * Compares this fraction with the given one.
	 * @return  the value <code>0</code> if this fraction is
	 *    equal to the given one; -1 if this fraction is numerically less
	 *    than the given one; 1 if this fraction is numerically
	 *    greater than the given one.
	 */
	@Override public int compareTo(Fraction fraction) {
		Fraction compare = this.sub(fraction);
		if (compare.numerator < 0)
			return -1;
		else if (compare.numerator == 0)
			return 0;
		else
			return 1;
	}

	/**
	 * Returns this fraction as a String in the
	 * format "numerator/denominator", e.g. "3/4".
	 */
	@Override public String toString() {
		return numerator + "/" + denominator;
	}

	/**
	 * Returns true, if the given object is a fraction
	 * that is numerically equal to this one, otherwise
	 * false.
	 */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		else if (o instanceof Fraction) {
			Fraction fraction = (Fraction) o;
			if (this.numerator == 0 && fraction.numerator == 0)
				return true;
			else
				return (this.numerator == fraction.numerator && this.denominator == fraction.denominator);
		}
		else {
			return false;
		}
	}

	@Override public int hashCode() {
		return numerator + denominator * 100000;
	}

	/**
	 * Returns this fraction as a float value (which may
	 * have rounding errors).
	 */
	public float toFloat() {
		return 1.0f * numerator / denominator;
	}

	/**
	 * Parses the given string in the format "x/y" or "x".
	 */
	public static Fraction parse(String s) {
		int slashPos = s.indexOf("/");
		if (slashPos == -1) {
			return fr(Integer.parseInt(s));
		}
		else {
			return fr(Integer.parseInt(s.substring(0, slashPos)),
				Integer.parseInt(s.substring(slashPos + 1)));
		}
	}

	/**
	 * Divides this {@link Fraction} by the given one.
	 */
	public Fraction divideBy(Fraction fraction) {
		return fr(this.numerator * fraction.denominator, this.denominator * fraction.numerator);
	}

	/**
	 * Multiplies this {@link Fraction} with the given one.
	 */
	public Fraction mult(Fraction fraction) {
		return fr(this.numerator * fraction.numerator, this.denominator * fraction.denominator);
	}

	/**
	 * Multiplies this {@link Fraction} with the given scalar value.
	 */
	public Fraction mult(int scalar) {
		return fr(this.numerator * scalar, this.denominator);
	}

	/**
	 * Returns true, if this fraction is 0.
	 */
	public boolean is0() {
		return numerator == 0;
	}

	/**
	 * Returns true, if this fraction is greater than 0.
	 */
	public boolean isGreater0() {
		return numerator > 0;
	}
	
	/**
	 * Returns true, if this fraction is less than the given one.
	 */
	public boolean isLessThan(Fraction fr) {
		return compareTo(fr) < 0;
	}
	
	/**
	 * Returns true, if this fraction is greater than the given one.
	 */
	public boolean isGreaterThan(Fraction fr) {
		return compareTo(fr) > 0;
	}

	/**
	 * Gets a comparator for fractions.
	 */
	public static Comparator<Fraction> getComparator() {
		return comparator;
	}

}
