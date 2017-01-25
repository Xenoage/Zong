package com.xenoage.zong.io.midi.out.dynamics.type;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsInterpretation;
import lombok.Data;

/**
 * A single dynamic value, constant over time.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class FixedDynamics
	implements DynamicsType {

	public final DynamicValue value;

	@Override public DynamicValue getEndValue() {
		return value;
	}

	@Override public float getVolumeAt(float progress, DynamicsInterpretation interpretation) {
		return interpretation.getVolume(value);
	}

	@Override public String toString() {
		return ""+value;
	}

}
