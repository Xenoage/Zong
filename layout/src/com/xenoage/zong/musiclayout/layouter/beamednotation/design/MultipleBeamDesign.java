package com.xenoage.zong.musiclayout.layouter.beamednotation.design;

import com.xenoage.zong.core.music.chord.StemDirection;


/**
 * This class is a implementation of {@link BeamDesign}. It can be used for all
 * beams with 4 or more beamlines.
 * 
 * @author Uli Teschemacher
 */
public class MultipleBeamDesign
	extends BeamDesign
{

	private float totalBeamWidthIS;
	private SingleBeamDesign singleBeamDesign;

	
	/**
	 * Creates a beam design for a beam with more than 3 lines.
	 */
	public MultipleBeamDesign(StemDirection stemDirection, int staffLinesCount, int beamLinesCount)
	{
		super(stemDirection, staffLinesCount);
		this.totalBeamWidthIS = 0.5f * beamLinesCount + 0.5f * (beamLinesCount - 1);
		this.singleBeamDesign = new SingleBeamDesign(stemDirection, staffLinesCount);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getDistanceBetweenBeamLines()
	{
		return 0.5f;
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getMinimumStemLength()
	{
		return 2.5f + totalBeamWidthIS;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getSlantAscendingMiddleNotes()
	{
		return singleBeamDesign.getSlantAscendingMiddleNotes();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getSlantDescendingMiddleNotes()
	{
		return singleBeamDesign.getSlantDescendingMiddleNotes();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getCloseSpacing(int startNoteLP, int endNoteLP)
	{
		return singleBeamDesign.getCloseSpacing(startNoteLP, endNoteLP);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getNormalSpacing(int startNoteLP, int endNoteLP)
	{
		return singleBeamDesign.getNormalSpacing(startNoteLP, endNoteLP);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getWideSpacing(int startNoteLP, int endNoteLP)
	{
		return singleBeamDesign.getWideSpacing(startNoteLP, endNoteLP);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override public boolean isGoodStemPosition(float startLP, float slantIS)
	{
		return singleBeamDesign.isGoodStemPosition(startLP, slantIS);
	}

}
