package com.xenoage.zong.musiclayout.notation.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Accidental;

/**
 * The vertical position and horizontal offset of a single
 * accidental within a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public class AccidentalDisplacement {

	/** The vertical position of the accidental as a LP. */
	public final int yLp;
	
	/** The horizontal offset of the accidental in IS.
	 * The leftmost accidental usually has a value of 0. */
	public final float xIs;
	
	/** The accidental. */
	public final Accidental accidental;

}
