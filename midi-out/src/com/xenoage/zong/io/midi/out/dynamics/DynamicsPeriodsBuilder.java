package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.CMap;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsPeriods.StaffRepetition;
import lombok.val;

import static com.xenoage.utils.collections.CMap.cmap;
import static com.xenoage.zong.io.midi.out.dynamics.DynamicsPeriods.StaffRepetition.noVoice;

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

	private final CMap<StaffRepetition, IList<DynamicsPeriod>> periods = cmap();

	/**
	 * Adds a {@link DynamicsPeriod} in the given staff and repetition.
	 * This is used for dynamics which are defined in a measure, not in a specific voice.
	 */
	public DynamicsPeriodsBuilder addPeriodToStaff(
			DynamicsPeriod period, int staff, int repetition) {
		addPeriod(period, new StaffRepetition(staff, noVoice, repetition));
		return this;
	}

	/**
	 * Adds a {@link DynamicsPeriod} in the given staff, voice and repetition.
	 * This is used for dynamics which are defined in a specific voice, not in its parent measure.
	 */
	public DynamicsPeriodsBuilder addPeriodToVoice(
			DynamicsPeriod period, int staff, int voice, int repetition) {
		addPeriod(period, new StaffRepetition(staff, voice, repetition));
		return this;
	}

	public DynamicsPeriods build() {
		for (val p : periods.values())
			((CList) p).close();
		return new DynamicsPeriods(periods.close());
	}

	/**
	 * Adds the given period at the given staff, maybe voice, and repetition.
	 */
	private void addPeriod(DynamicsPeriod period, StaffRepetition staffRepetition) {
		if (false == periods.containsKey(staffRepetition))
			periods.put(staffRepetition, new CList<DynamicsPeriod>());
		val list = (CList<DynamicsPeriod>) periods.get(staffRepetition);
		//add at correct position
		int pos = 0;
		while (pos < list.size() && list.get(pos).startTime.compareTo(period.startTime) < 0)
			pos++;
		//add at this position, but only if the following (if any) period does not overlap
		if (pos + 1 < list.size() && list.get(pos + 1).startTime.compareTo(period.endTime) < 0)
			throw new IllegalArgumentException("periods overlap");
		list.add(period);
	}

}
