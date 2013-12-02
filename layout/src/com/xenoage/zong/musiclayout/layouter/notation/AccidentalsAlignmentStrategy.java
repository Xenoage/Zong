package com.xenoage.zong.musiclayout.layouter.notation;

import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalAlignment;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NoteAlignment;
import com.xenoage.zong.musiclayout.notations.chord.NotesAlignment;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * This strategy stores the alignment of
 * the accidentals of the given chord.
 * 
 * @author Andreas Wenger
 */
public class AccidentalsAlignmentStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Computes the alignment of the accidentals of the given chord,
	 * using the given note alignments and the given musical context,
	 * and returns it. If there are no accidentals, null is returned.
	 */
	public AccidentalsAlignment computeAccidentalsAlignment(Chord chord,
		NotesAlignment notesAlignment, ChordWidths chordWidths, MusicContext mc) {
		return computeAccidentalsAlignment(chord.getPitches(), notesAlignment.getNoteAlignments(),
			chordWidths, mc);
	}

	/**
	 * Creates a new {@link AccidentalsAlignment} for the given notes,
	 * or null if there are no accidentals.
	 */
	AccidentalsAlignment computeAccidentalsAlignment(List<Pitch> pitches, NoteAlignment[] alignments,
		ChordWidths chordWidths, MusicContext mc) {
		//count number of needed accidentals
		int accCount = 0;
		for (Pitch pitch : pitches) {
			Accidental acc = mc.getAccidental(pitch);
			if (acc != null)
				accCount++;
		}
		//there are different alignment algorithms for each number
		if (accCount == 1)
			return computeAlignment1Accidental(pitches, alignments, chordWidths, mc);
		else if (accCount == 2)
			return computeAlignment2Accidentals(pitches, alignments, chordWidths, mc);
		else if (accCount == 3)
			return computeAlignment3Accidentals(pitches, alignments, chordWidths, mc);
		else if (accCount > 3)
			//chords with more than 3 accidentals have no good layout yet!
			return computeAlignmentNAccidentals(pitches, alignments, chordWidths, mc);
		else
			return null;
	}

	/**
	 * Computes the accidentals alignment for a chord
	 * with one accidental.
	 */
	private AccidentalsAlignment computeAlignment1Accidental(List<Pitch> pitches,
		NoteAlignment[] alignments, ChordWidths chordWidths, MusicContext mc) {
		for (int i = 0; i < pitches.size(); i++) {
			Accidental at = mc.getAccidental(pitches.get(i));
			if (at != null) {
				AccidentalAlignment[] a = { new AccidentalAlignment(alignments[i].getLinePosition(), 0, at) };
				float width = chordWidths.get(at) + chordWidths.accToNoteGap;
				return new AccidentalsAlignment(a, width);
			}
		}
		throw new IllegalArgumentException("Invalid chord");
	}

	/**
	 * Computes the accidentals alignment for a chord
	 * with two accidentals.
	 */
	private AccidentalsAlignment computeAlignment2Accidentals(List<Pitch> pitches,
		NoteAlignment[] alignments, ChordWidths chordWidths, MusicContext context) {
		//compute index of top and bottom note with accidental
		boolean[] checklist = computeAccidentalsChecklist(pitches, context);
		int topNoteIndex = computeLastTrueEntryIndex(checklist);
		checklist[topNoteIndex] = false;
		int bottomNoteIndex = computeLastTrueEntryIndex(checklist);
		//compute accidental types
		Accidental atTop = context.getAccidental(pitches.get(topNoteIndex));
		Accidental atBottom = context.getAccidental(pitches.get(bottomNoteIndex));
		//interval of at least a seventh?
		int distance = alignments[topNoteIndex].getLinePosition() -
			alignments[bottomNoteIndex].getLinePosition();
		AccidentalAlignment[] a;
		float width;
		if (distance >= 6) {
			//placed on the same horizontal position
			a = new AccidentalAlignment[] {
				new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(), 0, atBottom),
				new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(), 0, atTop) };
			width = chordWidths.getMaxWidth(atBottom, atTop) + chordWidths.accToNoteGap;
		}
		else {
			//placed on different horizontal positions
			//normally begin with the bottom accidental
			boolean bottomFirst = true;
			//when the bottom accidental note is suspended
			//on the right side of the stem, and the top accidental note
			//is not, then the bottom accidental
			//is nearer to the note (see Ross, p. 132)
			if (alignments[bottomNoteIndex].getOffset() > alignments[topNoteIndex].getOffset()) {
				bottomFirst = false;
			}
			if (bottomFirst) {
				//begin with bottom note
				a = new AccidentalAlignment[] {
					new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(), 0, atBottom),
					new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(),
						chordWidths.get(atBottom) + chordWidths.accToAccGap, atTop) };
			}
			else {
				//begin with top note
				a = new AccidentalAlignment[] {
					new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(),
						chordWidths.get(atTop) + chordWidths.accToAccGap, atBottom),
					new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(), 0f, atTop) };
			}
			//compute width
			width = chordWidths.get(atBottom) + chordWidths.accToAccGap + chordWidths.get(atTop) +
				chordWidths.accToNoteGap;
		}
		return new AccidentalsAlignment(a, width);
	}

	/**
	 * Computes the accidentals alignment for a chord with three accidentals.
	 * The 6 rules are adepted from Ross, page 132 f.
	 */
	private AccidentalsAlignment computeAlignment3Accidentals(List<Pitch> pitches,
		NoteAlignment[] alignments, ChordWidths chordWidths, MusicContext context) {
		//compute index of top, middle and bottom note with accidental
		boolean[] checklist = computeAccidentalsChecklist(pitches, context);
		int topNoteIndex = computeLastTrueEntryIndex(checklist);
		checklist[topNoteIndex] = false;
		int middleNoteIndex = computeLastTrueEntryIndex(checklist);
		checklist[middleNoteIndex] = false;
		int bottomNoteIndex = computeLastTrueEntryIndex(checklist);
		//compute accidental types
		Accidental atTop = context.getAccidental(pitches.get(topNoteIndex));
		Accidental atMiddle = context.getAccidental(pitches.get(middleNoteIndex));
		Accidental atBottom = context.getAccidental(pitches.get(bottomNoteIndex));
		//interval of at least a seventh?
		int distance = alignments[topNoteIndex].getLinePosition() -
			alignments[bottomNoteIndex].getLinePosition();
		AccidentalAlignment[] a = new AccidentalAlignment[3];
		float width;
		if (distance >= 6) {
			//interval of at least a seventh. can be rule 1, 3 or 4
			if (alignments[topNoteIndex].getOffset() > 0f) {
				//top note is suspended on the right side of the stem.
				//this is rule 4. (same code as rule 1)
				a[1] = new AccidentalAlignment(alignments[middleNoteIndex].getLinePosition(), 0f, atMiddle);
				float middleWidth = chordWidths.get(atMiddle);
				a[0] = new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(), middleWidth +
					chordWidths.accToAccGap, atBottom);
				a[2] = new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(), middleWidth +
					chordWidths.accToAccGap, atTop);
				width = middleWidth + chordWidths.accToAccGap + chordWidths.getMaxWidth(atBottom, atTop) +
					chordWidths.accToNoteGap;
			}
			else if (alignments[middleNoteIndex].getOffset() > 0f) {
				//middle note is suspended on the right side of the stem.
				//(bottom note is never suspended on the right) (TODO: really?)
				//this is rule 3.
				a[0] = new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(), 0f, atBottom);
				float bottomWidth = chordWidths.get(atBottom);
				a[2] = new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(), bottomWidth +
					chordWidths.accToAccGap, atTop);
				float topWidth = chordWidths.get(atTop);
				a[1] = new AccidentalAlignment(alignments[middleNoteIndex].getLinePosition(), bottomWidth +
					chordWidths.accToAccGap + topWidth + chordWidths.accToAccGap, atMiddle);
				width = bottomWidth + chordWidths.accToAccGap + topWidth + chordWidths.accToAccGap +
					chordWidths.get(atMiddle) + chordWidths.accToNoteGap;
			}
			else {
				//there are no accidental notes suspended on the right side of the stem.
				//this is rule 1.
				a[1] = new AccidentalAlignment(alignments[middleNoteIndex].getLinePosition(), 0f, atMiddle);
				float middleWidth = chordWidths.get(atMiddle);
				a[0] = new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(), middleWidth +
					chordWidths.accToAccGap, atBottom);
				a[2] = new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(), middleWidth +
					chordWidths.accToAccGap, atTop);
				width = middleWidth + chordWidths.accToAccGap + chordWidths.getMaxWidth(atBottom, atTop) +
					chordWidths.accToNoteGap;
			}
		}
		else {
			//interval of less than a seventh. can be rule 2, 5 or 6
			if (alignments[topNoteIndex].getOffset() > 0f) {
				//top note is suspended on the right side of the stem.
				//this is rule 5. (same code as rule 2)
				a[1] = new AccidentalAlignment(alignments[middleNoteIndex].getLinePosition(), 0f, atMiddle);
				float middleWidth = chordWidths.get(atMiddle);
				a[0] = new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(), middleWidth +
					chordWidths.accToAccGap, atBottom);
				float bottomWidth = chordWidths.get(atBottom);
				a[2] = new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(), middleWidth +
					chordWidths.accToAccGap + bottomWidth + chordWidths.accToAccGap, atTop);
				float topWidth = chordWidths.get(atTop);
				width = middleWidth + chordWidths.accToAccGap + bottomWidth + chordWidths.accToAccGap +
					topWidth + chordWidths.accToNoteGap;
			}
			else if (alignments[middleNoteIndex].getOffset() > 0f) {
				//middle note is suspended on the right side of the stem.
				//(bottom note is never suspended on the right)
				//this is rule 6. (same code as rule 3)
				a[0] = new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(), 0f, atBottom);
				float bottomWidth = chordWidths.get(atBottom);
				a[2] = new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(), bottomWidth +
					chordWidths.accToAccGap, atTop);
				float topWidth = chordWidths.get(atTop);
				a[1] = new AccidentalAlignment(alignments[middleNoteIndex].getLinePosition(), bottomWidth +
					chordWidths.accToAccGap + topWidth + chordWidths.accToAccGap, atMiddle);
				width = bottomWidth + chordWidths.accToAccGap + topWidth + chordWidths.accToAccGap +
					chordWidths.get(atMiddle) + chordWidths.accToNoteGap;
			}
			else {
				//there are no accidental notes suspended on the right side of the stem.
				//this is rule 2.
				a[1] = new AccidentalAlignment(alignments[middleNoteIndex].getLinePosition(), 0f, atMiddle);
				float middleWidth = chordWidths.get(atMiddle);
				a[0] = new AccidentalAlignment(alignments[bottomNoteIndex].getLinePosition(), middleWidth +
					chordWidths.accToAccGap, atBottom);
				float bottomWidth = chordWidths.get(atBottom);
				a[2] = new AccidentalAlignment(alignments[topNoteIndex].getLinePosition(), middleWidth +
					chordWidths.accToAccGap + bottomWidth + chordWidths.accToAccGap, atTop);
				float topWidth = chordWidths.get(atTop);
				width = middleWidth + chordWidths.accToAccGap + bottomWidth + chordWidths.accToAccGap +
					topWidth + chordWidths.accToNoteGap;
			}
		}
		return new AccidentalsAlignment(a, width);
	}

	/**
	 * Computes the accidentals alignment for a chord
	 * with any number of accidental. Currently this returns no good layout,
	 * since all accidentals are within a single column.
	 */
	private AccidentalsAlignment computeAlignmentNAccidentals(List<Pitch> pitches,
		NoteAlignment[] alignments, ChordWidths chordWidths, MusicContext mc) {
		ArrayList<AccidentalAlignment> al = alist();
		float width = 0;
		for (int i = 0; i < pitches.size(); i++) {
			Accidental at = mc.getAccidental(pitches.get(i));
			if (at != null) {
				al.add(new AccidentalAlignment(alignments[i].getLinePosition(), 0, at));
				width = Math.max(width, chordWidths.get(at) + chordWidths.accToNoteGap);
			}
		}
		return new AccidentalsAlignment(al.toArray(new AccidentalAlignment[al.size()]), width);
	}

	/**
	 * Computes and returns a boolean array for the given
	 * notes, with a true indicating that a accidental is needed
	 * for this note, and a false indicating that no accidental is needed.
	 */
	private boolean[] computeAccidentalsChecklist(List<Pitch> pitches, MusicContext context) {
		boolean[] ret = new boolean[pitches.size()];
		for (int i = 0; i < pitches.size(); i++) {
			ret[i] = (context.getAccidental(pitches.get(i)) != null);
		}
		return ret;
	}

	/**
	 * Computes the index of the last field containing
	 * true in the given array, or -1 if there is none.
	 */
	private int computeLastTrueEntryIndex(boolean[] checklist) {
		for (int i = checklist.length - 1; i >= 0; i--) {
			if (checklist[i])
				return i;
		}
		return -1;
	}

}
