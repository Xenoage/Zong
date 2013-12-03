package com.xenoage.zong.core.music.tuplet;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.chord.Chord;


/**
 * Information about a tuplet (e.g. a triplet).
 * 
 * This class is inspired by MusicXML's time-modification element.
 * 
 * For example, to represent a triplet containing a quarter note
 * and an eighth note, that has the total duration of two eighth notes,
 * <code>baseDuration</code> is 1/8, <code>actualNotes</code> is 3
 * (because there is space for three eighth notes within the tuplet)
 * and <code>normalNotes</code> is 2 (because the duration of
 * the tuplet is two eighth notes).
 * 
 * This class also stores the references of its chords.
 * 
 * @author Andreas Wenger
 */
public final class Tuplet {

	/** Tuplet timing information. See class documentation. */
	@Getter @Setter private int actualNotes, normalNotes;
	/** Tuplet timing information. See class documentation. */
	@Getter @Setter @NonNull private Fraction baseDuration;

	/** True, iff the bracket shown above or below the tuplet is visible. */
	@Getter @Setter private boolean isBracketVisible = true;

	/** Back reference: The chords, containing their real duration, e.g. 1/12 for an eight triplet chord. */
	@Getter @Setter private List<Chord> chords;


	public Tuplet(int actualNotes, int normalNotes, Fraction baseDuration) {
		if (chords.size() < 1)
			throw new IllegalArgumentException("Tuplet must contain at least one chord");
		this.actualNotes = actualNotes;
		this.normalNotes = normalNotes;
		this.baseDuration = baseDuration;
	}


	/**
	 * Replaces the given old chord with the given new one.
	 */
	public void replaceChord(Chord oldChord, Chord newChord) {
		int index = chords.indexOf(oldChord);
		if (index == -1)
			throw new IllegalArgumentException("Given chord is not part of this tuplet");
		chords.set(index, newChord);
	}
	
	/**
	 * Gets the first chord.
	 * @return
	 */
	public Chord getFirstChord() {
		return chords.get(0);
	}
	
	/**
	 * Gets the last chord.
	 * @return
	 */
	public Chord getLastChord() {
		return chords.get(chords.size() - 1);
	}

}
