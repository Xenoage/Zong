package com.xenoage.zong.musiclayout.notator.beam.lines;

import com.xenoage.zong.musiclayout.notation.BeamNotation;

/**
 * Beam rules, dependent on the number of beam lines.
 * 
 * @author Andreas Wenger
 */
public abstract class BeamRules {

	/**
	 * Gets the maximum number of beam lines, e.g. 1 for a beam
	 * which contains at maximum 8th notes, 2 for 16th notes and so on.
	 */
	public abstract int getBeamLinesCount();
	
	/**
	 * Gets the minimum absolute length of a stem in IS,
	 * from the innermost note at the stem side up to
	 * the end of the stem.
	 */
	public abstract float getMinimumStemLengthIs();

	/**
	 * Returns the vertical distance between two beam lines in IS, that is
	 * the "white space" between two beam lines.
	 * Normally this is {@link BeamNotation#normalGapIs}, but it can be up to
	 * {@link BeamNotation#largeGapIs} for a beam with more than three lines.
	 */
	public abstract float getGapIs();
	
	/**
	 * Like {@link #getGapIs()}, but for beams with four or more lines
	 * at the special case when only up to three beam lines fall inside the staff.
	 * See Ross, p. 126, at the top.
	 */
	public float getGapOutsideStaffIs() {
		return getGapIs();
	}
	
}
