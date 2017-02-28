package com.xenoage.zong.musiclayout.notator.chord.stem.beam;

import static com.xenoage.utils.collections.ArrayUtils.setValues;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.SingleStaff;
import static com.xenoage.zong.musiclayout.notator.chord.stem.beam.range.OneMeasureOneStaff.oneMeasureOneStaff;
import static com.xenoage.zong.musiclayout.notator.chord.stem.beam.range.OneMeasureTwoStaves.oneMeasureTwoStaves;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.range.Strategy;

/**
 * Computes the {@link StemDirection} of beamed chords.
 * 
 * @author Andreas Wenger
 */
public class BeamedStemDirector {
	
	public static final BeamedStemDirector beamedStemDirector = new BeamedStemDirector();
	

	public StemDirection[] compute(Beam beam, Score score) {
		//choose appropriate strategy
		Strategy strategy;
		if (beam.getVerticalSpan() == SingleStaff)
			strategy = oneMeasureOneStaff;
		else if (beam.getVerticalSpan() == VerticalSpan.CrossStaff)
			strategy = oneMeasureTwoStaves;
		else
			//no strategy for more than two or non-adjacent staves
			return fallback(beam.size());
		
		return strategy.compute(beam, score);
	}
	
	/**
	 * Fallback for unsupported beams: All stems up.
	 */
	private StemDirection[] fallback(int stemsCount) {
		StemDirection[] ret = new StemDirection[stemsCount];
		setValues(ret, StemDirection.Up);
		return ret;
	}

}
