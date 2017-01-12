package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.io.midi.out.repetitions.VoltaGroup.VoltaStartMeasure;
import lombok.AllArgsConstructor;
import lombok.val;

/**
 * The {@link VoltaGroup}s in a score.
 */
@AllArgsConstructor
public class VoltaGroups {

	final IList<VoltaGroup> voltaGroups;

	/**
	 * Gets information about the volta group over the given measure.
	 */
	@MaybeNull public VoltaGroupState getStateAt(int measure) {
		val group = getVoltaGroupAt(measure);
		val volta = getVoltaAt(measure);
		if (volta == null)
			return null;
		return new VoltaGroupState(group, volta.volta, volta.startMeasure,
				volta.startMeasure + volta.volta.getLength() - 1);
	}

	/**
	 * Gets the {@link Volta} and its start measure over the given measure.
	 */
	@MaybeNull public VoltaStartMeasure getVoltaAt(int measure) {
		val group = getVoltaGroupAt(measure);
		if (group == null)
			return null;
		VoltaStartMeasure volta = null;
		for (val v : group.voltasStartMeasures) {
			volta = v;
			if (measure < v.startMeasure + v.volta.getLength())
				break;
		}
		return volta;
	}

	/**
	 * Gets the {@link VoltaGroup} over the given measure.
	 */
	@MaybeNull public VoltaGroup getVoltaGroupAt(int measure) {
		for (val group : voltaGroups)
			if (group.startMeasure <= measure && measure <= group.endMeasure)
				return group;
		return null;
	}

	/**
	 * Gets the {@link VoltaGroup} starting at the given measure.
	 */
	@MaybeNull public VoltaGroup getVoltaGroupStartingAt(int measure) {
		for (val group : voltaGroups)
			if (group.startMeasure == measure)
				return group;
		return null;
	}

}
