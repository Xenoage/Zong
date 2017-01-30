package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.TriMap;
import com.xenoage.utils.kernel.Tuple3;
import com.xenoage.zong.core.position.Time;
import lombok.EqualsAndHashCode;
import lombok.val;

import java.util.Collections;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static java.lang.Math.max;

/**
 * Constant-time lookup of {@link MidiTime}s by providing one of
 * its components (MIDI tick, repetition index and time, or MIDI millisecond).
 *
 * @author Andreas Wenger
 */
@Const @EqualsAndHashCode
public class TimeMap {

	private final TriMap<Long, RepTime, Long> timeMap;
	private final int repetitionCount;


	TimeMap(TriMap<Long, RepTime, Long> timeMap) {
		this.timeMap = timeMap;
		//find max repetition index
		int maxRep = 0;
		for (val repTime : timeMap.getKeys2())
			maxRep = max(maxRep, repTime.repetition);
		this.repetitionCount = maxRep;
	}

	/**
	 * Returns the {@link MidiTime} at the given MIDI tick, or null.
	 */
	public MidiTime getByTick(long tick) {
		return get(timeMap.getBy1(tick));
	}

	/**
	 * Returns the {@link MidiTime} at the given {@link RepTime}, or null.
	 */
	public MidiTime getByRepTime(RepTime repTime) {
		return get(timeMap.getBy2(repTime));
	}

	/**
	 * Returns the {@link MidiTime} at the given repetition index and {@link Time}, or null.
	 */
	public MidiTime getByRepTime(int repetition, Time time) {
		return get(timeMap.getBy2(new RepTime(repetition, time)));
	}

	/**
	 * Returns the {@link MidiTime} at the given {@link Time}, or null.
	 * The first repetition using this time is returned.
	 */
	public MidiTime getByTime(Time time) {
		MidiTime ret;
		for (int iRep : range(repetitionCount))
			if ((ret = get(timeMap.getBy2(new RepTime(iRep, time)))) != null)
				return ret;
		return null;
	}

	/**
	 * Returns the {@link MidiTime} at the given MIDI millisecond, or null.
	 */
	public MidiTime getTimeByMs(long ms) {
		return get(timeMap.getBy3(ms));
	}

	private MidiTime get(Tuple3<Long, RepTime, Long> value) {
		if (value == null)
			return null;
		return new MidiTime(value.get1(), value.get2(), value.get3());
	}

	/**
	 * Gets the registered {@link RepTime}s (unsorted).
	 */
	public Iterable<RepTime> getRepTimes() {
		return it(timeMap.getKeys2());
	}

	/**
	 * For debugging purposes: Returns the time-to-tick map as a string, sorted
	 * by repetitions and time. The milliseconds are ignored in the output string.
	 */
	@Override public String toString() {
		String s = "TimeMap ( repetitions = [\n";
		val repTimes = alist(timeMap.getKeys2());
		Collections.sort(repTimes);
		if (repTimes.size() > 0) {
			int repsCount = repTimes.get(repTimes.size() - 1).repetition;
			for (int iRep : range(repsCount)) {
				s += "repetition " + iRep + " = [";
				for (val repTime : repTimes)
					if (repTime.repetition == iRep) {
						val time = getByRepTime(repTime);
						s += "{" + repTime.time.measure + "," + repTime.time.beat + " -> " +
								time.getTick() + "}, ";
					}
				s += "]\n";
			}
		}
		s += "])";
		return s;
	}

}
