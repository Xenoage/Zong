package com.xenoage.zong.io.midi.out;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.io.midi.out.time.TimeMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

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
	/** Mapping between MIDI and score time. */
	@NonNull protected TimeMap timeMap;
	
}
