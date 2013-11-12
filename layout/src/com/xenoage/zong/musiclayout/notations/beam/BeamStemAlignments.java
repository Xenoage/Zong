package com.xenoage.zong.musiclayout.notations.beam;

import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;


/**
 * Contains the layouting details of a {@link Beam}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public final class BeamStemAlignments
{
	
	private final StemAlignment[] stemAlignments;
	private final float beamLineWidth;
	private final float beamLineDistance;
	private final int beamLinesCount;

	
	/**
	 * Creates a new {@link BeamStemAlignments}.
	 * @param stemAlignments    the alignments of the stems stems of all chords
	 * @param beamLineWidth     the width (vertical) in IS for all beam lines
	 * @param beamLineDistance  the vertical distance between the beam lines in IS
	 *                          (the real gap between them)
	 * @param beamLinesCount    the number of beam lines, e.g. 2 for 16th notes
	 */
	public BeamStemAlignments(StemAlignment[] stemAlignments, float beamLineWidth,
		float beamLineDistance, int beamLinesCount)
	{
		this.stemAlignments = stemAlignments;
		this.beamLineWidth = beamLineWidth;
		this.beamLineDistance = beamLineDistance;
		this.beamLinesCount = beamLinesCount;
	}

	
	/**
	 * Gets the alignments of the stems stems of all chords.
	 */
	public StemAlignment[] getStemAlignments()
	{
		return stemAlignments;
	}

	
	/**
	 * Gets the width (vertical) in IS for all beam lines.
	 */
	public float getBeamsWidth()
	{
		return beamLineWidth;
	}

	
	/**
	 * Gets the vertical distance between the beam lines in IS
	 * (the real gap between them).
	 */
	public float getBeamsDistance()
	{
		return beamLineDistance;
	}

	
	/**
	 * Gets the number of beam lines, e.g. 2 for 16th notes.
	 */
	public int getLinesCount()
	{
		return beamLinesCount;
	}
	
	
	/**
	 * Returns the added up width of all lines of the beam including their distances
	 */
	public float getTotalWidth()
	{
		return beamLineWidth * beamLinesCount + beamLineDistance * (beamLinesCount - 1);
	}
}
