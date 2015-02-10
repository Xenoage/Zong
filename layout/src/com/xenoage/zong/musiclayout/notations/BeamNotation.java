package com.xenoage.zong.musiclayout.notations;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.beam.Beam;

/**
 * Layout details of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamNotation {

	/** The height in IS for each beam line. */
	public float lineHeightIs;
	/** The vertical gap (distance on y-axis) between the beam lines in IS. */
	public float gapIs;
	/** The number of beam lines, e.g. 2 for 16th notes. */
	public int linesCount;

	/**
	 * Returns the height of all lines of the beam including their distances in IS.
	 */
	public float getTotalHeightIs() {
		return lineHeightIs * linesCount + gapIs * (linesCount - 1);
	}
	
}
