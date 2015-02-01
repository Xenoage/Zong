package com.xenoage.zong.musiclayout.notator.beam;

import static com.xenoage.zong.core.music.beam.Beam.HorizontalSpan.SingleMeasure;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.SingleStaff;
import static com.xenoage.zong.musiclayout.notator.beam.direction.OneMeasureOneStaff.oneMeasureOneStaff;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notator.beam.direction.Strategy;

/**
 * Computes the {@link StemDirection} of beamed chords.
 * 
 * @author Andreas Wenger
 */
public class BeamedStemDirector {

	public StemDirection[] compute(Beam beam, Score score) {

		//choose appropriate strategy
		Strategy strategy;
		if (beam.getHorizontalSpan() == SingleMeasure) {
			if (beam.getVerticalSpan() == SingleStaff)
				strategy = oneMeasureOneStaff;
			else if (beam.getVerticalSpan() == VerticalSpan.TwoAdjacentStaves)
				strategy = null; //GOON
			else
				//GOON
				throw new IllegalStateException("No strategy for more than two or non-adjacent staves");
		}
		else {
			//GOON
			throw new IllegalStateException("Multi-measure beams are not supported yet");
		}
		
		return strategy.compute(beam, score);
	}

}
