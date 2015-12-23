package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.zong.musiclayout.notation.BeamNotation.normalGapIs;

/**
 * {@link BeamRules} for a three-line beam (32nd).
 * 
 * @author Andreas Wenger
 */
public class Beam32ndRules
	extends BeamRules {
	
	public static final Beam32ndRules beam32ndRules = new Beam32ndRules();
	
	
	@Override public int getBeamLinesCount() {
		return 3;
	}

	@Override public float getMinimumStemLengthIs() {
		//see Ross, p. 125, section 10
		//3.25 (normal length) + 0.5 extra
		return 3.75f;
	}

	@Override public float getGapIs() {
		//see Ross, p. 125, section 10
		return normalGapIs;
	}

}
