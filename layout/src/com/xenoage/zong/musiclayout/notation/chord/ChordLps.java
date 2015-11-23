package com.xenoage.zong.musiclayout.notation.chord;

import static com.xenoage.utils.kernel.Range.range;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.chord.Chord;

/**
 * This class stores the line positions of the notes of a chord.
 * The notes are sorted from bottom to top.
 * 
 * @author Andreas Wenger
 */
@Const
public final class ChordLps {

	private final int[] lps;


	/**
	 * Creates a new {@link ChordLps} object from the
	 * given {@link Chord} and {@link MusicContext}.
	 */
	public ChordLps(Chord chord, MusicContext context) {
		lps = new int[chord.getNotes().size()];
		for (int i : range(lps))
			lps[i] = context.getLp(chord.getNotes().get(i).getPitch());
	}

	/**
	 * Creates a new {@link ChordLps} object from the
	 * given line positions.
	 */
	public ChordLps(int... lps) {
		this.lps = lps;
		//check if sorted correctly
		for (int i = 0; i < lps.length - 1; i++) {
			if (this.lps[i] > this.lps[i + 1])
				throw new IllegalArgumentException("Line positions must be sorted from bottom to top.");
		}
	}

	/**
	 * Gets the number of notes in this chord.
	 */
	public int getNotesCount() {
		return lps.length;
	}

	/**
	 * Returns the line position with the given index.
	 */
	public int get(int noteIndex) {
		return lps[noteIndex];
	}

	/**
	 * Returns the line position of the bottommost note.
	 */
	public int getBottom() {
		return lps[0];
	}

	/**
	 * Returns the line position of the topmost note.
	 */
	public int getTop() {
		return lps[lps.length - 1];
	}

}
