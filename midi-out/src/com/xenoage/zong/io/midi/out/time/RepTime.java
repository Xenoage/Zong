package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;
import lombok.Data;

/**
 * A {@link Time} within a {@link Repetition}, given by its index.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class RepTime
	implements Comparable<RepTime> {

	/** The index of the repetition. */
	public final int repetition;
	/** The time. */
	public final Time time;


	/**
	 * Sorts first by ascending repetition index, then by ascending time.
	 */
	@Override public int compareTo(RepTime t) {
		if (repetition < t.repetition)
			return -1;
		else if (repetition > t.repetition)
			return 1;
		else
			return time.compareTo(t.time);
	}

}
