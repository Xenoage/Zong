package com.xenoage.zong.musiclayout.notator.chord.stem.beam;

import static com.xenoage.zong.core.music.beam.Beam.HorizontalSpan.SingleMeasure;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.SingleStaff;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.TwoAdjacentStaves;
import static com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.OneMeasureOneStaff.oneMeasureOneStaff;

import java.util.List;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.Beam.HorizontalSpan;
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.notations.Notations;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.OneMeasureOneStaff;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.OneMeasureTwoStaves;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.lines.OneLine;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;

/**
 * Computes the {@link StemNotation} of the stems of a {@link Beam}.
 * 
 * One of the chords of the beam must be given, but the notations
 * are computed and updated for all chords in the beam.
 * 
 * @author Andreas Wenger
 */
public class BeamedStemNotator {


	public void compute(Chord chord, List<ColumnSpacing> columnSpacings) {
		Beam beam = chord.getBeam();
		
		//choose appropriate strategy
		if (beam.getHorizontalSpan() == SingleMeasure) {
			if (beam.getVerticalSpan() == SingleStaff) {
				oneMeasureOneStaff.compute(beam, columnSpacings);
			}
			else if (beam.getVerticalSpan() == TwoAdjacentStaves) {
				singleMeasureTwoStavesStrategy.computeNotations(beam, notations);
			}
			else {
				throw new IllegalStateException("No strategy for more than two or non-adjacent staves");
			}
		}
		else {
			//Multi-measure beams are not supported yet - TODO
		}
	}

}
