package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.collections.IMap;
import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;
import lombok.Data;
import lombok.val;

/**
 * All {@link DynamicsPeriod}s in a score.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class DynamicsPeriods {

	public static final int noVoice = -1;

	/** A staff, any optionally a voice, within a {@link Repetition}. */
	@Const @Data
	public static class StaffPlayRange {
		/** The staff index. */
		public final int staff;
		/** The voice index within the staff, or {@link #noVoice} for the whole staff. */
		public final int voice;
		/** The repetition index. */
		public final int repetition;
	}

	private final IMap<StaffPlayRange, IList<DynamicsPeriod>> periods;


	/**
	 * Gets the {@link DynamicsPeriod} for the given position.
	 * The highest priority has a dynamic in the current voice. If there is none,
	 * the last dynamic of the part is searched. When there are multiple candidates
	 * on different staves, the current staff wins.
	 * @param mp          the position where to find the dynamic
	 * @param repetition  the index of the {@link Repetition}
	 * @param partStaves  the staves indices in the part
	 */
	@MaybeNull public DynamicsPeriod get(MP mp, int repetition, Range partStaves) {
		val time = mp.getTime();
		//first, find voice dynamics
		val voicePeriods = periods.get(new StaffPlayRange(mp.staff, mp.voice, repetition));
		val voicePeriod = getPeriod(voicePeriods, time);
		if (voicePeriod != null)
			return voicePeriod;
		//then, for all staves in the part, look for last period(s) - when there
		//is more than one at the same time, prefer the one at the own staff
		Time maxStartTime = time;
		DynamicsPeriod bestMatch = null;
		for (int iStaff : partStaves) {
			val staffPeriods = periods.get(new StaffPlayRange(mp.staff, noVoice, repetition));
			val staffPeriod = getPeriod(staffPeriods, time);
			int compare = staffPeriod.startTime.compareTo(maxStartTime);
			if (staffPeriod != null && compare > 0 || (compare >= 0 && iStaff == mp.staff)) {
				//new best match
				maxStartTime = staffPeriod.startTime;
				bestMatch = null;
			}
		}
		return bestMatch;
	}

	/**
	 * Gets the {@link DynamicsPeriod} from the given list,
	 * which includes the given time, or null if none is found.
	 */
	@MaybeNull private DynamicsPeriod getPeriod(@MaybeNull IList<DynamicsPeriod> periods, Time time) {
		if (periods == null)
			return null;
		for (val period : periods)
			if (period.contains(time))
				return period;
		return null;
	}

}
