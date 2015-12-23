package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.zong.musiclayout.notation.BeamNotation.normalGapIs;

/**
 * {@link BeamRules} for a four-line beam (64th).
 * 
 * @author Andreas Wenger
 */
public class Beam64thRules
	extends BeamRules {
	
	public static final Beam64thRules beam64thRules = new Beam64thRules();
	
	
	@Override public int getBeamLinesCount() {
		return 4;
	}

	//TODO: depends on gap (getGapIs vs getGapOutsideStaffIs)
	@Override public float getMinimumStemLengthIs() {
		//see Ross, p. 121, bottom half: a secondary beam
		//should never be closer to the notehead then 2.5 IS
		//thus, the minimum stem length is 4 (2.5 + gap + line + gap + line).
		return 4;
	}

	@Override public float getGapIs() {
		//see Ross, p. 125, section 11
		//for 4 beam lines in 3 
		return 1/3f;
	}

	@Override public float getGapOutsideStaffIs() {
		//see Ross, p. 126, at the top
		return normalGapIs;
	}

}
