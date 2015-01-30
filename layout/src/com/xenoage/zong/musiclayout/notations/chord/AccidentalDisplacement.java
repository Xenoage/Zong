package com.xenoage.zong.musiclayout.notations.chord;

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

	public final int yLp;
	public final float xIs;
	public final Accidental accidental;

}
