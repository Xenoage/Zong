package com.xenoage.zong.musiclayout.notation.chord;

import static java.lang.Math.abs;

import lombok.AllArgsConstructor;


/**
 * Vertical position of a chord stem.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class StemNotation {

	/** The line position where the stem stars (note side). */
	public float startLp;
	/** The line position where the stem ends (flag/beam side). */
	public float endLp;

	
	public float getLengthIs() {
		return abs(endLp - startLp) / 2;
	}
	
}
