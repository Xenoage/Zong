package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;

/**
 * The vertical position and and the horizontal offset (suspension)
 * of a note in a chord.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class NoteDisplacement {

	public int lp;
	public float offsetIs;
	public NoteSuspension suspension;

}
