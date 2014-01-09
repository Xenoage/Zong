package com.xenoage.zong.io.midi.out;

import lombok.Getter;

/**
 * Settings for MIDI files in Zong!.
 * Later, this class may be using dynamically loaded settings instead
 * of static values.
 * 
 * @author Andreas Wenger
 */
@Getter public class MidiSettings {
	
	/** Default volume. */
	private float defaultVolume = 0.7f;

	/** Tick resolution factor. Even the shortest note has {@value #resolutionFactor} ticks.
	 * this allows staccato playback for example. */
	private int resolutionFactor = 8;
	
	/** MIDI note of a strong metronome beat. */
	private int metronomeStrongBeatNote = 36; //getSetting("MetronomeStrongBeat", "playback")
	
	/** MIDI note of a weak metronome beat. */
	private int metronomeWeakBeatNote = 50; //getSetting("MetronomeWeakBeat", "playback");

}
