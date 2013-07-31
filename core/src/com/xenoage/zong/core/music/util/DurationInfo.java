package com.xenoage.zong.core.music.util;

import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.utils.math.Fraction;


/**
 * This class contains methods that
 * provide information about a duration,
 * e.g. how much flags or which symbol it needs.
 *
 * @author Andreas Wenger
 */
public final class DurationInfo {

	/** Duration of a note symbol. */
	public enum Type {
		Whole,
		Half,
		Quarter,
		Eighth,
		_16th,
		_32th,
		_64th,
		_128th,
		_256th;
	}


	/**
	 * Gets the type of symbol that
	 * must be used for the given duration for a notehead.
	 */
	public static Type getNoteheadSymbolType(Fraction duration) {
		int length = duration.getNumerator() * 4 / duration.getDenominator(); //looseness does not matter
		if (length >= 4)
			return Type.Whole;
		else if (length >= 2)
			return Type.Half;
		else
			return Type.Quarter;
	}


	/**
	 * Gets the type of rest that must be used for the given duration.
	 * If the duration doesn't fit to a type exactly, the next bigger
	 * type is returned (so that tuplets are handled correctly) - TODO: better strategy needed?
	 */
	public static Type getRestType(Fraction duration) {
		float length = duration.getNumerator() * 4f / duration.getDenominator(); //looseness does not matter
		if (length <= 1 / 64f)
			return Type._256th;
		else if (length <= 1 / 32f)
			return Type._128th;
		else if (length <= 1 / 16f)
			return Type._64th;
		else if (length <= 1 / 8f)
			return Type._32th;
		else if (length <= 1 / 4f)
			return Type._16th;
		else if (length <= 1 / 2f)
			return Type.Eighth;
		else if (length <= 1)
			return Type.Quarter;
		else if (length <= 2)
			return Type.Half;
		else
			return Type.Whole;
	}


	/**
	 * Gets the number of flags of a unbeamed chord with the given
	 * duration. If the duration is equal or greater than 1/4,
	 * 0 is returned.
	 */
	public static int getFlagsCount(Fraction duration) {
		int n = duration.getNumerator();
		int d = duration.getDenominator();
		//quick test for common cases
		if (n == 1) {
			if (d <= 4)
				return 0; //4th or bigger
			switch (d) {
				case 8:
					return 1; //8th
				case 16:
					return 2; //16th
				case 32:
					return 3; //32th
				case 64:
					return 4; //64th
			}
		}
		//otherwise: compute the value
		while (n > 2) {
			d /= 2;
			n = n - 1;
		}
		if (d >= 256)
			return 6;
		else if (d >= 128)
			return 5;
		else if (d >= 64)
			return 4;
		else if (d >= 32)
			return 3;
		else if (d >= 16)
			return 2;
		else if (d >= 8)
			return 1;
		else
			return 0;
	}


	/**
	 * Gets the duration of the given base duration with the
	 * given number of dots.
	 */
	public static Fraction getDuration(Fraction baseDuration, int dots) {
		Fraction ret = baseDuration;
		if (dots == 1)
			ret = ret.mult(fr(3, 2)); //+ 50%
		else if (dots == 2)
			ret = ret.mult(fr(7, 4)); //+ 50% + 25%
		return ret;
	}


	/**
	 * Gets the number of dots of the given fraction.
	 * At maximum two dots are returned, otherwise 0 is returned.
	 */
	public static int getDots(Fraction duration) {
		int num = duration.getNumerator();
		//try to find dots:
		//find the base fraction
		int dotDenominator = 256; //start with 256th notes
		while (dotDenominator > 1 && duration.compareTo(fr(1, dotDenominator)) > 0) {
			dotDenominator /= 2;
		}
		//find the dots
		if (num == 3) {
			//1/dotDenominator with one dot
			return 1;
		}
		else if (num == 7) {
			//1/dotDenominator with two dots
			return 2;
		}
		else {
			//more than 2 dots? Not handled.
			return 0;
		}
	}


	/**
	 * Gets the base duration of the given duration, that is the
	 * duration without dots.
	 */
	public static Fraction getBaseDuration(Fraction duration) {
		Fraction ret = duration;
		int dots = getDots(duration);
		if (dots == 1)
			ret = ret.mult(fr(2, 3)); //remove the added 50%
		else if (dots == 2)
			ret = ret.mult(fr(4, 7)); //remove the added (50% + 25%)
		return ret;
	}

}
