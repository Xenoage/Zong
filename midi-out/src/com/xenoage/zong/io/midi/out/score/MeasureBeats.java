package com.xenoage.zong.io.midi.out.score;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.position.Time;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;

/**
 * The length of each measure column in a score.
 *
 * @author Andreas Wenger
 */
@Const @Data @AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeasureBeats {

	private final IList<Fraction> measureLengths;


	/**
	 * Finds the {@link MeasureBeats} in a {@link Score}.
	 */
	public static MeasureBeats findMeasureBeats(Score score) {
		CList<Fraction> lengths = clist();
		for (int iMeasure : range(score.getMeasuresCount()))
			lengths.add(score.getMeasureBeats(iMeasure));
		return new MeasureBeats(lengths.close());
	}

	/**
	 * Gets the length of the given measure column in beats.
	 * @param measure  the index of the measure. May be also one measure after
	 *                 the score ends, then 0 is returned.
	 */
	public Fraction getLength(int measure) {
		if (measure == measureLengths.size())
			return Companion.get_0();
		return measureLengths.get(measure);
	}

	/**
	 * Computes the beats between the given two {@link Time}s.
	 */
	public Fraction computeBeatsBetween(Time startTime, Time endTime) {
		if (startTime.compareTo(endTime) > 0) {
			throw new IllegalArgumentException("startTime > endTime");
		}
		else if (startTime.getMeasure() == endTime.getMeasure()) {
			//simple case: same measure
			return endTime.getBeat().sub(startTime.getBeat());
		}
		else {
			//first measure
			Fraction ret = getLength(startTime.getMeasure()).sub(startTime.getBeat());
			//measures inbetween
			for (int iMeasure : range(startTime.getMeasure() + 1, endTime.getMeasure() - 1))
				ret = ret.add(getLength(iMeasure));
			//last measure
			ret = ret.add(endTime.getBeat());
			return ret;
		}
	}

}
