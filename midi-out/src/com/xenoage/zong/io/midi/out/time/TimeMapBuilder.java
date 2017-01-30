package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.collections.TriMap;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;

import static com.xenoage.utils.collections.TriMap.triMap;

/**
 * Builder for a {@link TimeMap}.
 *
 * @author Andreas Wenger
 */
public class TimeMapBuilder {

	private TriMap<Long, RepTime, Long> timeMap = triMap();

	/**
	 * Adds the given time.
	 * @param tick     the MIDI tick
	 * @param repTime  the {@link Repetition} index and {@link Time}
	 * @param ms       the MIDI time in milliseconds, or null if still unknown.
	 *                 In this case ms = tick is used.
	 */
	public void addTime(long tick, RepTime repTime, long ms) {
		timeMap.put(tick, repTime, ms);
	}

	/**
	 * Adds the given time, when the millisecond is still unknown.
	 * In this case ms = tick is used.
	 */
	public void addTimeNoMs(long tick, RepTime repTime) {
		addTime(tick, repTime, tick);
	}

	public TimeMap build() {
		return new TimeMap(timeMap);
	}

}
