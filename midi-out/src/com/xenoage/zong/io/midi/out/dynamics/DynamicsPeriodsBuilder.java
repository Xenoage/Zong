package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.CMap;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsPeriods.StaffPlayRange;
import lombok.val;

import static com.xenoage.utils.collections.CMap.cmap;

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

	private final CMap<StaffPlayRange, IList<DynamicsPeriod>> periods = cmap();

	/**
	 * Adds a {@link DynamicsPeriod} in the given staff and repetition.
	 * This is used for dynamics which are defined in a measure, not in a specific voice.
	 */
	public DynamicsPeriodsBuilder addPeriodToStaff(
			DynamicsPeriod period, int staff, int repetition) {
		//GOON
		return this;
	}

	/**
	 * Adds a {@link DynamicsPeriod} in the given staff, voice and repetition.
	 * This is used for dynamics which are defined in a specific voice, not in its parent measure.
	 */
	public DynamicsPeriodsBuilder addPeriodToVoice(
			DynamicsPeriod period, int staff, int voice, int repetition) {
		//GOON
		return this;
	}

	public DynamicsPeriods build() {
		for (val p : periods.values())
			((CList) p).close();
		return new DynamicsPeriods(periods.close());
	}

}
