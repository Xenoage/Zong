package com.xenoage.zong.musiclayout.layouter.beamednotation.design;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.musiclayout.layouter.beamednotation.alignment.SingleMeasureSingleStaffStrategy.isBeamOutsideStaff;

import com.xenoage.zong.core.music.chord.StemDirection;


/**
 * This implementation of {@link BeamDesign} is the default design for a single beam.
 * 
 * @author Uli Teschemacher
 */
public class SingleBeamDesign
	extends BeamDesign
{
	
	private float totalBeamWidthIS = 0.5f;
	
	
	/**
	 * Creates the design for a beam with one beam line.
	 */
	public SingleBeamDesign(StemDirection stemDirection, int staffLinesCount)
	{
		super(stemDirection, staffLinesCount);
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
			//TODO: some of the following 4 are possibly not really always 4 but
			//dependent on the staffLinesCount.
			int linepositionstart = (int) ((startLP * 2 + 1000) % 4);
			int linepositionend = (int) (((startLP + slantIS * 2) * 2 + 1000) % 4);
			if (stemDirection == Down)
			{
				if (startLP <= 4 && startLP + slantIS * 2 <= 4)
				{
					//downstems must only straddle the line or sit on it (at the beginning)
					//the end of the stem must not be in the space between two lines
					if (Math.abs(slantIS) < 0.1f)
					{
						if (linepositionstart == 0 || linepositionstart == 3)
						{
							return true;
						}
					}
					else
					{
						if (linepositionstart != 1 && linepositionend != 1)
						{
							return true;
						}
					}
				}
			}
			else
			{
				if (startLP >= 4 && startLP + slantIS * 2 >= 4)
				{
					if (Math.abs(slantIS) < 0.1f)
					{
						if (linepositionstart == 0 || linepositionstart == 1)
						{
							return true;
						}
					}
					else
					{
						if (linepositionstart != 3 && linepositionend != 3)
						{
							return true;
						}
					}
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
		return getSlants(Math.abs(startNoteLP - endNoteLP))[0];
	}


	/**
	 * {@inheritDoc}
	 */
	@Override public float getWideSpacing(int startNoteLP, int endNoteLP)
	{
		return getSlants(Math.abs(startNoteLP - endNoteLP))[1];
	}
	
	/**
	 * Returns the slant in Linepositions.
	 * @param differenceLP difference between the two interesting notes
	 * @return
	 */
	private float[] getSlants(int differenceLP)
	{
		differenceLP = Math.abs(differenceLP);
		float[][] ted = {  //Ted Ross' book
			{0,0},
			{0.5F,0.5F},
			{1,1},
			{2,2.5F},
			{2.5F,2.5F},
			{2.5F,3},
			{2.5F,3.5F},
			{2.5F,4F}
		};
		/*float[][] sib = {   //Sibelius
			{0,0},
			{0.5F,0.5F},
			{1,1},
			{1.5F,2}
		};*/
		float[][] used = ted;
		if (used.length-1 < differenceLP)
		{
			return used[used.length-1];
		}
		else
		{
			return used[differenceLP];
		}
	}

}
