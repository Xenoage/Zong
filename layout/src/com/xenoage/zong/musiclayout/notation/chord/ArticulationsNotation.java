package com.xenoage.zong.musiclayout.notation.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;

/**
 * This class computes and stores
 * the alignment of the articulations of a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class ArticulationsNotation {
	
	public static final ArticulationsNotation empty = new ArticulationsNotation(
		new ArticulationDisplacement[0], 0);

	/** The articulations of this chord.
	 *  The articulations are sorted upwards, that means, the lowest accidental has index 0. */
	public final ArticulationDisplacement[] articulations;
	/** The total height of the articulations in IS,
	 * including the space between the outer chord and the first articulation. */
	public final float heightIs;

}
