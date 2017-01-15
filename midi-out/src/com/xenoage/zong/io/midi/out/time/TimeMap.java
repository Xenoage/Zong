package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.collections.IMap;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.repetitions.PlayRange;
import lombok.Data;
import lombok.val;

import java.util.Collections;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;

/**
 * Provides a MIDI tick position for each used {@link Time}
 * in each {@link PlayRange} in the score.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class TimeMap {

	private final IList<IMap<Time, Long>> ranges;

	public long getTick(Time time, int playRangeIndex) {
		val range = ranges.get(playRangeIndex);
		Long ret = range.get(time);
		if (ret == null)
			throw new IllegalArgumentException("Unknown time " + time + " in range " + playRangeIndex);
		return ret;
	}

	/**
	 * For debugging purposes: Returns the map as a string, sorted
	 * by ranges and time.
	 */
	@Override public String toString() {
		String s = "TimeMap ( rangesCount = " + ranges.size() + ", ranges = [\n";
		for (int iRange : range(ranges)) {
			val range = ranges.get(iRange);
			s += "range " + iRange + " = [";
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
