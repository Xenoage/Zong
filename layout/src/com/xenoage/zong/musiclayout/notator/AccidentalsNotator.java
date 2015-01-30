package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.zong.musiclayout.notator.accidentals.ManyAccidentals.manyAccidentals;
import static com.xenoage.zong.musiclayout.notator.accidentals.OneAccidental.oneAccidental;
import static com.xenoage.zong.musiclayout.notator.accidentals.ThreeAccidentals.threeAccidentals;
import static com.xenoage.zong.musiclayout.notator.accidentals.TwoAccidentals.twoAccidentals;

import java.util.List;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Accidental;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notator.accidentals.Strategy;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Computes the notations of the accidentals of a given chord.
 * 
 * Some rules are adepted from
 * "Ross: The Art of Music Engraving", page 130 ff, and
 * "Chlapik: Die Praxis des Notengraphikers", page 48 ff.
 * 
 * @author Andreas Wenger
 */
public class AccidentalsNotator {
	
	public static final AccidentalsNotator accidentalsNotator =
		new AccidentalsNotator();
	
	
	/**
	 * Computes the notations of the accidentals of the given chord.
	 * If there are no accidentals, {@link AccidentalsNotation#empty} is returned.
	 */
	public AccidentalsNotation compute(Chord chord,
		NotesNotation notesAlignment, ChordWidths chordWidths, MusicContext mc) {
		return compute(chord.getPitches(), notesAlignment.notes,
			chordWidths, mc);
	}

	AccidentalsNotation compute(List<Pitch> pitches, NoteDisplacement[] notes,
		ChordWidths chordWidths, MusicContext mc) {
		//count number of needed accidentals
		int accCount = countAccidentals(pitches, mc);
		//there are different alignment algorithms for each number
		if (accCount == 0)
			return AccidentalsNotation.empty;
		else
			return getStrategy(accCount).computeAccidentalsDisplacement(
				pitches, notes, accCount, chordWidths, mc);
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

}
