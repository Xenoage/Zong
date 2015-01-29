package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.Right;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.ChordDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notator.accidentals.OneAccidental;
import com.xenoage.zong.musiclayout.notator.accidentals.Strategy;
import com.xenoage.zong.musiclayout.notator.accidentals.TwoAccidentals;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Computes the displacements of the accidentals of a given chord.
 * 
 * Some rules are adepted from
 * "Ross: The Art of Music Engraving", page 130 ff, and
 * "Chlapik: Die Praxis des Notengraphikers", page 48 ff.
 * 
 * @author Andreas Wenger
 */
public class AccidentalsDisplacementPolicy {
	
	public static final AccidentalsDisplacementPolicy accidentalsDisplacementPolicy =
		new AccidentalsDisplacementPolicy();
	
	private OneAccidental oneAccidental = new OneAccidental();
	private TwoAccidentals twoAccidentals = new TwoAccidentals();

	
	/**
	 * Computes the alignment of the accidentals of the given chord.
	 * If there are no accidentals, {@link AccidentalsDisplacement#empty} is returned.
	 */
	public AccidentalsDisplacement computeAccidentalsAlignment(Chord chord,
		ChordDisplacement notesAlignment, ChordWidths chordWidths, MusicContext mc) {
		return computeAccidentalsAlignment(chord.getPitches(), notesAlignment.notes,
			chordWidths, mc);
	}

	AccidentalsDisplacement computeAccidentalsAlignment(List<Pitch> pitches, NoteDisplacement[] notes,
		ChordWidths chordWidths, MusicContext mc) {
		//count number of needed accidentals
		int accCount = countAccidentals(pitches, mc);
		//there are different alignment algorithms for each number
		if (accCount == 0)
			return AccidentalsDisplacement.empty;
		else
			return getStrategy(accCount).computeAccidentalsDisplacement(pitches, notes, chordWidths, mc);
	}

	private int countAccidentals(List<Pitch> pitches, MusicContext mc) {
		int accCount = 0;
		for (Pitch pitch : pitches) {
			Accidental acc = mc.getAccidental(pitch);
			if (acc != null)
				accCount++;
		}
		return accCount;
	}
	
	private Strategy getStrategy(int accCount) {
		switch (accCount) {
			case 0: throw new IllegalArgumentException();
			case 1:	return oneAccidental;
			case 2: return twoAccidentals;
			case 3: return threeAccidentals;
			default: return manyAccidentals;
		}
	}

	/**
	 * Computes the accidentals alignment for a chord
	 * with any number of accidental. Currently this returns no good layout,
	 * since all accidentals are within a single column.
	 */
	private AccidentalsDisplacement computeAlignmentNAccidentals(List<Pitch> pitches,
		NoteDisplacement[] notes, ChordWidths chordWidths, MusicContext mc) {
		ArrayList<AccidentalDisplacement> al = alist();
		float width = 0;
		for (int i = 0; i < pitches.size(); i++) {
			Accidental at = mc.getAccidental(pitches.get(i));
			if (at != null) {
				al.add(new AccidentalDisplacement(notes[i].lp, 0, at));
				width = Math.max(width, chordWidths.get(at) + chordWidths.accToNoteGap);
			}
		}
		return new AccidentalsDisplacement(al.toArray(new AccidentalDisplacement[al.size()]), width);
	}

	/**
	 * Returns a boolean array for the given
	 * notes, with a true indicating that a accidental is needed
	 * for this note, and a false indicating that no accidental is needed.
	 */
	boolean[] computeAccidentalsChecklist(List<Pitch> pitches, MusicContext context) {
		boolean[] ret = new boolean[pitches.size()];
		for (int i = 0; i < pitches.size(); i++)
			ret[i] = (context.getAccidental(pitches.get(i)) != null);
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
