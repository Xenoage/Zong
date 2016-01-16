package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.zong.musiclayout.notation.BeamNotation.largeGapIs;
import static com.xenoage.zong.musiclayout.notation.BeamNotation.normalGapIs;

/**
 * {@link BeamRules} for a beam with five beamlines (128th).
 * 
 * @author Andreas Wenger
 */
public class Beam128thRules
	extends BeamRules {
	
	public static final Beam128thRules beam128thRules = new Beam128thRules();
	
	
	@Override public int getBeamLinesCount() {
		return 5;
	}

	//TODO: depends on gap (getGapIs vs getGapOutsideStaffIs)
	@Override public float getMinimumStemLengthIs() {
		//see Ross, p. 121, bottom half: a secondary beam
		//should never be closer to the notehead then 2.5 IS
		//thus, the minimum stem length is 5.5 (2.5 + gap + line + gap + line + gap + line),
		//where gap is 0.5 here
		return 5.5f;
	}

	@Override public float getGapIs() {
		return largeGapIs;
	}

	@Override public float getGapOutsideStaffIs() {
		//see Ross, p. 126, at the top
		return normalGapIs;
	}

}
