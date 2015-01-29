package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Accidental;

/**
 * The vertical position, horizontal offset and type of a single
 * {@link Accidental} within a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class AccidentalDisplacement {

	public final int lp;
	public final float offsetIs;
	public final Accidental type;

}
