package com.xenoage.zong.core.music.time;

import static com.xenoage.utils.math.Fraction.fr;
import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;


/**
 * Type of a time signature, e.g. a 4/4, alla breve or senza misura time.
 * 
 * This class includes some common instances which may be reused.
 * 
 * A senza-misura element time indicates that there is no time signature.
 * Thus as many beats as needed are allowed for each measure.
 * 
 * @author Andreas Wenger
 */
@Const @Data public final class TimeType {

	/** 2/2 time. */
	public static final TimeType time_2_2 = new TimeType(2, 2,
		TimeSymbol.Fractional, new boolean[]{true, false});
	/** 2/4 time. */
	public static final TimeType time_2_4 = new TimeType(2, 4,
		TimeSymbol.Fractional, new boolean[]{true, false});
	/** 3/4 time. */
	public static final TimeType time_3_4 = new TimeType(3, 4,
		TimeSymbol.Fractional, new boolean[]{true, false, false});
	/** 4/4 time. */
	public static final TimeType time_4_4 = new TimeType(4, 4,
		TimeSymbol.Fractional, new boolean[]{true, false, false, false});
	/** 6/8 time. */
	public static final TimeType time_6_8 = new TimeType(6, 8,
		TimeSymbol.Fractional, new boolean[]{true, false, false, true, false, false});
	/** Common time. */
	public static final TimeType timeCommon = new TimeType(4, 4,
		TimeSymbol.Common, new boolean[]{true, false, false, false});
	/** Alla breve time. */
	public static final TimeType timeAllaBreve = new TimeType(2, 2,
		TimeSymbol.AllaBreve, new boolean[]{true, false});
	/** Senza misura. */
	public static final TimeType timeSenzaMisura = new TimeType(0, 0,
		TimeSymbol.None, new boolean[0]);
	
	//list of known time types with numerator/denominator
	private static final TimeType[] knownTypes = {time_2_2, time_2_4, time_3_4, time_4_4, time_6_8};
	
	
	/** The time fraction (numerator and denominator).
	 * The beats per measure this time signature allows, e.g. (4/4) or (3/4).
	 * 0 means: Undefined, so as many beats as needed are allowed. */
	private final int numerator, denominator;
	/** The symbol of this time signature. For example, a (2/2) time may be represented
	 * by a normal fraction or or by a alla breve symbol. */
	private final TimeSymbol symbol;
	/** The accentuation of each beat. For example, usually a 4/4 time has <code>[x . . .]</code>
	 * and 6/8 has <code>[x . . x . .]</code>. <code>x</code> means accentuated (true in the array),
	 * <code>.</code> means unaccentuated (false in the array).
	 * If there are no beats (senza misura), the array is empty. */
	private final boolean[] beatsAccentuation;
	
	
	/**
	 * Returns a {@link TimeType} with the given numerator and denominator.
	 */
	public static TimeType timeType(int numerator, int denominator) {
		//look for existing instance
		for (TimeType t : knownTypes)
			if (t.numerator == numerator && t.denominator == denominator)
				return t;
		//create new TimeType. only the first beat is accentuated.
		boolean[] beatsAccentuation = new boolean[numerator];
		if (denominator > 0)
			beatsAccentuation[0] = true;
		return new TimeType(numerator, denominator, TimeSymbol.Fractional, beatsAccentuation);
	}
	
	/**
	 * Gets the beats of a measure in this time, or null for senza misura.
	 */
	public Fraction getMeasureBeats() {
		if (numerator == 0 || denominator == 0)
			return null;
		return fr(numerator, denominator);
	}

}
