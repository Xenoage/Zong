package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.music.volta.Volta;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;

import static com.xenoage.utils.kernel.Range.range;
import static java.lang.Math.max;

/**
 * A group of consecutive {@link Volta}s with their start measure indices.
 * Use the {@link VoltaGroupFinder} to get volta groups from a score.
 *
 * See {@link Volta} for more details and ordering rules.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class VoltaGroup {

	@AllArgsConstructor
	public static class VoltaStartMeasure {
		Volta volta;
		int startMeasure;
	}

	//the voltas and their start measure indices.
	IList<VoltaStartMeasure> voltasStartMeasures;


	/**
	 * Returns the measure numbers of the measures that
	 * have to be played in the volta, when the playback reaches the
	 * volta block the given time (1-based), or null if no appropriate volta
	 * can be found.
	 * @deprecated unneeded
	 */
	@MaybeNull private Range getRange(int repeatTime) {
		val v = findVolta(repeatTime);
		if (v != null)
			return range(v.startMeasure, v.startMeasure + v.volta.getLength() - 1);
		else
			return null;
	}

	/**
	 * Returns whether this time is the last time to repeat.
	 * @deprecated unneeded
	 */
	private boolean isLastTime(int repeatTime) {
		return (repeatTime == getRepeatCount());
	}

	/**
	 * Gets the number of repetitions.
	 * See {@link Volta} for the playback rules.
	 * Notice, that the actual playback could be different,
	 * e.g. when a volta contains a jump (e.g. coda) to another
	 * measure.
	 */
	public int getRepeatCount() {
		//find maximum repeat number (+1, if the last volta is a default volta)
		int maxRepeatTime = 0;
		for (val v : voltasStartMeasures) {
			if (false == v.volta.isDefault())
				maxRepeatTime = max(maxRepeatTime, v.volta.getNumbers().getStop());
		}
		if (voltasStartMeasures.size() > 1 && //when there is at least one other volta before a final default volta
				voltasStartMeasures.getLast().volta.isDefault()) {
			maxRepeatTime += 1;
		}
		return maxRepeatTime;
	}

	/**
	 * Gets the volta and start measure where to jump into when
	 * the volta group is reached the given time.
	 * When there is no repeat for the given time, null is returned.
	 */
	@MaybeNull public VoltaStartMeasure findVolta(int repeatTime) {
		if (repeatTime < 1 || repeatTime > getRepeatCount())
			return null;
		//find explicit number in voltas first
		for (val v : voltasStartMeasures) {
			if (false == v.volta.isDefault() && v.volta.getNumbers().isInRange(repeatTime))
				return v;
		}
		//otherwise use first default volta after the last matching one
		VoltaStartMeasure last = null;
		for (val v : voltasStartMeasures) {
			if (v.volta.isDefault())
				last = v;
			else if (v.volta.getNumbers().getStart() >= repeatTime)
				break;
		}
		if (last != null)
			return last;
		//otherwise use the last volta before the searched one
		for (val v : voltasStartMeasures) {
			if (v.volta.getNumbers().getStart() >= repeatTime)
				break;
			last = v;
		}
		//return first one
		return voltasStartMeasures.getFirst();
	}

	/**
	 * Returns the number of measures in the group.
	 */
	public int getMeasuresCount() {
		int length = 0;
		for (val v : voltasStartMeasures)
			length += v.volta.getLength();
		return length;
	}

	private int getStartMeasure(Volta volta) {
		for (val v : voltasStartMeasures)
			if (v.volta == volta)
				return v.startMeasure;
		throw new IllegalStateException("unknown volta");
	}

}
