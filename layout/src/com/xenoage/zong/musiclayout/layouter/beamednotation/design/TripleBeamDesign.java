package com.xenoage.zong.musiclayout.layouter.beamednotation.design;

import static com.xenoage.zong.musiclayout.layouter.beamednotation.alignment.SingleMeasureSingleStaffStrategy.isBeamOutsideStaff;

import com.xenoage.zong.core.music.chord.StemDirection;


/**
 * This implementation of {@link BeamDesign} is used for beams with
 * three beam lines.
 * 
 * @author Uli Teschemacher
 */
public class TripleBeamDesign
	extends BeamDesign
{

	private float totalBeamWidthIS = 2;


	/**
	 * Creates the design for a beam with three beam lines.
	 */
	public TripleBeamDesign(StemDirection stemDirection, int staffLinesCount)
	{
		super(stemDirection, staffLinesCount);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getMinimumStemLength()
	{
		return 4;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getSlantAscendingMiddleNotes()
	{
		return 0;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override public float getSlantDescendingMiddleNotes()
	{
		return 0;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override public boolean isGoodStemPosition(float startLP, float slantIS)
	{
		//look whether the beam doesn't start or end at a wrong position (e.g. between the lines)
		if (isBeamOutsideStaff(stemDirection, startLP, startLP + slantIS * 2,
			staffLinesCount, totalBeamWidthIS))
		{
			return true;
		}
		else
		{
			int linepositionstart = (int) ((startLP * 2 + 1000) % 4);
			int linepositionend = (int) (((startLP + slantIS * 2) * 2 + 1000) % 4);
			if ((startLP <= 2 && startLP + slantIS * 2 <= 2 && stemDirection == StemDirection.Up)
				||(startLP >= 6 && startLP + slantIS * 2 >= 6 && stemDirection == StemDirection.Down))
			{
				if ((linepositionstart == 0 && linepositionend == 0))
				{
					return true;
				}
			}
		}
		return false;	
	}


	/**
	 * {@inheritDoc}
	 */
	@Override public float getCloseSpacing(int startNoteLP, int endNoteLP)
	{
		if (Math.abs(startNoteLP - endNoteLP) == 1)
		{
			return 0.5f;
		}
		else
		{
			return 1f;
		}
	}


	/**
	 * {@inheritDoc}
	 */
	@Override public float getNormalSpacing(int startNoteLP, int endNoteLP)
	{
		return getNormalOrWideSpacing(startNoteLP, endNoteLP, false);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override public float getWideSpacing(int startNoteLP, int endNoteLP)
	{
		return getNormalOrWideSpacing(startNoteLP, endNoteLP, true);
	}
	
	
	private float getNormalOrWideSpacing(int startNoteLP, int endNoteLP, boolean wide)
	{
		int staffMaxLP = (staffLinesCount - 1) * 2;
		int dir = stemDirection.getSignum();
		float startStemLP = startNoteLP + dir * getMinimumStemLength();
		float endStemLP = endNoteLP + dir * getMinimumStemLength();
		if ((startStemLP < -1 && endStemLP < -1) ||
			(startStemLP > staffMaxLP + 1 && endStemLP > staffMaxLP + 1))
		{
			//use design of single beam
			SingleBeamDesign sbd = new SingleBeamDesign(stemDirection, staffLinesCount);
			return sbd.getNormalSpacing(startNoteLP, endNoteLP);
		}
		else
		{
			return getSlants(Math.abs(startNoteLP - endNoteLP))[wide ? 1 : 0];
		}
	}


	private float[] getSlants(int differenceLP)
	{
		differenceLP = Math.abs(differenceLP);
		float[][] ted = {
			{ 0, 0 },
			{ 0, 0 },
			{ 2, 2 },
		};
		float[][] used = ted;
		if (used.length - 1 < differenceLP)
		{
			return used[used.length - 1];
		}
		else
		{
			return used[differenceLP];
		}
	}
	
}
