package com.xenoage.zong.musiclayout.notation;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.notation.beam.Fragments;

/**
 * Layout information about a beam, like the appearance
 * of the beam lines.
 * 
 * Ross (p. 119) calles the 8th beam line a primary line, and all others
 * (16th, 32nd, ...) secondary lines.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamNotation {
	
	/** Height of a beam line in IS. See Ross, p. 88.*/
	public static final float lineHeightIs = 0.5f;
	/** Default gap between lines in IS. Since three beam lines should
	 * fit into two IS (see Chlapik, p. 41, rule 1, and Ross, p. 119),
	 * the default gap between the lines is {@value #gapIs}. */ 
	public static final float normalGapIs = 0.25f;
	/** Larger gap between lines in IS. It should be used when a beam
	 * has four or more lines, to avoid beam lines centered within staff whitespace.
	 * Ross, p. 125, at the bottom, uses a more flexible value, but it should be
	 * fine for the moment. */
	public static final float largeGapIs = 0.5f;
	/** Length of a beam hook in IS. See Chlapik, p. 45, rule 6. */
	public static final float hookLengthIs = 1.25f;


	/** The beam element. */
	public Beam element;
	/** The fragments for each secondary line, starting with the 16th line, then 32nd ... .
	 * The primary 8th line is always continuous, so it is not stored. */
	public List<Fragments> linesFragments;
	/** The vertical gap (distance on y-axis) between the beam lines in IS. */
	public float gapIs;
	/** The beamed chords. */
	public List<ChordNotation> chords;
	
	
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
