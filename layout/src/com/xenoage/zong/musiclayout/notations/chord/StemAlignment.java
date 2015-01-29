package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;

/**
 * Vertical position of a chord stem.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class StemAlignment {
	
	public static final StemAlignment none = new StemAlignment(0, 0);

	/** The line position where the stem stars (note side). */
	public final float startLp;
	/** The line position where the stem ends (flag/beam side). */
	public final float endLp;

}
