package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.zong.musiclayout.notation.BeamNotation.normalGapIs;

/**
 * {@link BeamRules} for a single-line beam (8th).
 * 
 * @author Andreas Wenger
 */
public class Beam8thRules
	extends BeamRules {
	
	public static final Beam8thRules beam8thRules = new Beam8thRules();
	
	
	@Override public int getBeamLinesCount() {
		return 1;
	}

	@Override public float getMinimumStemLengthIs() {
		return 3;
	}

	@Override public float getGapIs() {
		return normalGapIs;
	}

}
