package com.xenoage.zong.io.midi.out;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.position.MP;

/**
 * A MIDI sequence with additional information.
 * 
 * @param <T> the platform-specific sequence class
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class MidiSequence<T> {

	/** The sequence data. */
	@NonNull protected T sequence;
	/** Index of the metronome track, or null if there is no metronome track */
	@MaybeNull protected Integer metronomeTrack;
	/** Mappings of MIDI ticks to {@link MP}s and milliseconds. */
	protected List<MidiTime> timePool;
	/** The start ticks of each measure (including repetitions). */
	protected List<Long> measureStartTicks;
	
}
