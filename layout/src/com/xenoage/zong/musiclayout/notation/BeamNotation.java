package com.xenoage.zong.musiclayout.notation;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.notation.beam.Fragments;

/**
 * 
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamNotation {
	
	/** Height of a beam line in IS. See Ross, p. 88.*/
	public static final float lineHeightIs = 0.5f;
	
	public static final float defaultGapIs = 0.25f; //default gap between lines in interline spaces
	public static final float hookLengthIs = 1.25f; //Chlapik TODO


	/** The beam element. */
	public Beam element;
	/** The fragments for each line, starting with the 16th line, then 32nd ... .
	 * The 8th line is always continuous, so it is not stored. */
	public List<Fragments> linesFragments;
	/** The vertical gap (distance on y-axis) between the beam lines in IS. */
	public float gapIs;
	
	
	public int getLinesCount() {
		return linesFragments.size() + 1;
	}
	
	/**
	 * Returns the height of all lines of the beam including their distances in IS.
	 */
	public float getTotalHeightIs() {
		int linesCount = getLinesCount();
		return lineHeightIs * linesCount + gapIs * (linesCount - 1);
	}
	
}
