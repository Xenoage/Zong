package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.position.Time;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A playback range from a given {@link Time} to a given {@link Time}.
 *
 * We call this range a "repetition", since usually a score consists of many
 * of repeated parts and jumps. If a score has no repeats at all, it consists
 * of a single "repetition". The old name was "play range", but we think the
 * new name is more meaningful in most cases.
 *
 * @author Andreas Wenger
 */
@Const @Data @AllArgsConstructor
public final class Repetition {

	/** The beginning of the range. This is the first position where notes are played. */
	public final Time start;
	/** The ending of the range. Notes at this position are not played any more. */
	public final Time end;


	/**
	 * Returns true, if the given {@link Time} is at or after the start time of
	 * this {@link Repetition} and before its end time.
	 */
	public boolean contains(Time time) {
		if (time.measure > start.measure && time.measure < end.measure)
			return true; //somewhere in the middle
		else if (time.measure == start.measure && time.beat.compareTo(start.beat) >= 0)
			return true; //in the first measure, at or after the start beat
		else if (time.measure == end.measure && time.beat.compareTo(end.beat) < 0)
			return true; //in the last measure, before the start beat
		else
			return false; //outside the range
	}


	@Override public String toString() {
		return String.format("[%d;%s to %d;%s]", start.measure, start.beat, end.measure, end.beat);
	}

}