package com.xenoage.zong.io.midi.out;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.position.Time;
import lombok.AllArgsConstructor;

/**
 * This class maps a MIDI tick to a {@link Time} and a
 * time position in ms.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public class MidiTime {

	public final long tick;
	public final Time time;
	public final long ms;

}
