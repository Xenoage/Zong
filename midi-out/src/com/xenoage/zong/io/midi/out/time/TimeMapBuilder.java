package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.CMap;
import com.xenoage.utils.collections.IMap;
import com.xenoage.zong.core.position.Time;
import lombok.val;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Range.range;

/**
 * Builder for a {@link TimeMap}.
 *
 * @author Andreas Wenger
 */
public class TimeMapBuilder {

	private CList<IMap<Time, Long>> ranges;

	public TimeMapBuilder(int playRangesCount) {
		ranges = clist(playRangesCount);
		for (int i : range(playRangesCount))
			ranges.add(new CMap<Time, Long>());
	}

	public void addTick(long tick, Time time, int playRangeIndex) {
		val range = ranges.get(playRangeIndex);
		((CMap) range).put(time, tick);
	}

	public TimeMap build() {
		ranges.close();
		for (val range : ranges)
			((CMap) range).close();
		return new TimeMap(ranges);
	}

}
