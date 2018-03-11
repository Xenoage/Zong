package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.collections.TriMap;
import com.xenoage.utils.kernel.Tuple3;
import com.xenoage.zong.core.position.Time;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.util.Collections;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.MathUtils.max;
import static com.xenoage.utils.math.MathUtils.min;
import static com.xenoage.zong.core.position.Time.time;
import static java.lang.Long.MAX_VALUE;
import static java.lang.Long.MIN_VALUE;

/**
 * Time mapping within a repetition.
 */
@Const @EqualsAndHashCode
public final class RepTimes {

	public final int repetition;
	private final TriMap<Long, Time, Long> timeMap;

	//cache
	public final IList<Time> timesSorted;
	public final long minTick;
	public final long maxTick;
	public final Time minTime;
	public final Time maxTime;
	public final long minMs;
	public final long maxMs;


	RepTimes(int repetition, TriMap<Long, Time, Long> timeMap) {
		if (timeMap.size() == 0)
			throw new IllegalArgumentException("At least one time is required");

		this.repetition = repetition;
		this.timeMap = timeMap;

		//compute list of times and find minimum and maximum values
		CList<Time> times = clist(timeMap.size());
		long minTick = MAX_VALUE;
		long maxTick = MIN_VALUE;
		Time minTime = Companion.time(Integer.MAX_VALUE, Companion.get_0());
		Time maxTime = Companion.time(-1, Companion.get_0());
		long minMs = MAX_VALUE;
		long maxMs = MIN_VALUE;
		for (val tick : timeMap.getKeys1()) {
			val time = timeMap.getBy1(tick);
			times.add(time.get2());
			minTick = INSTANCE.min(minTick, time.get1());
			maxTick = INSTANCE.max(maxTick, time.get1());
			minTime = INSTANCE.min(minTime, time.get2());
			maxTime = INSTANCE.max(maxTime, time.get2());
			minMs = INSTANCE.min(minMs, time.get3());
			maxMs = INSTANCE.max(maxMs, time.get3());
		}
		Collections.sort(times);
		this.timesSorted = times.close();
		this.minTick = minTick;
		this.maxTick = maxTick;
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.minMs = minMs;
		this.maxMs = maxMs;
	}

	/**
	 * Returns the {@link MidiTime} at the given MIDI tick, or null.
	 */
	public MidiTime getByTick(long tick) {
		return get(timeMap.getBy1(tick));
	}

	/**
	 * Returns the {@link MidiTime} at the given {@link Time}, or null.
	 */
	public MidiTime getByTime(Time time) {
		return get(timeMap.getBy2(time));
	}

	/**
	 * Returns the {@link MidiTime} at the given MIDI millisecond, or null.
	 */
	public MidiTime getByMs(long ms) {
		return get(timeMap.getBy3(ms));
	}

	private MidiTime get(Tuple3<Long, Time, Long> value) {
		if (value == null)
			return null;
		return new MidiTime(value.get1(), new RepTime(repetition, value.get2()), value.get3());
	}

	public boolean containsTick(long tick) {
		return tick >= minTick && tick <= maxTick;
	}

	public boolean containsTime(Time time) {
		return time.compareTo(minTime) >= 0 && time.compareTo(maxTime) <= 0;
	}

	public boolean containsMs(long ms) {
		return ms >= minMs && ms <= maxMs;
	}

}
