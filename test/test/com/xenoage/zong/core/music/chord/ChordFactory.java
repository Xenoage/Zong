package com.xenoage.zong.core.music.chord;

import static com.xenoage.utils.math.Fraction._1$8;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;


/**
 * Helper class for quickly creating chords for testing purposes.
 * 
 * @author Andreas Wenger
 */
public class ChordFactory {

	public static Chord chord(Pitch pitch, Fraction duration) {
		return new Chord(new Note(pitch), duration);
	}

	public static Chord chord(Pitch[] pitches, Fraction duration) {
		return new Chord(Note.notes(pitches), duration);
	}

	public static Chord graceChord(Pitch pitch) {
		return graceChord(pitch, _1$8);
	}
	
	public static Chord graceChord(Pitch pitch, Fraction displayedDuration) {
		Chord chord = new Chord(Note.notes(pitch), new Grace(true, displayedDuration));
		return chord;
	}

}
