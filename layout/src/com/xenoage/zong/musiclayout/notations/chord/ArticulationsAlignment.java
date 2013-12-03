package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;

/**
 * This class computes and stores
 * the alignment of the articulations of a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter public final class ArticulationsAlignment {

	/** The positions of the articulations. */
	public final ArticulationAlignment[] articulations;
	/** The total height of the articulations in IS,
	 * including the space between the outer chord and the first articulation. */
	public final float totalHeightIS;

}
