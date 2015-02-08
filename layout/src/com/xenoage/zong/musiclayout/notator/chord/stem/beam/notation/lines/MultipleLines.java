package com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.lines;

import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Implementation of a {@link BeamLines} strategy for for all
 * beams with four or more beamlines (64th, 128th, ...).
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MultipleLines
	extends OneLine {

	private float totalBeamHeightIs;


	public MultipleLines(StemDirection stemDirection, int staffLinesCount, int beamLinesCount) {
		super(stemDirection, staffLinesCount);
		this.totalBeamHeightIs = 0.5f * beamLinesCount + 0.5f * (beamLinesCount - 1);
	}

	@Override public float getDistanceBetweenBeamLinesIs() {
		return 0.5f;
	}

	@Override public float getMinimumStemLengthIs() {
		return 2.5f + totalBeamHeightIs;
	}

}
