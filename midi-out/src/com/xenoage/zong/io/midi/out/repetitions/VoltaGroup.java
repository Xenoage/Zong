package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.music.volta.Volta;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.List;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.kernel.Range.range;

/**
 * A group of consecutive {@link Volta}s with their start measure indices.
 *
 * See {@link Volta} for more details and ordering rules.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class VoltaGroup {

	//the voltas and their start measure indices.
	private IList<Tuple2<Volta, Integer>> voltasStartMeasures;


	/**
	 * Returns the measure numbers of the measures that
	 * have to be played in the volta, when the playback reaches the
	 * volta block the given time (1-based), or null if no appropriate volta
	 * can be found.
	 */
	private Range getRange(int repeatTime) {
		val volta = findVolta(repeatTime);
		if (volta != null) {
			int startMeasure = getStartMeasure(volta);
			return range(startMeasure, startMeasure + volta.getLength() - 1);
		}
		else {
			return null;
		}
	}

	/**
	 * Finds the {@link Volta} for the given repeat time (1-based).
	 * If there is no explicit one, the default volta is returned.
	 * If there is no default volta, null is returned.
	 */
	private Volta findVolta(int repeatTime) {
		//find explicit volta
		for (Volta volta : getVoltas())
			if (false == volta.isDefault() && volta.getNumbers().isInRange(repeatTime))
				return volta;
		//find default volta
		for (Volta volta : getVoltas())
			if (volta.isDefault())
				return volta;
		//no volta matching at all
		return null;
	}

	/**
	 * Returns whether this time is the last time to repeat.
	 * @deprecated unneeded
	 */
	private boolean isLastTime(int repeatTime) {
		if (repeatTime == getRepeatCount()) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the number of repetitions.
	 * See {@link Volta} for the playback rules.
	 */
	private int getRepeatCount() {
		//find maximum repeat number (+1, if the last volta is a default volta)
		int maxRepeatTime = 0;
		for ()
		//when there are gaps, skip this repeat
		int repeatCount = 0;
		for (int range : range(1, maxRepeatTime))
			if (existsRepeatTime(maxRepeatTime))
				repeatCount++;
		return repeatCount;
	}

	/**
	 * Returns the number of measures in the group.
	 */
	public int getMeasuresCount() {
		int length = 0;
		for (Volta volta : getVoltas())
			length += volta.getLength();
		return length;
	}

	private Iterable<Volta> getVoltas() {
		return voltasStartMeasures.keySet();
	}

	private int getStartMeasure(Volta volta) {
		for (Volta volt)
		return voltasStartMeasures.get(volta);
	}

}
