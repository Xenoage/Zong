package com.xenoage.zong.musiclayout.notations.chord;

import com.xenoage.utils.annotations.Const;

import lombok.AllArgsConstructor;

/**
 * The displacement of the notes, the stem and the dots of a chord.
 * 
 * The horizontal positions are measured in IS.
 * The position x = 0 is at the left side of the normal, unsuspended
 * chord notes. Thus, for example, the stem offset is 0 if the stem is on the
 * left side, or it is the notehead width if it is on the right side.
 * 
 * The vertical positions are measured in LPs.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class NotesNotation {

	/** The width of the chord notes and dots. */
	public final float widthIs;
	/** The width of a notehead in the chord. */
	public final float noteheadWidthIs;
	/** The displacement of the notes.
	 * The notes are sorted upwards, that means, the lowest note has index 0. */
	public final NoteDisplacement[] notes;
	/** The offsets of the first and second column of dots (if any). */
	public final float[] dotsOffsetsIs;
	/** The line positions of the dots, or an empty array, if the chord has no dots. */
	public final int[] dotsLp;
	/** The horizontal offset of the stem. */
	public final float stemOffsetIs;
	/** True, if there are left-suspended notes, otherwise false. */
	public final boolean leftSuspended;
	
	
	/**
	 * Gets the number of notes.
	 */
	public int getNotesCount() {
		return notes.length;
	}

	/**
	 * Gets the displacement of the note with the given index.
	 * The notes are sorted upwards, that means, the lowest
	 * note has index 0.
	 */
	public NoteDisplacement getNote(int index) {
		return notes[index];
	}

	/**
	 * Gets the number of dots per note.
	 */
	public int getDotsPerNoteCount() {
		return dotsOffsetsIs.length;
	}

	/**
	 * Gets the displacement of the top note of the chord.
	 */
	public NoteDisplacement getTopNote() {
		return notes[notes.length - 1];
	}

	/**
	 * Gets the displacement of the bottom note of the chord.
	 */
	public NoteDisplacement getBottomNote() {
		return notes[0];
	}

	/**
	 * Gets the offset of the dots with the given index (0 or 1).
	 */
	public float getDotsOffsetIs(int dot) {
		return dotsOffsetsIs[dot];
	}

	/**
	 * Gets the LPs of the chord (convenience method).
	 */
	public ChordLps getLps() {
		int[] ret = new int[notes.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = notes[i].lp;
		return new ChordLps(ret);
	}

}
