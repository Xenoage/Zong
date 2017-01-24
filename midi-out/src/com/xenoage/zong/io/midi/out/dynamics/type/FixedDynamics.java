package com.xenoage.zong.io.midi.out.dynamics.type;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsInterpretation;
import lombok.Data;

/**
 * A volume.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class FixedDynamics
	implements DynamicsVolume {

	public final DynamicsType dynamics;

	@Override public float getVolumeAt(float progress, DynamicsInterpretation interpretation) {
		return interpretation.getVolume(dynamics);
	}

}
