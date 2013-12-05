package com.xenoage.zong.musiclayout.layouter.beamednotation;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.Beam.HorizontalSpan;
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.direction.SingleMeasureSingleStaffStrategy;
import com.xenoage.zong.musiclayout.layouter.beamednotation.direction.SingleMeasureTwoStavesStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;

/**
 * This strategy recomputes the {@link Notation}s of the chords
 * of the given {@link Beam}, if necessary, so that the chords
 * have the right stem direction.
 * 
 * The lengths of the stems are not fitted to the beam here, this must
 * be done by a later strategy called {@link BeamedStemAlignmentNotationsStrategy}
 * (This can not be done here, because
 * we do not know the horizontal distance between the stems yet,
 * which is necessary to compute a nice angle for the beam).
 * 
 * This strategy will decide which substrategy fits best for the given
 * {@link Beam} and delegates the work to it.
 * 
 * @author Andreas Wenger
 */
public class BeamedStemDirectionNotationsStrategy
	implements ScoreLayouterStrategy {

	//strategies
	private final SingleMeasureSingleStaffStrategy singleMeasureSingleStaffStrategy;
	private final SingleMeasureTwoStavesStrategy singleMeasureTwoStavesStrategy;


	/**
	 * Creates a new {@link BeamedStemDirectionNotationsStrategy}.
	 */
	public BeamedStemDirectionNotationsStrategy(NotationStrategy notationStrategy) {
		singleMeasureSingleStaffStrategy = new SingleMeasureSingleStaffStrategy(notationStrategy);
		singleMeasureTwoStavesStrategy = new SingleMeasureTwoStavesStrategy(notationStrategy);
	}

	/**
	 * Returns better {@link Notation}s of the chords connected by the given beam,
	 * using the given notations and musical context.
	 * Only changed notations are returned.
	 */
	public NotationsCache computeNotations(Beam beam, NotationsCache notations, Score score,
		LayoutSettings layoutSettings) {

		//choose appropriate strategy
		if (beam.getHorizontalSpan() == HorizontalSpan.SingleMeasure) {
			if (beam.getVerticalSpan() == VerticalSpan.SingleStaff) {
				MP firstMP = MP.getMP(beam.getStart().getChord());
				return singleMeasureSingleStaffStrategy.computeNotations(beam, notations,
					score.getStaff(firstMP).getLinesCount(), score, layoutSettings);
			}
			else if (beam.getVerticalSpan() == VerticalSpan.TwoAdjacentStaves) {
				return singleMeasureTwoStavesStrategy.computeNotations(beam, notations, score,
					layoutSettings);
			}
			else {
				throw new IllegalStateException("No strategy for more than two or non-adjacent staves");
			}
		}
		else {
			//Multi-measure beams are not supported yet TODO
			return notations;
		}
	}

}
