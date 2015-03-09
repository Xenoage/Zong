package com.xenoage.zong.musiclayout.notator.beam.lines;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Implementation of a {@link BeamRules} strategy for for all
 * beams with four or more beamlines (64th, 128th, ...).
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class Beam64thOrMoreRules
	extends Beam8thRules {

	private float totalBeamHeightIs;


	public Beam64thOrMoreRules(StemDirection stemDirection, int staffLinesCount, int beamLinesCount) {
		super(stemDirection, staffLinesCount);
		this.totalBeamHeightIs = 0.5f * beamLinesCount + 0.5f * (beamLinesCount - 1);
	}

	@Override public float getGapIs() {
		return 0.5f;
	}

	@Override public float getMinimumStemLengthIs() {
		return 2.5f + totalBeamHeightIs;
	}

}
