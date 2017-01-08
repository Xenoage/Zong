package com.xenoage.zong.core.position;

import com.xenoage.utils.math.Fraction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Wither;

import static com.xenoage.utils.math.Fraction._0;

/**
 * Beat Position in a score.
 * Like a {@link MP}, but consists only of measure and beat.
 *
 * @author Andreas Wenger
 */
@Data @AllArgsConstructor public class BP
		implements Comparable<BP> {

	/** The measure index. */
	@Wither public final int measure;
	/** The beat. */
	@Wither public final Fraction beat;

	/** Musical position with all values set to 0. */
	public static final BP bp0 = new BP(0, _0);


	/**
	 * Creates a new {@link BP} at the given measure and beat.
	 */
	public static BP bp(int measure, Fraction beat) {
		return new BP(measure, beat);
	}

	@Override public String toString() {
		return "[Measure = " + measure + ", Beat = " + beat.getNumerator() + "/" + beat.getDenominator() + "]";
	}

	/**
	 * Compares this {@link BP} with the given one.
	 */
	@Override public int compareTo(BP bp) {
		//measure
		if (measure < bp.measure)
			return -1;
		else if (measure > bp.measure)
			return 1;
		else
			return beat.compareTo(bp.beat);
	}

}
