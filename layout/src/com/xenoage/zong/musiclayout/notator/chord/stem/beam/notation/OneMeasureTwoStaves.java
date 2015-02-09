package com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation;

import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;

import java.util.List;

import com.xenoage.utils.iterators.It;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.beam.BeamStemAlignments;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.lines.BeamLines;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.lines.MultipleLines;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.lines.OneLine;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.lines.ThreeLines;
import com.xenoage.zong.musiclayout.notator.chord.stem.beam.notation.lines.TwoLines;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;

/**
 * {@link Strategy} for a {@link Beam}, which spans over two adjacent staves
 * in a single measure column.
 * 
 * The strategy is quite simple: The first and the last stem have the default lengths
 * (or the lengths the user has defined). All other stem alignments can not be
 * computed here, since the distance of the staves is still unknown. They have to be
 * computed later, meanwhile they have wrong values. - TODO: change this! the staff
 * distances should be known already!
 * 
 * @author Andreas Wenger
 */
public class OneMeasureTwoStaves
	implements Strategy {

	@Override public void compute(Beam beam, List<ColumnSpacing> columnSpacings) {
		
		NotesNotation[] chordNa = new NotesNotation[beam.getWaypoints().size()];
		int beamlines = beam.getMaxBeamLinesCount();
		int i = 0;
		for (BeamWaypoint waypoint : beam.getWaypoints()) {
			Chord chord = waypoint.getChord();
			ChordNotation cn = notations.getChord(chord);
			chordNa[i] = cn.notes;
			i++;
		}
		Chord firstChord = beam.getStart().getChord();
		Stem firstStem = firstChord.getStem();
		StemDirection firstStemDirection = notations.getChord(firstChord).stemDirection;
		Chord lastChord = beam.getStop().getChord();
		Stem lastStem = lastChord.getStem();
		StemDirection lastStemDirection = notations.getChord(lastChord).stemDirection;

		BeamStemAlignments bsa = computeStemAlignments(chordNa, beamlines, firstStem, lastStem,
			firstStemDirection, lastStemDirection);

		//compute new notations
		It<BeamWaypoint> waypoints = it(beam.getWaypoints());
		for (BeamWaypoint waypoint : waypoints) {
			Chord chord = waypoint.getChord();
			ChordNotation cn = notations.getChord(chord);
			cn.stem = bsa.stemAlignments[waypoints.getIndex()];
		}
	}

	/**
	 * Computes the vertical positions of all stems
	 * of the given beam. The lengths of the middle stems have to be
	 * recomputed in a later step, since their lengths can not be computed yet.                    
	 */
	public BeamStemAlignments computeStemAlignments(List<ChordNotation> notations, int beamLinesCount) {
		//get appropriate beam design
		BeamLines strategy;
		StemDirection firstStemDirection = getFirst(notations).stemDirection;
		switch (beamLinesCount) {
		//TIDY: we need only a small subset of the BeamDesign class. extract it?
			case 1:
				strategy = new OneLine(firstStemDirection, 0);
				break;
			case 2:
				strategy = new TwoLines(firstStemDirection, 0);
				break;
			case 3:
				strategy = new ThreeLines(firstStemDirection, 0);
				break;
			default:
				strategy = new MultipleLines(firstStemDirection, 0, beamLinesCount);
		}

		//compute stem alignments
		int chordsCount = notations.size();
		for (int i : range(notations)) {
			ChordNotation notation = notations.get(i);
			Stem stem = notation.element.getStem();
			
			StemDirection stemDirection = (i == 0 ?
				getFirst(notations) : getLast(notations)).stemDirection;

			//start LP
			float startLP;
			ChordLps lps = notation.notes.getLps();
			if (stemDirection == Up)
				startLP = lps.getBottom();
			else
				startLP = lps.getTop();

			//end LP
			float endLP;
			if (stem.getLength() != null) {
				//use user-defined length
				endLP = startLP + stemDirection.getSign() * 2 * stem.getLength();
			}
			else {
				//compute length
				endLP = startLP + stemDirection.getSign() * 2 * strategy.getMinimumStemLengthIs();
			}

			notation.stem = new StemNotation(startLP, endLP);
		}
		BeamStemAlignments beamstemalignments = new BeamStemAlignments(stemAlignments,
			BeamLines.beamLineWidth, strategy.getDistanceBetweenBeamLinesIs(), beamLinesCount);

		return beamstemalignments;
	}

}
