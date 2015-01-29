package com.xenoage.zong.musiclayout.notations.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;

/**
 * The displacement of the accidentals of a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class AccidentalsDisplacement {
	
	public static final AccidentalsDisplacement empty = new AccidentalsDisplacement(new AccidentalDisplacement[0], 0);

	/** The accidentals of this chord.
	 *  The accidentals are sorted upwards, that means, the lowest accidental has index 0. */
	public final AccidentalDisplacement[] accidentals;
	/** The width of the accidentals of this chord. This is the distance between the left side of
	 * the leftmost accidental and the beginning of the notes. */
	public final float widthIs;

}
