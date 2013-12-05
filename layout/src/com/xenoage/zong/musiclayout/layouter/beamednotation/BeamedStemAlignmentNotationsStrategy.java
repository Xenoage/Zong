package com.xenoage.zong.musiclayout.layouter.beamednotation;

import java.util.List;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.Beam.HorizontalSpan;
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.alignment.SingleMeasureSingleStaffStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.alignment.SingleMeasureTwoStavesStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;

/**
 * This strategy computes the alignments of the stems and the widths
 * of a given {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public class BeamedStemAlignmentNotationsStrategy
	implements ScoreLayouterStrategy {

	private SingleMeasureSingleStaffStrategy singleMeasureSingleStaffStrategy = new SingleMeasureSingleStaffStrategy();
	private SingleMeasureTwoStavesStrategy singleMeasureTwoStavesStrategy = new SingleMeasureTwoStavesStrategy();


	/**
	 * This strategy computes the lengths of the stems of beamed notes.
	 * It only works for chords, in which all the stems point in the same
	 * direction! (like computed by {@link BeamedStemDirectionNotationsStrategy}).
	 * The updated chord notations are returned.
	 */
	public NotationsCache computeNotations(Score score, Beam beam,
		List<ColumnSpacing> columnSpacings, NotationsCache notations) {

		//choose appropriate strategy
		if (beam.getHorizontalSpan() == HorizontalSpan.SingleMeasure) {
			if (beam.getVerticalSpan() == VerticalSpan.SingleStaff) {
				MP firstMP = MP.getMP(beam.getStart().getChord());
				return singleMeasureSingleStaffStrategy.computeNotations(score, beam,
					columnSpacings.get(firstMP.measure), notations);
			}
			else if (beam.getVerticalSpan() == VerticalSpan.TwoAdjacentStaves) {
				return singleMeasureTwoStavesStrategy.computeNotations(beam, notations);
			}
			else {
				throw new IllegalStateException("No strategy for more than two or non-adjacent staves");
			}
		}
		else {
			//Multi-measure beams are not supported yet - TODO
			return notations;
		}
	}

}
