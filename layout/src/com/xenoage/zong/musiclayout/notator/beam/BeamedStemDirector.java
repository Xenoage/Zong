package com.xenoage.zong.musiclayout.notator.beam;

import static com.xenoage.utils.iterators.It.it;

import java.util.Iterator;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.beam.Beam.HorizontalSpan;
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.notator.Notator;
import com.xenoage.zong.musiclayout.notator.beam.direction.OneMeasureOneStaff;
import com.xenoage.zong.musiclayout.notator.beam.direction.SingleMeasureTwoStavesStrategy;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;

/**
 * Recomputes the {@link ChordNotation}s of a {@link Beam}, if necessary,
 * so that the beamed chords have the right stem direction.
 * 
 * The lengths of the stems are not fitted to the beam here, this must
 * be done by a later in another layout step. It can not be done here, because
 * we do not know the horizontal distance between the stems yet,
 * which is necessary to compute a nice angle for the beam.
 * 
 * @author Andreas Wenger
 */
public class BeamedStemDirector {

	//strategies
	private final OneMeasureOneStaff singleMeasureSingleStaffStrategy;
	private final SingleMeasureTwoStavesStrategy singleMeasureTwoStavesStrategy;


	/**
	 * Creates a new {@link BeamedStemDirector}.
	 */
	public BeamedStemDirector(Notator notationStrategy) {
		singleMeasureSingleStaffStrategy = new OneMeasureOneStaff(notationStrategy);
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
		
		
		//GOON
	//return the results as a new NotationsCache
			NotationsCache ret = new NotationsCache();
			Iterator<BeamWaypoint> beamWaypoints = it(beam.getWaypoints());
			for (int i = 0; i < bsd.stemDirections.length; i++) {
				Chord chord = beamWaypoints.next().getChord();
				ChordNotation oldChordNotation = notations.getChord(chord);
				StemDirection oldStemDir = oldChordNotation.getStemDirection();
				//if stem direction was changed, recompute the notation.
				//the stem lengths are not fitted to the beam already, so this
				//has to be done later within another strategy
				if (bsd.stemDirections[i] != oldStemDir) {
					ret.add(notationStrategy.computeChord(chord, bsd.stemDirections[i], score,
						layoutSettings));
				}
			}
	}

}
