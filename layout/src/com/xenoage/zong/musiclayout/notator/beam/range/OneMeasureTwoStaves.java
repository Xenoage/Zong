package com.xenoage.zong.musiclayout.notator.beam.range;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.collections.CollectionUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.geom.Point2f.p;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.core.position.MP.getMP;

import java.util.List;

import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.position.MP;
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
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.ScoreSpacing;

/**
 * {@link Strategy} for a {@link Beam}, which spans over two adjacent staves
 * in a single measure column.
 * 
 * The strategy is quite simple: The first and the last stem have the default lengths
 * (or the lengths the user has defined). All middle stem lengths are interpolated
 * so that they end at the outer beam line.
 * 
 * @author Andreas Wenger
 */
public class OneMeasureTwoStaves
	implements Strategy {
	
	public static final OneMeasureTwoStaves oneMeasureTwoStaves = new OneMeasureTwoStaves();
	

	@Override public void compute(Beam beam, ScoreSpacing scoreSpacing) {
		ColumnSpacing column = scoreSpacing.getColumn(getMP(beam.getChord(0)).measure);
		int beamLinesCount = beam.getMaxBeamLinesCount();
		
		//collect chord notations
		List<ChordNotation> chords = alist(beam.size());
		for (BeamWaypoint waypoint : beam.getWaypoints()) {
			Chord chord = waypoint.getChord();
			ChordNotation notation = column.getNotation(chord);
			chords.add(notation);
		}
		
		compute(chords, beamLinesCount, column);
	}

	/**
	 * Computes and updates the {@link BeamNotation} and {@link StemNotation}s of
	 * the given beamed chords.
	 * 
	 * TODO: change this:
	 * The lengths of the middle stems have to be
	 * recomputed in a later step, since their lengths can not be computed yet.                    
	 */
	public void compute(List<ChordNotation> notations, int beamLinesCount, ColumnSpacing column) {
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

		//compute notations of the first and last stem
		BeamNotation beamNot = new BeamNotation(BeamLines.beamLineHeightIs,
			strategy.getDistanceBetweenBeamLinesIs(), beamLinesCount);
		Point2f leftStemPosMm = null, rightStemPosMm = null;
		for (int i : range(2)) {
			ChordNotation chord = (i == 0 ? getFirst(notations) : getLast(notations));
			int staff = getMP(chord.element).staff;
			float is = column.getMeasures().get(staff).getInterlineSpace();
			Stem stem = chord.element.getStem();
			StemDirection stemDirection = chord.stemDirection;

			//start LP
			float startLP;
			ChordLps lps = chord.notes.getLps();
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
			
			//remember horizontal stem position
			ElementSpacing chordSpacing = column.getElement(chord.element);
			float stemXMm = (chordSpacing.offsetIs + chord.getStemOffsetIs()) * is;
			float stemYMm = column.parentSystem.getYMm(staff, endLP);
			Point2f stemPosMm = p(stemXMm, stemYMm);
			if (i == 0)
				leftStemPosMm = stemPosMm;
			else
				rightStemPosMm = stemPosMm;

			chord.stem = new StemNotation(startLP, endLP);
			chord.beam = beamNot;
		}
		
		for (int i : range(1, notations.size() - 2)) {
			ChordNotation chord = notations.get(i);
			ElementSpacing chordSpacing = column.getElement(chord.element);
			MP chordMp = getMP(chord.element);
			float is = column.getMeasures().get(chordMp.staff).getInterlineSpace();
			float stemXMm = (chordSpacing.offsetIs + chord.getStemOffsetIs()) * is;
			
			float f = (stemXMm - leftStemPosMm.x) / (rightStemPosMm.x - leftStemPosMm.x);
			float endLp = 0;

			//two staff beam: we have first to translate
			//the beam in absolute frame coordinates, then we have to translate it into the
			//coordinates of the parent staff of the current stem
			float endMm = leftStemPosMm.y + f * (rightStemPosMm.y - leftStemPosMm.y);
			endLp = column.parentSystem.getYLp(chordMp.staff, endMm);
			
			chord.stem = new StemNotation(chord.stem.startLp, endLp);
			chord.beam = beamNot;
		}
	}

}
