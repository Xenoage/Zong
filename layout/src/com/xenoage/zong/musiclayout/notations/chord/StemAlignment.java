package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;

/**
 * Vertical position of a chord stem.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class StemAlignment {

	/** The line position where the stem stars (note side). */
	public float startLp;
	/** The line position where the stem ends (flag/beam side). */
	public float endLp;

}
