package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.midi.out.score.MeasureBeats;
import com.xenoage.zong.io.midi.out.score.PartStaves;
import lombok.Data;
import lombok.val;

/**
 * Provides the dynamics for each ({@link MP}) in the score.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class Dynamics {

	public static final DynamicValue defaultDynamics = DynamicValue.mf;


	//dynamics in the score and their interpretations
	private final DynamicsPeriods periods;
	private final DynamicsInterpretation interpretation;

	//additional information on the score
	private final MeasureBeats measureBeats;
	private final PartStaves partStaves;


	/**
	 * Gets the volume (0 = silent, 1 = full) at the given {@link MP} and repetition,
	 * or the volume of {@link #defaultDynamics}, if undefined.
	 */
	public float getVolumeAt(MP mp, int repetition) {
		int part = partStaves.getPartByStaff(mp.staff);
		val staves = partStaves.getStaves(part);
		val period = periods.get(mp, repetition, staves);
		if (period != null)
			return period.getVolumeAt(mp.getTime(), interpretation, measureBeats);
		else
			return interpretation.getVolume(defaultDynamics);
	}

}
