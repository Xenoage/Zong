package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.collections.IMap;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;
import lombok.Data;
import lombok.val;

import java.util.Collections;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;

/**
 * Provides a MIDI tick position for each used {@link Time}
 * in each {@link Repetition} in the score.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class TimeMap {

	private final IList<IMap<Time, Long>> repetitions;

	public long getTick(Time time, int repetitionIndex) {
		val repetition = repetitions.get(repetitionIndex);
		Long ret = repetition.get(time);
		if (ret == null)
			throw new IllegalArgumentException("Unknown time " + time + " in range " + repetitionIndex);
		return ret;
	}

	/**
	 * For debugging purposes: Returns the map as a string, sorted
	 * by ranges and time.
	 */
	@Override public String toString() {
		String s = "TimeMap ( repetitionsCount = " + repetitions.size() + ", repetitions = [\n";
		for (int iRepetition : range(repetitions)) {
			val range = repetitions.get(iRepetition);
			s += "repetition " + iRepetition + " = [";
			List<Time> times = alist(range.keySet());
			Collections.sort(times);
			for (val time : times)
				s += "{" + time.measure + "," + time.beat + " -> " + range.get(time) + "}, ";
			s += "]\n";
		}
		s += "])";
		return s;
	}

}
