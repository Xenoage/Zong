package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;

/**
 * This class represents the alignment of the notes and the dots of a chord.
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
@Const @AllArgsConstructor @Getter public final class NotesAlignment {

	/** The width of the chord notes and dots in IS. */
	public float widthIs;
	/** The width of a notehead in the chord in IS. */
	public float noteheadWidthIs;
	/** The list of note alignments.
	 * The notes are sorted upwards, that means, the lowest note has index 0. */
	public NoteAlignment[] noteAlignments;
	/** The offsets of the first and second column of dots (if any). */
	public float[] dotsOffsetsIs;
	/** The line positions of the dots, or an empty array, if the chord has no dots. */
	public int[] dotsLp;
	/** The horizontal offset of the stem in IS. */
	public float stemOffsetIs;
	/** True, if there are no left-suspended notes, otherwise false. */
	public boolean leftSuspended;
	
	
	/**
	 * Gets the number of notes.
	 */
	public int getNotesCount() {
		return noteAlignments.length;
	}

	/**
	 * Gets the alignment of the note with the given index.
	 * The notes are sorted upwards, that means, the lowest
	 * note has index 0.
	 */
	public NoteAlignment getNoteAlignment(int index) {
		return noteAlignments[index];
	}

	/**
	 * Gets the number of dots per note.
	 */
	public int getDotsPerNoteCount() {
		return dotsOffsetsIs.length;
	}

	/**
	 * Gets the alignment of the top note of the chord.
	 */
	public NoteAlignment getTopNoteAlignment() {
		return noteAlignments[noteAlignments.length - 1];
	}

	/**
	 * Gets the alignment of the bottom note of the chord.
	 */
	public NoteAlignment getBottomNoteAlignment() {
		return noteAlignments[0];
	}

	/**
	 * Gets the offset of the dots with the given index (0 or 1).
	 */
	public float getDotsOffsetIs(int dot) {
		return dotsOffsetsIs[dot];
	}

	/**
	 * Gets the line positions of the chord (convenience method).
	 */
	public ChordLinePositions getLinePositions() {
		int[] ret = new int[noteAlignments.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = noteAlignments[i].lp;
		return new ChordLinePositions(ret);
	}

}
