package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.utils.math.MathUtils.mod;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.notator.beam.BeamNotator.beamNotator;
import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.BeamNotation;

/**
 * Implementation of a {@link BeamRules} strategy for a single line beam (8th).
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class Beam8thRules
	extends BeamRules {

	public static final Beam8thRules beam8thStemUpStaff5 = new Beam8thRules(Up, 5);
	
	private static final float totalBeamHeightIs = BeamNotation.lineHeightIs;
	
	private StemDirection stemDirection;
	private int staffLinesCount;
	
	
	@Override public int getBeamLinesCount() {
		return 1;
	}

	@Override public boolean isGoodStemPosition(float startLp, float slantIs) {
		//look whether the beam doesn't start or end at a wrong position (e.g. between the lines)
		if (beamNotator.isBeamOutsideStaff(stemDirection, startLp, startLp + slantIs * 2,
			staffLinesCount, totalBeamHeightIs))
			return true;
		
		//TODO: some of the following 4 are possibly not really always 4 but
		//are dependent on the staffLinesCount.
		int linepositionstart = mod((int) (startLp * 2), 4);
		int linepositionend = mod((int) ((startLp + slantIs * 2) * 2), 4);
		if (stemDirection == Down) {
			if (startLp <= 4 && startLp + slantIs * 2 <= 4) {
				//downstems must only straddle the line or sit on it (at the beginning)
				//the end of the stem must not be in the space between two lines
				if (Math.abs(slantIs) < 0.1f) {
					if (linepositionstart == 0 || linepositionstart == 3)
						return true;
				}
				else {
					if (linepositionstart != 1 && linepositionend != 1)
						return true;
				}
			}
		}
		else {
			if (startLp >= 4 && startLp + slantIs * 2 >= 4) {
				if (Math.abs(slantIs) < 0.1f) {
					if (linepositionstart == 0 || linepositionstart == 1)
						return true;
				}
				else {
					if (linepositionstart != 3 && linepositionend != 3)
						return true;
				}
			}
		}
		return false;
	}

	@Override public float getAbsSlantForCloseSpacingIs(int startNoteLp, int endNoteLp) {
		if (Math.abs(startNoteLp - endNoteLp) == 1)
			return 0.5f;
		else
			return 1f;
	}

	@Override public float getAbsSlantForNormalSpacingIs(int startNoteLp, int endNoteLp) {
		return getSlants(Math.abs(startNoteLp - endNoteLp))[0];
	}

	@Override public float getAbsSlantForWideSpacingIs(int startNoteLp, int endNoteLp) {
		return getSlants(Math.abs(startNoteLp - endNoteLp))[1];
	}

	/**
	 * Returns the slant in IS.
	 * @param differenceLp difference between the two interesting notes
	 */
	private float[] getSlants(int differenceLp) {
		differenceLp = Math.abs(differenceLp);
		float[][] ted = { //Ted Ross' book
		{ 0, 0 }, { 0.5F, 0.5F }, { 1, 1 }, { 2, 2.5F }, { 2.5F, 2.5F }, { 2.5F, 3 }, { 2.5F, 3.5F },
			{ 2.5F, 4F } };
		/*float[][] sib = {   //Sibelius
			{0,0},
			{0.5F,0.5F},
			{1,1},
			{1.5F,2}
		};*/
		float[][] used = ted;
		if (used.length - 1 < differenceLp) {
			return used[used.length - 1];
		}
		else {
			return used[differenceLp];
		}
	}

}
