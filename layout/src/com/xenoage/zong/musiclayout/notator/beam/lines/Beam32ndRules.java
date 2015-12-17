package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.utils.math.MathUtils.mod;
import static com.xenoage.zong.musiclayout.notator.beam.BeamNotator.beamNotator;
import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Implementation of a {@link BeamRules} strategy for a three-line beam (32nd).
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class Beam32ndRules
	extends BeamRules {

	private static final float totalBeamHeightIs = 2;
	
	private StemDirection stemDirection;
	private int staffLinesCount;

	
	@Override public int getBeamLinesCount() {
		return 3;
	}

	@Override public float getMinimumStemLengthIs() {
		return 4;
	}

	@Override public float getAbsSlantForMiddleNotesRunIs() {
		return 0;
	}

	@Override public boolean isGoodStemPosition(float startLp, float slantIs) {
		//look whether the beam doesn't start or end at a wrong position (e.g. between the lines)
		if (beamNotator.isBeamOutsideStaff(stemDirection, startLp, startLp + slantIs * 2, staffLinesCount,
			totalBeamHeightIs)) {
			return true;
		}
		else {
			int linepositionstart = mod((int) (startLp * 2), 4);
			int linepositionend = mod((int) (startLp + slantIs * 2) * 2, 4);
			if ((startLp <= 2 && startLp + slantIs * 2 <= 2 && stemDirection == StemDirection.Up) ||
				(startLp >= 6 && startLp + slantIs * 2 >= 6 && stemDirection == StemDirection.Down)) {
				if ((linepositionstart == 0 && linepositionend == 0)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override public float getAbsSlantForCloseSpacingIs(int startNoteLp, int endNoteLp) {
		if (Math.abs(startNoteLp - endNoteLp) == 1) {
			return 0.5f;
		}
		else {
			return 1f;
		}
	}

	@Override public float getAbsSlantForNormalSpacingIs(int startNoteLp, int endNoteLp) {
		return getNormalOrWideSpacing(startNoteLp, endNoteLp, false);
	}

	@Override public float getAbsSlantForWideSpacingIs(int startNoteLp, int endNoteLp) {
		return getNormalOrWideSpacing(startNoteLp, endNoteLp, true);
	}

	private float getNormalOrWideSpacing(int startNoteLp, int endNoteLp, boolean wide) {
		int staffMaxLP = (staffLinesCount - 1) * 2;
		int dir = stemDirection.getSign();
		float startStemLp = startNoteLp + dir * getMinimumStemLengthIs(); //TODO: *2 missing for IS->LP?
		float endStemLp = endNoteLp + dir * getMinimumStemLengthIs(); //TODO: *2 missing for IS->LP?
		if ((startStemLp < -1 && endStemLp < -1) ||
			(startStemLp > staffMaxLP + 1 && endStemLp > staffMaxLP + 1)) {
			//use design of single beam
			Beam8thRules sbd = new Beam8thRules(stemDirection, staffLinesCount);
			return sbd.getAbsSlantForNormalSpacingIs(startNoteLp, endNoteLp);
		}
		else {
			return getSlants(Math.abs(startNoteLp - endNoteLp))[wide ? 1 : 0];
		}
	}

	private float[] getSlants(int differenceLp) {
		differenceLp = Math.abs(differenceLp);
		float[][] ted = { { 0, 0 }, { 0, 0 }, { 2, 2 }, }; //values from Ted Ross' book
		float[][] used = ted;
		if (used.length - 1 < differenceLp) {
			return used[used.length - 1];
		}
		else {
			return used[differenceLp];
		}
	}

}
