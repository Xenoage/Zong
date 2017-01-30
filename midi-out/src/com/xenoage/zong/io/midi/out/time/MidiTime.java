package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.annotations.Const;
import lombok.Data;

/**
 * This class maps a MIDI tick to a {@link RepTime} and a
 * time position in ms.
 *
 * @author Andreas Wenger
 */
@Const @Data
public final class MidiTime {

	public final long tick;
	public final RepTime repTime;
	public final long ms;

}
