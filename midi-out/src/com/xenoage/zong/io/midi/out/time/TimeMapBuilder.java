package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.TriMap;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;

import java.util.List;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;

/**
 * Builder for a {@link TimeMap}.
 *
 * @author Andreas Wenger
 */
public class TimeMapBuilder {

	private List<TriMap<Long, Time, Long>> repTimes = alist();

	/**
	 * Adds the given time.
	 * @param tick     the MIDI tick
	 * @param repTime  the {@link Repetition} index and {@link Time}
	 * @param ms       the MIDI time in milliseconds, or null if still unknown.
	 *                 In this case ms = tick is used.
	 */
	public void addTime(long tick, RepTime repTime, long ms) {
		while (repTimes.size() <= repTime.repetition)
			repTimes.add(new TriMap<>());
		repTimes.get(repTime.repetition).put(tick, repTime.time, ms);
	}

	/**
	 * Adds the given time, when the millisecond is still unknown.
	 * In this case ms = tick is used.
	 */
	public void addTimeNoMs(long tick, RepTime repTime) {
		addTime(tick, repTime, tick);
	}

	public TimeMap build() {
		CList<RepTimes> repTimes = clist();
		for (int iRep : range(this.repTimes))
			repTimes.add(new RepTimes(iRep, this.repTimes.get(iRep)));
		return new TimeMap(repTimes.close());
	}

}
