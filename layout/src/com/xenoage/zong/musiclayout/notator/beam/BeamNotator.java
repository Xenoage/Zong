package com.xenoage.zong.musiclayout.notator.beam;

import static com.xenoage.zong.core.music.beam.Beam.HorizontalSpan.SingleMeasure;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.SingleStaff;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.TwoAdjacentStaves;
import static com.xenoage.zong.musiclayout.notator.beam.range.OneMeasureOneStaff.oneMeasureOneStaff;
import static com.xenoage.zong.musiclayout.notator.beam.range.OneMeasureTwoStaves.oneMeasureTwoStaves;

import java.util.List;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.notations.BeamNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;

/**
 * Computes {@link BeamNotation}s and the {@link StemNotation}s of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public class BeamNotator {
	
	public static final BeamNotator beamNotator = new BeamNotator();


	public void compute(Beam beam, List<ColumnSpacing> columnSpacings) {

		//choose appropriate strategy
		if (beam.getHorizontalSpan() == SingleMeasure) {
			if (beam.getVerticalSpan() == SingleStaff) {
				oneMeasureOneStaff.compute(beam, columnSpacings);
			}
			else if (beam.getVerticalSpan() == TwoAdjacentStaves) {
				oneMeasureTwoStaves.compute(beam, columnSpacings);
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
