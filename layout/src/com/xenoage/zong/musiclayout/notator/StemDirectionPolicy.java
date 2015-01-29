package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._1;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Stem;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;

/**
 * Rules for the stem direction of single chords.
 * 
 * @author Andreas Wenger
 */
public final class StemDirectionPolicy {
	
	public static StemDirectionPolicy stemDirectionPolicy = new StemDirectionPolicy();

	
	public StemDirection computeStemDirection(Chord chord, MusicContext context) {
		//stem specified in chord?
		Stem stem = chord.getStem();
		if (stem.getDirection() != StemDirection.Default)
			return stem.getDirection();
		
		//stem needed?
		if (!isStemNeeded(chord))
			return StemDirection.None;
	
		//compute default stem
		ChordLps chordLp = new ChordLps(chord, context);
		int linesCount = context.getLinesCount();
		return computeStemDirection(chordLp, linesCount);
	}

	
	public StemDirection computeStemDirection(ChordLps chordLps, int staffLinesCount) {
		//compute distance to middle line of staff
		int distanceLowest = Math.abs(getDistanceToMidline(chordLps.getBottom(), staffLinesCount));
		int distanceHighest = Math.abs(getDistanceToMidline(chordLps.getTop(), staffLinesCount));
		
		//the side with the longest distance determines the direction
		if (distanceLowest > distanceHighest) {
			return StemDirection.Up;
		}
		else if (distanceLowest < distanceHighest) {
			return StemDirection.Down;
		}
		
		//the distance is equal on both sides
		//if the majority of notes on the stem are above the middleline, 
		//a down stem is used, otherwise an up-stem
		int above = 0;
		int below = 0;
		for (int i : range(chordLps.getNotesCount())) {
			int d = getDistanceToMidline(chordLps.get(i), staffLinesCount);
			if (d > 0)
				above++;
			else if (d < 0)
				below++;
		}
		if (below > above)
			return StemDirection.Up;
		else
			return StemDirection.Down;
	}

	private boolean isStemNeeded(Chord chord) {
		//all chords shorter than 1/1 need a stem
		return chord.getDuration().isLessThan(_1);
	}

	private int getDistanceToMidline(int lp, int staffLinesCount) {
		int middleLp = staffLinesCount - 1;
		return lp - middleLp;
	}

}
