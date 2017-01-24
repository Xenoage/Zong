package com.xenoage.zong.io.midi.out.dynamics.type;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsInterpretation;
import lombok.Data;

/**
 * A crescendo or decrescendo between two dynamic values.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class GradientDynamics
	implements Dynamics {

	/** The beginning dynamics of the gradient. */
	public final DynamicsType start;

	/** The ending dynamics of the gradient. */
	public final DynamicsType end;


	@Override public float getVolumeAt(float progress, DynamicsInterpretation interpretation) {
		float startVolume = interpretation.getVolume(start);
		float endVolume = interpretation.getVolume(end);
		return startVolume + (endVolume - startVolume) * progress;
	}

}
