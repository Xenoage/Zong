package com.xenoage.zong.musiclayout.notator.beam.direction;

import static com.xenoage.utils.iterators.It.it;

import java.util.Iterator;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.notations.beam.BeamStemDirections;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;
import com.xenoage.zong.musiclayout.notator.Notator;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;

/**
 * {@link Strategy} for a {@link Beam}, which spans over a single staff and measure.
 * 
 * @author Andreas Wenger
 */
public class OneMeasureOneStaff
	extends Strategy {

	@Override public StemDirection[] compute(Beam beam, ChordNotation[] chordsNot, int linesCount) {
		//pre-requirements: beam spans over only one measure (not tested here),
		//and line positions and the stem direction of each chord are known (tested here)
		int chordsCount = beam.getWaypoints().size();
		ChordLps[] chordsLps = new ChordLps[chordsCount];
		StemDirection[] stemDirections = new StemDirection[chordsCount];
		int iChord = 0;
		for (BeamWaypoint waypoint : beam.getWaypoints()) {
			Chord chord = waypoint.getChord();
			ChordNotation cn = chordsNot[iChord];
			if (cn != null)
				chordsLps[iChord] = cn.notes.getLps();
			else
				throw new IllegalStateException("ChordLinePositions unknown for Chord " + iChord);
			if (cn.getStemDirection() != null)
				stemDirections[iChord] = cn.getStemDirection();
			else
				throw new IllegalStateException("Stem.Direction unknown for Chord " + iChord);
			iChord++;
		}

		//do the work
		return compute(chordsLps, stemDirections, linesCount);
	}

	StemDirection[] compute(ChordLps[] chordsLp, StemDirection[] stemDirections, int staffLinesCount) {
		int up = 0;
		int down = 0;
		int furthest = 0;
		int middlelinepos = staffLinesCount - 1;
		int f = 0;

		for (int iChord = 0; iChord < chordsLp.length; iChord++) {
			ChordLps chordLp = chordsLp[iChord];
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

		return dir;
	}

}
