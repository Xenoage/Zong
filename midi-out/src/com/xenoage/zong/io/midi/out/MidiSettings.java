package com.xenoage.zong.io.midi.out;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import lombok.Builder;
import lombok.Data;

import static com.xenoage.utils.math.Fraction._1;

/**
 * Settings for MIDI files in Zong!.
 * Later, this class may be using dynamically loaded midiSettings instead
 * of static values.
 * 
 * @author Andreas Wenger
 */
@Const @Data @Builder(builderClassName = "midiSettings")
public class MidiSettings {
	public static final MidiSettings defaultMidiSettings = new MidiSettings(
			0.7f, 8, 36, 50, Companion.get_1());
	
	/** Default volume. */
	public final float defaultVolume;

	/** Number of ticks for the shortest possible note.
	 * Even the shortest note should have at least 8 ticks by default.
	 * This allows staccato playback for example. */
	public final int resolutionFactor;
	
	/** MIDI note of a strong metronome beat. */
	public final int metronomeStrongBeatNote; //getSetting("MetronomeStrongBeat", "playback")
	
	/** MIDI note of a weak metronome beat. */
	public final int metronomeWeakBeatNote; //getSetting("MetronomeWeakBeat", "playback");

	/** Factor to multiply all note durations with. 1 means 100% note length. */
	public final Fraction durationFactor;

}
