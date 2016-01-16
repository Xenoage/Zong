package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.zong.musiclayout.notation.BeamNotation.normalGapIs;

/**
 * {@link BeamRules} for a two-line beam (16th).
 * 
 * @author Andreas Wenger
 */
public class Beam16thRules
	extends BeamRules {
	
	public static final Beam16thRules beam16thRules = new Beam16thRules();
	

	@Override public int getBeamLinesCount() {
		return 2;
	}

	@Override public float getMinimumStemLengthIs() {
		//see Ross, p. 121, bottom half
		return 3.25f;
	}

	@Override public float getGapIs() {
		//see Ross, p. 120f
		return normalGapIs;
	}

}
