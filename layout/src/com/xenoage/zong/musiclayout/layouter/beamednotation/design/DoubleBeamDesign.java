package com.xenoage.zong.musiclayout.layouter.beamednotation.design;

import static com.xenoage.zong.musiclayout.layouter.beamednotation.alignment.SingleMeasureSingleStaffStrategy.isBeamOutsideStaff;

import com.xenoage.zong.core.music.chord.StemDirection;


/**
 * Implementation of a {@link BeamDesign} for a two-line beam.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class DoubleBeamDesign
	extends BeamDesign
{
	
	private float totalBeamWidthIS = 1.25f;
	
	
	/**
	 * Creates a beam design for a two-line beam.
	 */
	public DoubleBeamDesign(StemDirection stemDirection, int staffLinesCount)
	{
		super(stemDirection, staffLinesCount);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override public float getMinimumStemLength()
	{
		return 3.25f;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override public boolean isGoodStemPosition(float startLP, float slantIS)
	{
		int maxStaffLP = 2 * (staffLinesCount - 1);
		//look whether the beam doesn't start or end at a wrong position (e.g. between the lines)
		if ((startLP < -3 && startLP + slantIS * 2 < -3)
			|| (startLP > maxStaffLP + 3 && startLP + slantIS * 2 > maxStaffLP + 3))
		{
			return true;
		}
		else
		{
			//TODO: some of the following 4 are possibly not really always 4 but
			//dependent on the staffLinesCount.
			int linepositionstart = (int) ((startLP * 2 + 1000) % 4);
			int linepositionend = (int) (((startLP + slantIS * 2) * 2 + 1000) % 4);
			if (stemDirection == StemDirection.Down)
			{
				if (startLP <= 4 && startLP + slantIS * 2 <= 4)
				{
					//downstems must only straddle the line or sit on it (at the beginning).
					//the end of the stem must not be in the space between two lines.
					if (Math.abs(slantIS) < 0.1f)
					{
						if (linepositionstart == 0 || linepositionstart == 3)
						{
							return true;
						}
					}
					else
					{
						if ((linepositionstart == 0 && linepositionend == 3)
							|| (linepositionstart == 3 && linepositionend == 0))
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
						if ((linepositionstart == 0 && linepositionend == 1)
							|| (linepositionstart == 1 && linepositionend == 0))
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
		int dir = stemDirection.getSignum();
		float startStemLP = startNoteLP + dir * getMinimumStemLength();
		float endStemLP = endNoteLP + dir * getMinimumStemLength();
		if (isBeamOutsideStaff(stemDirection, startStemLP, endStemLP, staffLinesCount, totalBeamWidthIS))
		{
			//use design of single beam
			SingleBeamDesign sbd = new SingleBeamDesign(stemDirection, staffLinesCount);
			return sbd.getNormalSpacing(startNoteLP, endNoteLP);			
		}
		else
		{
			return 0.5f;
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
		float[][] ted = {  //like in Ted Ross' book
			{0,0},
			{0.5F,0.5F},
			{0.5F,1.5F},
			{2,2.5F},
			{2.5F,2.5F},
			{2.5F,2.5F},
			{2.5F,3.5F},
			{2.5F,4F}
		};
		/*float[][] sib = {  //like in sibelius
			{0,0},
			{0.5F,0.5F},
			{0.5F,0.5F},
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
