package com.xenoage.zong.musiclayout.notator.beam.direction;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.core.music.util.Interval.Before;
import static com.xenoage.zong.core.music.util.Interval.BeforeOrAt;
import static com.xenoage.zong.musiclayout.notator.chord.StemDirector.stemDirector;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;

/**
 * {@link Strategy} for a {@link Beam}, which spans over a single staff and measure.
 * 
 * @author Andreas Wenger
 */
public class OneMeasureOneStaff
	extends Strategy {
	
	public static OneMeasureOneStaff oneMeasureOneStaff = new OneMeasureOneStaff();
	

	@Override public StemDirection[] compute(Beam beam, Score score) {
		int staffLinesCount = getStaffLinesCount(beam.getChord(0), score);
		ChordLps[] chordsLps = new ChordLps[beam.size()]; 
		for (int iChord : range(chordsLps)) {
			Chord chord = beam.getChord(iChord);
			MP mp = MP.getMP(chord);
			MusicContext mc = score.getMusicContext(mp, BeforeOrAt, Before);
			chordsLps[iChord] = new ChordLps(chord, mc);
		}
		return compute(chordsLps, staffLinesCount);
	}
		
	StemDirection[] compute(ChordLps[] chordsLps, int staffLinesCount) {
		int staffMiddleLp = staffLinesCount - 1;
		int upCount = 0;
		int downCount = 0;
		int furthestDistance = 0; 
		StemDirection furthestDistanceDir = Up;
		//compute preferred stem directions and remember the stem direction of
		//the chord with the note furthest away from the middle staff line
		for (ChordLps chordLps : chordsLps) {
			StemDirection preferredDir = stemDirector.compute(chordLps, staffLinesCount);
			int distance;
			if (preferredDir == Up) {
				upCount++;
				distance = staffMiddleLp - chordLps.getTop();
			}
			else {
				downCount++;
				distance = chordLps.getBottom() - staffMiddleLp;
			}
			
			if (distance > furthestDistance) {
				furthestDistance = distance;
				furthestDistanceDir = preferredDir;
			}
		}

		//the mostly used stem direction wins.
		//if both directions are equally distributed, the stem direction of
		//the chord with the note furthest away from the staff middle line wins
		StemDirection finalStemDir;
		if (upCount != downCount)
			finalStemDir = (upCount > downCount ? Up : Down);
		else
			finalStemDir = furthestDistanceDir;
		
		//use same direction for all stems
		StemDirection[] dirs = new StemDirection[chordsLps.length];
		for (int i : range(dirs))
			dirs[i] = finalStemDir;

		return dirs;
	}
	
	private int getStaffLinesCount(Chord chord, Score score) {
		MP mp = MP.getMP(chord);
		return score.getStaff(mp).getLinesCount();
	}

}
