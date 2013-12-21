package com.xenoage.zong.io.midi.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A MIDI sequence with additional information.
 * It has to be subclasses to support platform-specific sequence classes.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public abstract class MidiSequence {

	protected Object sequence;
	/** Track number of the metronome, or null if there is no metronome track */
	private Integer metronomeBeatTrackNumber;
	private MidiTime[] timePool;
	private long[] measureStartTicks;
	private int[] staffTracks;

}
