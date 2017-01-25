package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.getOrNull;
import static com.xenoage.utils.collections.CollectionUtils.setExtend;

/**
 * Builder for {@link DynamicsPeriods}.
 *
 * {@link DynamicsPeriod}s can be added to play ranges and a staff
 * and optionally a voice, but they may not overlap within their
 * tracks.
 *
 * @author Andreas Wenger
 */
public class DynamicsPeriodsBuilder {

	private final CList<IList<IList<IList<DynamicsPeriod>>>> periods = clist();

	/**
	 * Adds a {@link DynamicsPeriod} in the given staff and repetition.
	 * This is used for dynamics which are defined in a measure, not in a specific voice.
	 */
	public DynamicsPeriodsBuilder addPeriodToStaff(
			DynamicsPeriod period, int staff, int repetition) {
		addPeriod(period, (CList<DynamicsPeriod>) getStaffPeriods(staff, repetition));
		return this;
	}

	/**
	 * Adds a {@link DynamicsPeriod} in the given staff, voice and repetition.
	 * This is used for dynamics which are defined in a specific voice, not in its parent measure.
	 */
	public DynamicsPeriodsBuilder addPeriodToVoice(
			DynamicsPeriod period, int staff, int voice, int repetition) {
		addPeriod(period, (CList<DynamicsPeriod>) getVoicePeriods(staff, voice, repetition));
		return this;
	}

	public DynamicsPeriods build() {
		return new DynamicsPeriods(periods.closeDeep());
	}

	/**
	 * Gets the periods track for the given staff and repetition.
	 */
	private IList<DynamicsPeriod> getStaffPeriods(int staff, int repetition) {
		return getVoicePeriods(staff, -1, repetition);
	}

	/**
	 * Gets the periods track for the given staff, voice and repetition.
	 */
	private IList<DynamicsPeriod> getVoicePeriods(int staff, int voice, int repetition) {
		//get or create staff
		IList<IList<IList<DynamicsPeriod>>> staffList = getOrNull(periods, staff);
		if (staffList == null) {
			staffList = clist();
			setExtend(periods, staff, staffList, null);
		}
		//get or create voice (0 = staff; 1..n = voice 0..(n-1))
		IList<IList<DynamicsPeriod>> voiceList = getOrNull(staffList, voice + 1);
		if (voiceList == null) {
			voiceList = clist();
			setExtend(staffList, voice + 1, voiceList, null);
		}
		//get or create repetition
		IList<DynamicsPeriod> repetitionList = getOrNull(voiceList, repetition);
		if (repetitionList == null) {
			repetitionList = clist();
			setExtend(voiceList, repetition, repetitionList, null);
		}
		return repetitionList;
	}

	/**
	 * Adds the given period into the given list. When there are collisions,
	 * an {@link IllegalArgumentException} is thrown.
	 */
	private void addPeriod(DynamicsPeriod period, CList<DynamicsPeriod> target) {
		//add at correct position
		int pos = 0;
		while (pos < target.size() && target.get(pos).startTime.compareTo(period.startTime) < 0)
			pos++;
		//add at this position, but only if the following (if any) period does not overlap
		if (pos + 1 < target.size() && target.get(pos + 1).startTime.compareTo(period.endTime) < 0)
			throw new IllegalArgumentException("periods overlap");
		target.add(pos, period);
	}

}
