package com.xenoage.zong.io.midi.out.dynamics.type;

import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.io.midi.out.dynamics.DynamicsInterpretation;

/**
 * Fixed or gradient dynamics.
 *
 * @author Andreas Wenger
 */
public interface DynamicsType {

	/**
	 * Gets the dynamic at the end of the time in which it is played.
	 */
	DynamicValue getEndValue();

	/**
	 * Gets the volume at the given time (0 = start of period; 1 = end of period)
	 * as a value between 0 (silence) and 1 (maximum),
	 * using the given {@link DynamicsInterpretation}.
	 */
	float getVolumeAt(float progress, DynamicsInterpretation interpretation);

}
