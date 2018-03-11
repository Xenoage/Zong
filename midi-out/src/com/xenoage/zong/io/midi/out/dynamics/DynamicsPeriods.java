package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static com.xenoage.utils.collections.CollectionUtils.getOrNull;
import static com.xenoage.utils.kernel.Range.range;

/**
 * All {@link DynamicsPeriod}s in a score.
 *
 * @author Andreas Wenger
 */
@Const @RequiredArgsConstructor
public class DynamicsPeriods {

	/**
	 * The periods, sorted by staff, voice and repetition.
	 * First index: staff. Second index: voice + 1, or 0 = no voice (staff). Third index: repetition.
	 */
	private final IList<IList<IList<IList<DynamicsPeriod>>>> periods;


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
		val voicePeriods = getVoicePeriods(mp.getStaff(), mp.getVoice(), repetition);
		val voicePeriod = getPeriod(voicePeriods, time);
		if (voicePeriod != null)
			return voicePeriod;
		//then, for all staves in the part, look for last period(s) - when there
		//is more than one at the same time, prefer the one at the own staff
		Time maxStartTime = time;
		DynamicsPeriod bestMatch = null;
		for (int iStaff : partStaves) {
			val staffPeriods = getStaffPeriods(mp.getStaff(), repetition);
			val staffPeriod = getPeriod(staffPeriods, time);
			int compare = staffPeriod.startTime.compareTo(maxStartTime);
			if (staffPeriod != null && compare > 0 || (compare >= 0 && iStaff == mp.getStaff())) {
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

	@MaybeNull private IList<DynamicsPeriod> getStaffPeriods(int staff, int repetition) {
		return getOrNull(getOrNull(getOrNull(periods, staff), 0), repetition);
	}

	@MaybeNull private IList<DynamicsPeriod> getVoicePeriods(int staff, int voice, int repetition) {
		return getOrNull(getOrNull(getOrNull(periods, staff), voice + 1), repetition);
	}

	@Override public String toString() {
		String s = "";
		for (int iStaff : range(periods)) {
			val staffList = getOrNull(periods, iStaff);
			if (staffList != null) {
				s += "staff " + iStaff + ":\n";
				for (int iVoice : range(staffList)) {
					val voiceList = getOrNull(staffList, iVoice);
					if (voiceList != null) {
						s += (iVoice == 0 ? "  no voice:" : "  voice " + iVoice + ":") + "\n";
						for (int iRep : range(voiceList)) {
							val repList = getOrNull(voiceList, iRep);
							if (repList != null) {
								s += "    rep " + iRep + ":     " + repList.toString() + "\n";
							}
						}
					}
				}
			}
		}
		return s;
	}

}
