package com.xenoage.zong.io.midi.out.dynamics.type;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsInterpretation;
import lombok.Data;

/**
 * A crescendo or decrescendo between two dynamic values.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class GradientDynamics
	implements DynamicsType {

	/** The beginning dynamics of the gradient. */
	public final DynamicValue start;

	/** The ending dynamics of the gradient. */
	public final DynamicValue end;


	@Override public DynamicValue getEndValue() {
		return end;
	}

	@Override public float getVolumeAt(float progress, DynamicsInterpretation interpretation) {
		float startVolume = interpretation.getVolume(start);
		float endVolume = interpretation.getVolume(end);
		return startVolume + (endVolume - startVolume) * progress;
	}

	@Override public String toString() {
		return start + "➔" + end;
	}

}
