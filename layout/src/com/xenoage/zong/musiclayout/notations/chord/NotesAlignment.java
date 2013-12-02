package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;

/**
 * This class represents the alignment of the notes and the dots of a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter public final class NotesAlignment {

	/** The width of the chord notes and dots in spaces. */
	public final float width;
	/** The list of note alignments.
	 * The notes are sorted upwards, that means, the lowest note has index 0. */
	public final NoteAlignment[] noteAlignments;
	/** The offsets of the first and second column of dots (if any). */
	public final float[] dotsOffsets;
	/** The line positions of the dots, or an empty array, if the chord has no dots. */
	public final int[] dotLPs;
	/** The horizontal offset of the stem in spaces. */
	public final float stemOffset;
	/** The width of the left suspended notes in IS.
	 * If there are no left-suspended notes, this will be 0. */
	public final float leftSuspendedWidth;
	
	
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
		return dotsOffsets.length;
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
	public float getDotsOffset(int dot) {
		return dotsOffsets[dot];
	}

	/**
	 * Gets the line positions of the chord (convenience method).
	 */
	public ChordLinePositions getLinePositions() {
		int[] ret = new int[noteAlignments.length];
		for (int i = 0; i < ret.length; i++)
			ret[i] = noteAlignments[i].linePosition;
		return new ChordLinePositions(ret);
	}

}
