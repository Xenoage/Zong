package com.xenoage.zong.musiclayout.layouter.notation;

import java.util.ArrayList;

import com.xenoage.utils.collections.ArrayUtils;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.notations.chord.ChordLinePositions;
import com.xenoage.zong.musiclayout.notations.chord.NoteAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NoteSuspension;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * This strategy computes the alignment of the notes
 * and the dots of a given chord.
 * 
 * The rules are adepted from
 * "Chlapik: Die Praxis des Notengraphikers", page 40.
 * 
 * The dot placing rules are based on Sibelius 1.4.
 * 
 * @author Andreas Wenger
 * @author Uli Teschemacher
 */
public final class NotesAlignmentStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Classes of chords, dependent on stem direction and unison/second interval.
	 */
	private enum ChordClass {
		/** Stem down without unison/second interval. */
		StemDownNoUni,
		/** Stem down with unison/second interval. */
		StemDownUni,
		/** Stem up without unison/second interval. */
		StemUpNoUni,
		/** Stem up with unison/second interval. */
		StemUpUni
	}


	/**
	 * Computes the alignment of the notes of the given chord, which has a stem into
	 * the given direction, using the given musical context.
	 */
	public NotesAlignment computeNotesAlignment(Chord chord, StemDirection stemDirection,
		ChordWidths chordWidths, MusicContext musicContext) {
		ChordLinePositions lp = new ChordLinePositions(chord, musicContext);
		ChordClass chordClass = computeChordClass(lp, stemDirection);

		float noteheadWidth = chordWidths.get(chord.getDisplayedDuration());
		float stemOffset = computeStemOffset(chordClass, noteheadWidth);
		float notesWidth = computeNotesWidth(chordClass, noteheadWidth);
		NoteAlignment[] notes = computeNotesAlignment(lp, stemDirection, stemOffset);
		int dotsCount = computeDotsCount(chord.getDuration());
		float[] dotsOffsets = computeDotsOffsets(notesWidth, dotsCount, chordWidths);
		int[] dots = (dotsCount > 0 ? computeDots(lp) : new int[0]);

		//are there left-suspended notes?
		float leftSuspendedWidth = 0;
		for (NoteAlignment note : notes) {
			if (note.getSuspension() == NoteSuspension.Left) {
				//yes, there is at least one left-suspended note
				leftSuspendedWidth = noteheadWidth;
				break;
			}
		}

		float totalWidth;
		if (dotsCount > 0)
			totalWidth = dotsOffsets[dotsCount - 1];
		else
			totalWidth = notesWidth;

		return new NotesAlignment(totalWidth, notes, dotsOffsets, dots, stemOffset, leftSuspendedWidth);
	}

	/**
	 * Computes the {@link ChordClass} of the given chord.
	 */
	private ChordClass computeChordClass(ChordLinePositions lp, StemDirection stemDirection) {
		int chordClass = (stemDirection == StemDirection.Up) ? 3 : 1;
		for (int i = 1; i < lp.getNotesCount(); i++) {
			if (Math.abs(lp.get(i) - lp.get(i - 1)) <= 1) {
				chordClass++;
				break;
			}
		}
		switch (chordClass) {
			case 1:
				return ChordClass.StemDownNoUni;
			case 2:
				return ChordClass.StemDownUni;
			case 3:
				return ChordClass.StemUpNoUni;
			default:
				return ChordClass.StemUpUni;
		}
	}

	/**
	 * Computes the offset of the stem.
	 */
	private float computeStemOffset(ChordClass chordClass, float noteheadWidth) {
		//the stem offsets are:
		// 0 for StemDownNoUni
		// notehead width for all other classes
		if (chordClass == ChordClass.StemDownNoUni)
			return 0;
		else
			return noteheadWidth;
	}

	/**
	 * Computes the width of the notes of the chord.
	 */
	private float computeNotesWidth(ChordClass chordClass, float noteheadWidth) {
		//the widths are:
		// NoUni: notehead width
		// Uni: 2 * notehead width
		if (chordClass == ChordClass.StemDownNoUni || chordClass == ChordClass.StemUpNoUni)
			return noteheadWidth;
		else
			return 2 * noteheadWidth;
	}

	/**
	 * Computes and sets the note alignments,
	 * the stem offset and the width.
	 */
	private NoteAlignment[] computeNotesAlignment(ChordLinePositions lp, StemDirection sd,
		float stemOffset) {
		NoteAlignment[] notes = new NoteAlignment[lp.getNotesCount()];

		//if stem direction is down or none, begin with the highest note,
		//otherwise with the lowest
		int dir = (sd == StemDirection.Up) ? 1 : -1;
		int startIndex = (dir == 1) ? 0 : lp.getNotesCount() - 1;
		int endIndex = lp.getNotesCount() - 1 - startIndex;
		//default side of the stem. 1 = right, 0 = left
		int stemSide = (sd == StemDirection.Up) ? 1 : 0;
		int lastSide = stemSide;
		for (int i = startIndex; dir * i <= dir * endIndex; i += dir) {
			int side = 1 - stemSide;
			NoteSuspension suspension = NoteSuspension.None;
			if (i == startIndex) {
				//first note: use default side
			}
			else {
				//following note: change side, if last note is 1 or 0 steps away
				if (Math.abs(lp.get(i) - lp.get(i - dir)) <= 1) {
					//change side
					side = 1 - lastSide;
					if (side != 1 - stemSide) {
						if (side == 0)
							suspension = NoteSuspension.Left;
						else
							suspension = NoteSuspension.Right;
					}
				}
			}
			notes[i] = new NoteAlignment(lp.get(i), side * stemOffset, suspension);
			lastSide = side;
		}

		return notes;
	}

	/**
	 * Computes the number of prolongation dots, e.g. 2 if it is
	 * a half+quarter+eighth note (a half with two dots).
	 */
	private int computeDotsCount(Fraction duration) {
		int num = duration.getNumerator();
		if (num == 3) //if numerator == 3, we have one dot
			return 1;
		else if (num == 7) //if numerator == 7, we have two dots
			return 2;
		else
			//otherwise we have no dots
			return 0;
	}

	/**
	 * Computes the offsets of the dots.
	 */
	private float[] computeDotsOffsets(float notesWidth, int dotsCount, ChordWidths chordWidths) {
		float[] ret = new float[dotsCount];
		for (int i : Range.range(dotsCount))
			ret[i] = notesWidth + chordWidths.dotGap + i * chordWidths.dot;
		return ret;
	}

	/**
	 * Computes the position of the dots of the chord.
	 */
	private int[] computeDots(ChordLinePositions lp) {
		//compute the position of the dots
		ArrayList<Integer> dots = new ArrayList<Integer>();
		ArrayList<Integer> dotLinePos = new ArrayList<Integer>();
		for (int i = lp.getNotesCount() - 1; i >= 0; i--) {
			int dot = lp.get(i);
			//place dot between (leger) lines, not on them
			if (dot % 2 == 0)
				dot++;
			//add dot
			if (!dots.contains(dot) && !dotLinePos.contains(lp.get(i))) {
				dots.add(0, dot);
				dotLinePos.add(lp.get(i));
			}
			else {
				//there is already a dot. try to place it 2 positions lower,
				//but only if the there is not already a dot for this pitch
				if (!dots.contains(dot - 2) && !dotLinePos.contains(lp.get(i))) {
					dots.add(0, dot - 2);
				}
			}
		}
		return ArrayUtils.toIntArray(dots);
	}

}
