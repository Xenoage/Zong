package com.xenoage.zong.musiclayout.notations.chord;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;

/**
 * This class stores the line positions of the notes of a chord.
 * The notes are sorted from bottom to top.
 * 
 * @author Andreas Wenger
 */
@Const public final class ChordLinePositions {

	private final int[] linePositions;


	/**
	 * Creates a new {@link ChordLinePositions} object from the
	 * given {@link Chord} and {@link MusicContext}.
	 */
	public ChordLinePositions(Chord chord, MusicContext context) {
		linePositions = new int[chord.getNotes().size()];
		for (int i = 0; i < chord.getNotes().size(); i++) {
			linePositions[i] = context.getLp(((Note) chord.getNotes().get(i)).getPitch());
		}
	}

	/**
	 * Creates a new {@link ChordLinePositions} object from the
	 * given line positions.
	 */
	public ChordLinePositions(int[] linePositions) {
		this.linePositions = linePositions;
		//check if sorted correctly
		for (int i = 0; i < linePositions.length - 1; i++) {
			if (this.linePositions[i] > this.linePositions[i + 1])
				throw new IllegalArgumentException("Line positions must be sorted from bottom to top.");
		}
	}

	/**
	 * Gets the number of notes in this chord.
	 */
	public int getNotesCount() {
		return linePositions.length;
	}

	/**
	 * Returns the line position with the given index.
	 */
	public int get(int noteIndex) {
		return linePositions[noteIndex];
	}

	/**
	 * Returns the line position of the bottommost note.
	 */
	public int getBottom() {
		return linePositions[0];
	}

	/**
	 * Returns the line position of the topmost note.
	 */
	public int getTop() {
		return linePositions[linePositions.length - 1];
	}

}
