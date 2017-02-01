package com.xenoage.zong.core.position;

import com.xenoage.utils.math.Fraction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;

import static com.xenoage.utils.math.Fraction._0;

/**
 * Time in a score.
 * Like a {@link MP}, but consists only of measure and beat.
 *
 * @author Andreas Wenger
 */
@Data @AllArgsConstructor public class Time
		implements Comparable<Time> {

	/** The measure index. */
	@Wither public final int measure;
	/** The beat. */
	@Wither public final Fraction beat;

	/** Musical position with all values set to 0. */
	public static final Time time0 = new Time(0, _0);


	/**
	 * Creates a new {@link Time} at the given measure and beat.
	 */
	public static Time time(int measure, Fraction beat) {
		return new Time(measure, beat);
	}

	/**
	 * Compares this {@link Time} with the given one.
	 */
	@Override public int compareTo(Time time) {
		//measure
		if (measure < time.measure)
			return -1;
		else if (measure > time.measure)
			return 1;
		else
			return beat.compareTo(time.beat);
	}

	@Override public String toString() {
		return "[Measure = " + measure + ", Beat = " + beat.getNumerator() + "/" + beat.getDenominator() + "]";
	}

	public String toStringCompact() {
		return "m" + measure + ",b" + beat.getNumerator() + "/" + beat.getDenominator();
	}

}
