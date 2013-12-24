package com.xenoage.zong.io.midi.out;

import lombok.Getter;

import com.xenoage.zong.core.position.MP;

/**
 * Settings for MIDI files in Zong!.
 * Later, this class may be using dynamically loaded settings instead
 * of static values.
 * 
 * @author Andreas Wenger
 */
public class MidiSettings {
	
	/** Default volume. */
	@Getter private static final float defaultVolume = 0.7f;

	/** Tick resolution factor. Even the shortest note has {@value #resolutionFactor} ticks.
	 * this allows staccato playback for example. */
	@Getter private static final int resolutionFactor = 8;

}
