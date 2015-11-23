package com.xenoage.zong.musiclayout.notation.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;


/**
 * Vertical position of a chord stem.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public class StemNotation {
	
	public static final StemNotation none = new StemNotation(0, 0);

	/** The line position where the stem stars (note side). */
	public final float startLp;
	/** The line position where the stem ends (flag/beam side). */
	public final float endLp;

}
