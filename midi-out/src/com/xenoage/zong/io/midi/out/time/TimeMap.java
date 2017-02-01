package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static com.xenoage.utils.kernel.Range.range;

/**
 * Constant-time lookup of {@link MidiTime}s by providing one of
 * its components (MIDI tick, repetition index and time, or MIDI millisecond).
 *
 * @author Andreas Wenger
 */
@Const @RequiredArgsConstructor(access = AccessLevel.PACKAGE) @EqualsAndHashCode
public class TimeMap {

	/** The {@link RepTime}s for each repetition. */
	private final IList<RepTimes> repTimes;


	/**
	 * Gets the number of {@link Repetition}s.
	 */
	public int getRepetitionsCount() {
		return repTimes.size();
	}

	/**
	 * Returns the {@link MidiTime} at the given MIDI tick, or null.
	 */
	public MidiTime getByTick(long tick) {
		for (val repTime : repTimes)
			if (repTime.containsTick(tick))
				return repTime.getByTick(tick);
		return null;
	}

	/**
	 * Returns the {@link MidiTime} at the given {@link RepTime}, or null.
	 */
	public MidiTime getByRepTime(RepTime repTime) {
		return getByRepTime(repTime.repetition, repTime.time);
	}

	/**
	 * Returns the {@link MidiTime} at the given repetition index and {@link Time}, or null.
	 */
	public MidiTime getByRepTime(int repetition, Time time) {
		val repTime = repTimes.get(repetition);
		if (repTime.containsTime(time))
			return repTime.getByTime(time);
		return null;
	}

	/**
	 * Returns the {@link MidiTime} at the given {@link Time}, or null.
	 * The first repetition using this time is returned.
	 */
	public MidiTime getByTime(Time time) {
		for (val repTime : repTimes)
			if (repTime.containsTime(time))
				return repTime.getByTime(time);
		return null;
	}

	/**
	 * Returns the {@link MidiTime} at the given MIDI millisecond, or null.
	 */
	public MidiTime getByMs(long ms) {
		for (val repTime : repTimes)
			if (repTime.containsMs(ms))
				return repTime.getByMs(ms);
		return null;
	}

	/**
	 * Gets the sorted {@link Time}s of the given {@link Repetition}.
	 */
	public IList<Time> getTimesSorted(int repetition) {
		return repTimes.get(repetition).timesSorted;
	}

	/**
	 * For debugging purposes: Returns the time-to-tick map as a string, sorted
	 * by repetitions and time. The milliseconds are ignored in the output string.
	 */
	@Override public String toString() {
		String s = "TimeMap ( repetitions = [\n";
		for (int iRep : range(repTimes)) {
			s += "repetition " + iRep + " = [";
			val rep = repTimes.get(iRep);
			for (val time : rep.timesSorted) {
				val midiTime = rep.getByTime(time);
				s += "{" + time.measure + "," + time.beat + " -> " + midiTime.tick + "}, ";
			}
			s += "]\n";
		}
		s += "])";
		return s;
	}

}
