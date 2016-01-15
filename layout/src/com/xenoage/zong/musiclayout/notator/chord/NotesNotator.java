package com.xenoage.zong.musiclayout.notator.chord;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.rangeReverse;

import java.util.ArrayList;

import com.xenoage.utils.collections.ArrayUtils;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.chord.ChordLps;
import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notation.chord.NoteSuspension;
import com.xenoage.zong.musiclayout.notation.chord.NotesNotation;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Computes the notation of the notes and the dots of a given chord.
 * 
 * The rules are adapted from "Chlapik: Die Praxis des Notengraphikers", page 40.
 * The dot placing rules are based on Sibelius 1.4.
 * 
 * @author Andreas Wenger
 */
public class NotesNotator {
	
	public static final NotesNotator notesNotator = new NotesNotator();
	
	private final int[] emptyIntArray = {};
	private final float[] emptyFloatArray = {};
	

	/**
	 * Classes of chords, dependent on stem direction and unison/second interval.
	 */
	private enum ChordClass {
		/** Stem down without unison/second interval. */
		StemDown,
		/** Stem down with unison/second interval. */
		StemDownSuspended,
		/** Stem up without unison/second interval. */
		StemUp,
		/** Stem up with unison/second interval. */
		StemUpSuspended
	}


	/**
	 * Computes the displacement of the notes of the given chord, which has a stem into
	 * the given direction, using the given musical context.
	 */
	public NotesNotation compute(Chord chord, StemDirection stemDirection,
		ChordWidths chordWidths, MusicContext musicContext) {
		
		ChordLps lp = new ChordLps(chord, musicContext);
		ChordClass chordClass = computeChordClass(lp, stemDirection);

		float noteheadWidth = chordWidths.get(chord.getDisplayedDuration());
		float stemOffset = computeStemOffset(chordClass, noteheadWidth);
		float notesWidth = computeNotesWidth(chordClass, noteheadWidth);
		NoteDisplacement[] notes = computeNotes(lp, stemDirection, stemOffset);
		int dotsCount = computeDotsCount(chord.getDuration());
		float[] dotsOffsets = computeDotsOffsets(notesWidth, dotsCount, chordWidths);
		int[] dotsLp = (dotsCount > 0 ? computeDotsLp(lp) : emptyIntArray);
		boolean leftSuspended = isLeftSuspended(notes);

		float totalWidth;
		if (dotsCount > 0)
			totalWidth = dotsOffsets[dotsCount - 1];
		else
			totalWidth = notesWidth;

		return new NotesNotation(totalWidth, noteheadWidth, notes, dotsOffsets,
			dotsLp, stemOffset, leftSuspended);
	}

	private ChordClass computeChordClass(ChordLps lps, StemDirection stemDirection) {
		ChordClass chordClass = (stemDirection == StemDirection.Up) ? ChordClass.StemUp : ChordClass.StemDown;
		for (int i = 1; i < lps.getNotesCount(); i++)
			if (lps.get(i) - lps.get(i - 1) <= 1)
				return ChordClass.values()[chordClass.ordinal() + 1];
		return chordClass;
	}

	private float computeStemOffset(ChordClass chordClass, float noteheadWidth) {
		if (chordClass == ChordClass.StemDown)
			return 0;
		else
			return noteheadWidth;
	}

	private float computeNotesWidth(ChordClass chordClass, float noteheadWidth) {
		if (chordClass == ChordClass.StemDown || chordClass == ChordClass.StemUp)
			return noteheadWidth;
		else
			return 2 * noteheadWidth;
	}

	private NoteDisplacement[] computeNotes(ChordLps lps, StemDirection sd, float stemOffset) {
		NoteDisplacement[] notes = new NoteDisplacement[lps.getNotesCount()];

		//if stem direction is down or none, begin with the highest note,
		//otherwise with the lowest
		int dir = (sd == StemDirection.Up) ? 1 : -1;
		int startIndex = (dir == 1) ? 0 : lps.getNotesCount() - 1;
		int endIndex = lps.getNotesCount() - 1 - startIndex;
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
				//following note: change side, if last note is 1 or 0 LPs away
				if (lps.get(i) - lps.get(i - dir) <= 1) {
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
			notes[i] = new NoteDisplacement(lps.get(i), side * stemOffset, suspension);
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
		if (num == 3)
			return 1;
		else if (num == 7)
			return 2;
		else
			return 0;
	}

	private float[] computeDotsOffsets(float notesWidth, int dotsCount, ChordWidths chordWidths) {
		if (dotsCount == 0)
			return emptyFloatArray;
		float[] ret = new float[dotsCount];
		for (int i : Range.range(dotsCount))
			ret[i] = notesWidth + chordWidths.dotGap + i * chordWidths.dot;
		return ret;
	}

	private int[] computeDotsLp(ChordLps lps) {
		ArrayList<Integer> dotsLp = alist(lps.getNotesCount());
		int lastDotLp = Integer.MAX_VALUE;
		for (int i : rangeReverse(lps.getNotesCount())) {
			int lp = lps.get(i);
			//place dot between (leger) lines, not on them. use the space above.
			int dotLp = lp - (lp % 2) + 1;
			if (dotLp < lastDotLp) {
				//space is free, use it
				dotsLp.add(0, dotLp);
				lastDotLp = dotLp;
			}
			else {
				//there is already a dot. if we are on a line, use the space below, if it is free
				if (lp % 2 == 0 && lp - 1 < lastDotLp) {
					dotLp = lp - 1;
					dotsLp.add(0, dotLp);
					lastDotLp = dotLp;
				}
			}
		}
		return ArrayUtils.toIntArray(dotsLp);
	}
	
	private boolean isLeftSuspended(NoteDisplacement[] notes) {
		for (NoteDisplacement note : notes)
			if (note.suspension == NoteSuspension.Left)
				return true;
		return false;
	}

}
