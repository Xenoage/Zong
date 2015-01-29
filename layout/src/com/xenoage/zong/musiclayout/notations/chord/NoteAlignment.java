package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;

/**
 * The alignment of a single note within a chord: its vertical position and its
 * horizontal offset and suspension.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class NoteAlignment {

	/** The vertical position of the note in half-space steps,
	 * beginning at the bottom line. Some examples:
	 * <ul>
	 *   <li>0: note is on the bottom line</li>
	 *   <li>-2: note is on the first lower leger line</li>
	 *   <li>5: note is between the 3rd and 4th line from below</li>
	 * </ul> */
	public int lp;
	/** The horizontal offset of the note in IS. */
	public float offsetIs;
	/** The suspension of the note, that is left or right side of the stem, or none if the
	 * notes are aligned on the default side of the stem. */
	public NoteSuspension suspension;


	/**
	 * Creates a new {@link NoteAlignment} for an unsuspended note.
	 */
	public NoteAlignment(int lp, float offsetIs) {
		this(lp, offsetIs, NoteSuspension.None);
	}

}
