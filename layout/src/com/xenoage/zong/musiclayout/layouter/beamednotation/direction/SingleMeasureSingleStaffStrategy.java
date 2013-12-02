package com.xenoage.zong.musiclayout.layouter.beamednotation.direction;

import static com.xenoage.utils.iterators.It.it;

import java.util.Iterator;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.notations.beam.BeamStemDirections;
import com.xenoage.zong.musiclayout.notations.chord.ChordLinePositions;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;

/**
 * This strategy recomputes the {@link Notation}s of the chords
 * of the given {@link Beam}, which spans over a single staff
 * and measure, but only, if necessary.
 * 
 * @author Andreas Wenger
 */
public class SingleMeasureSingleStaffStrategy
	implements ScoreLayouterStrategy {

	//used strategies
	private final NotationStrategy notationStrategy;


	public SingleMeasureSingleStaffStrategy(NotationStrategy notationStrategy) {
		this.notationStrategy = notationStrategy;
	}

	/**
	 * Returns better {@link Notation}s of the chords connected by the given beam within
	 * the given staff and measure, using the given notations and number of lines in this measure.
	 * Only changed notations are returned.
	 */
	public NotationsCache computeNotations(Beam beam, NotationsCache notations, int linesCount,
		Score score, LayoutSettings layoutSettings) {
		//pre-requirements: beam spans over only one measure (not tested here),
		//and line positions and the stem direction of each chord are known (tested here)
		int chordsCount = beam.getWaypoints().size();
		ChordLinePositions[] chordsLp = new ChordLinePositions[chordsCount];
		StemDirection[] stemDirections = new StemDirection[chordsCount];
		int iChord = 0;
		for (BeamWaypoint waypoint : beam.getWaypoints()) {
			Chord chord = waypoint.getChord();
			ChordNotation cn = notations.getChord(chord);
			if (cn != null && cn.getNotesAlignment() != null)
				chordsLp[iChord] = cn.getNotesAlignment().getLinePositions();
			else
				throw new IllegalStateException("ChordLinePositions unknown for Chord " + iChord);
			if (cn.getStemDirection() != null)
				stemDirections[iChord] = cn.getStemDirection();
			else
				throw new IllegalStateException("Stem.Direction unknown for Chord " + iChord);
			iChord++;
		}

		//do the work
		BeamStemDirections bsd = computeBeamStemDirections(chordsLp, stemDirections, linesCount);

		//return the results as a new NotationsCache
		NotationsCache ret = new NotationsCache();
		Iterator<BeamWaypoint> beamWaypoints = it(beam.getWaypoints());
		for (int i = 0; i < bsd.getStemDirections().length; i++) {
			Chord chord = beamWaypoints.next().getChord();
			ChordNotation oldChordNotation = notations.getChord(chord);
			StemDirection oldStemDir = oldChordNotation.getStemDirection();
			//if stem direction was changed, recompute the notation.
			//the stem lengths are not fitted to the beam already, so this is
			//has to be done later within another strategy
			if (bsd.getStemDirections()[i] != oldStemDir) {
				ret.add(notationStrategy.computeChord(chord, bsd.getStemDirections()[i], score,
					layoutSettings));
			}
		}
		return ret;
	}

	/**
	 * Computes the directions of the stems of beamed chords within a single measure.
	 * @param chordsLp        the line positions of every chord
	 * @param stemDirections  the precomputed directions of the stems (as if they were single, unbeamed chords)
	 * @param linesCount      the number of lines in this staff
	 */
	BeamStemDirections computeBeamStemDirections(ChordLinePositions[] chordsLp,
		StemDirection[] stemDirections, int linesCount) {
		int up = 0;
		int down = 0;
		int furthest = 0;
		int middlelinepos = linesCount - 1;
		int f = 0;

		for (int iChord = 0; iChord < chordsLp.length; iChord++) {
			ChordLinePositions chordLp = chordsLp[iChord];
			StemDirection direction = stemDirections[iChord];
			if (direction == StemDirection.Up) {
				up++;
				if (Math.abs(middlelinepos - chordLp.getTop()) > Math.abs(middlelinepos -
					chordsLp[furthest].get(chordsLp[furthest].getNotesCount() - 1))) {
					furthest = f;
				}
			}
			else if (direction == StemDirection.Down) {
				down++;
				if (Math.abs(middlelinepos - chordLp.getBottom()) > Math.abs(middlelinepos -
					chordsLp[furthest].get(chordsLp[furthest].getNotesCount() - 1))) {
					furthest = f;
				}
			}
			f++;
		}

		StemDirection[] dir = new StemDirection[chordsLp.length];
		if (up == down) {
			if (stemDirections[furthest] == StemDirection.Up) {
				up++;
			}
			else {
				down++;
			}
		}
		if (up > down) {
			//All stems up
			for (int i = 0; i < dir.length; i++) {
				dir[i] = StemDirection.Up;
			}
		}
		else {
			//All stems down
			for (int i = 0; i < dir.length; i++) {
				dir[i] = StemDirection.Down;
			}
		}

		return new BeamStemDirections(dir);
	}

}
