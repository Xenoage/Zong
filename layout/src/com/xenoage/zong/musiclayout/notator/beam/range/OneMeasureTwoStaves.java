package com.xenoage.zong.musiclayout.notator.beam.range;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.core.position.MP.getMP;

import java.util.List;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.BeamNotation;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.notator.beam.lines.BeamLines;
import com.xenoage.zong.musiclayout.notator.beam.lines.MultipleLines;
import com.xenoage.zong.musiclayout.notator.beam.lines.OneLine;
import com.xenoage.zong.musiclayout.notator.beam.lines.ThreeLines;
import com.xenoage.zong.musiclayout.notator.beam.lines.TwoLines;
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
	
	public static final OneMeasureTwoStaves oneMeasureTwoStaves = new OneMeasureTwoStaves();
	

	@Override public void compute(Beam beam, List<ColumnSpacing> columnSpacings) {
		ColumnSpacing column = columnSpacings.get(getMP(beam.getChord(0)).measure);
		int beamLinesCount = beam.getMaxBeamLinesCount();
		
		//collect chord notations
		List<ChordNotation> chords = alist(beam.size());
		for (BeamWaypoint waypoint : beam.getWaypoints()) {
			Chord chord = waypoint.getChord();
			ChordNotation notation = column.getNotation(chord);
			chords.add(notation);
		}
		
		compute(chords, beamLinesCount);
	}

	/**
	 * Computes and updates the {@link BeamNotation} and {@link StemNotation}s of
	 * the given beamed chords.
	 * 
	 * TODO: change this:
	 * The lengths of the middle stems have to be
	 * recomputed in a later step, since their lengths can not be computed yet.                    
	 */
	public void compute(List<ChordNotation> notations, int beamLinesCount) {
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

		//compute notations
		BeamNotation beamNot = new BeamNotation(BeamLines.beamLineHeightIs,
			strategy.getDistanceBetweenBeamLinesIs(), beamLinesCount);
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
			notation.beam = beamNot;
		}
	}

}
