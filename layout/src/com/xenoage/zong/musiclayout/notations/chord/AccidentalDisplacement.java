package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.chord.Accidental;

/**
 * The vertical position, horizontal offset and type of a single
 * {@link Accidental} within a chord.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class AccidentalDisplacement {

	public int lp;
	public float offsetIs;
	public Accidental type;

}
