package com.xenoage.zong.musiclayout.notations.chord;

import com.xenoage.utils.annotations.Const;

import lombok.AllArgsConstructor;

/**
 * The vertical position and and the horizontal offset (suspension)
 * of a note in a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class NoteDisplacement {

	public final int lp;
	public final float offsetIs;
	public final NoteSuspension suspension;

}
