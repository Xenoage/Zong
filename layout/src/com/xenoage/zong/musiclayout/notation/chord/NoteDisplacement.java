package com.xenoage.zong.musiclayout.notation.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;


/**
 * The vertical position, horizontal offset and suspension of a single
 * note within a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public class NoteDisplacement {

	public final int lp;
	public final float xIs;
	public final NoteSuspension suspension;

}
