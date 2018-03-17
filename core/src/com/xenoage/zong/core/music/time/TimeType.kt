package com.xenoage.zong.core.music.time

import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason.MemorySaving
import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math.Fraction.Companion.fr


/**
 * Type of a time signature, e.g. a 4/4, alla breve or senza misura time.
 *
 * This class includes some common instances which may be reused.
 *
 * A senza-misura element time indicates that there is no time signature.
 * Thus as many beats as needed are allowed for each measure.
 */
class TimeType private constructor(
		/** The time fraction (numerator and denominator).
	 * The beats per measure this time signature allows, e.g. (4/4) or (3/4).
	 * 0 means: Undefined, so as many beats as needed are allowed. */
	val numerator: Int,
		val denominator: Int,
		/** The symbol of this time signature. For example, a (2/2) time may be represented
	 * by a normal fraction or by a alla breve symbol.  */
	val symbol: Symbol,
		/** The accentuation of each beat. For example, usually a 4/4 time has `[x . . .]`
	 * and 6/8 has `[x . . x . .]`. `x` means accentuated (true in the array),
	 * `.` means unaccentuated (false in the array).
	 * If there are no beats (senza misura), the array is empty.  */
	val beatsAccentuation: BooleanArray
) {

	/** The beats of a measure in this time, or null for senza misura. */
	val measureBeats: Fraction? =
			if (numerator == 0 || denominator == 0) null else fr(numerator, denominator)

	companion object {

		/** 2/2 time. */
		val time_2_2 = TimeType(2, 2,
				Symbol.Fractional, booleanArrayOf(true, false))
		/** 2/4 time. */
		val time_2_4 = TimeType(2, 4,
				Symbol.Fractional, booleanArrayOf(true, false))
		/** 3/4 time. */
		val time_3_4 = TimeType(3, 4,
				Symbol.Fractional, booleanArrayOf(true, false, false))
		/** 4/4 time. */
		val time_4_4 = TimeType(4, 4,
				Symbol.Fractional, booleanArrayOf(true, false, false, false))
		/** 6/8 time. */
		val time_6_8 = TimeType(6, 8,
				Symbol.Fractional, booleanArrayOf(true, false, false, true, false, false))
		/** Common time. */
		val timeCommon = TimeType(4, 4,
				Symbol.Common, booleanArrayOf(true, false, false, false))
		/** Alla breve time. */
		val timeAllaBreve = TimeType(2, 2,
				Symbol.AllaBreve, booleanArrayOf(true, false))
		/** Senza misura. */
		val timeSenzaMisura = TimeType(0, 0,
				Symbol.None, BooleanArray(0))

		//list of known time types with numerator/denominator
		private val knownTypes = arrayOf(time_2_2, time_2_4, time_3_4, time_4_4, time_6_8)

		/**
		 * Returns a [TimeType] with the given numerator and denominator.
		 * When possible, shared instances are returned.
		 */
		@Optimized(MemorySaving)
		operator fun invoke(numerator: Int, denominator: Int): TimeType {
			//look for existing instance
			for (t in knownTypes)
				if (t.numerator == numerator && t.denominator == denominator)
					return t
			//create new TimeType. only the first beat is accentuated.
			val beatsAccentuation = BooleanArray(numerator)
			if (denominator > 0)
				beatsAccentuation[0] = true
			return TimeType(numerator, denominator, Symbol.Fractional, beatsAccentuation)
		}

	}

	/**
	 * Symbols for time signatures.
	 */
	enum class Symbol {
		/** No symbol (for senza misura). */
		None,
		/** Common time symbol ("C") for a 4/4 time signature. */
		Common,
		/** Alla breve symbol (also known as "cut" symbol) for a 2/2 time signature. */
		AllaBreve,
		/** TimeSignature signature shown by numbers, e.g. 3/4 or 7/8. */
		Fractional
	}

}
