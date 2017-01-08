package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.volta.Volta;
import lombok.AllArgsConstructor;

/**
 * Information about a playback state within a {@link VoltaGroup}.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public class VoltaGroupState {

	/** The volta group over the current measure. */
	public final VoltaGroup group;
	/** The volta over the current measure. */
	public final Volta volta;
	/** Index of the measure where the volta over the current measure starts. */
	public final int voltaStartMeasure;
	/** Index of the measure where the volta over the current measure ends (inclusive). */
	public final int voltaEndMeasure;
}
