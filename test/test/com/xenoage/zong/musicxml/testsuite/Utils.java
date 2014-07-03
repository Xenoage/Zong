package com.xenoage.zong.musicxml.testsuite;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Grace;
import com.xenoage.zong.core.music.chord.Note;

/**
 * Useful methods for the test suite.
 * 
 * @author Andreas Wenger
 */
public class Utils {
	
	/**
	 * Creates a <b>ch</b>ord.
	 */
	public static Chord ch(Fraction duration, Pitch... pitches) {
		ArrayList<Note> notes = alist();
		for (Pitch pitch : pitches)
			notes.add(new Note(pitch));
		return new Chord(notes, duration);
	}
	
	/**
	 * Creates a <b>gr</b>ace chord.
	 */
	public static Chord gr(Fraction graceDuration, boolean slash, Pitch... pitches) {
		Grace grace = new Grace(slash, graceDuration);
		ArrayList<Note> notes = alist();
		for (Pitch pitch : pitches)
			notes.add(new Note(pitch));
		return new Chord(notes, grace);
	} 

}
