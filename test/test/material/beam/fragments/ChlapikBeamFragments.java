package material.beam.fragments;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChordsUnchecked;

import java.util.List;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.Note;

/**
 * These test examples are from Chlapik, page 45, rule 6.
 * 
 * @author Andreas Wenger
 */
public class ChlapikBeamFragments {

	public Beam exampleRow1Col1() {
		List<Chord> chords = alist();
		chords.add(chordC(Companion.fr(1, 16)));
		chords.add(chordC(Companion.fr(1, 8)));
		chords.add(chordC(Companion.fr(1, 16)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow1Col2() {
		List<Chord> chords = alist();
		chords.add(chordF(Companion.fr(1, 16)));
		chords.add(chordF(Companion.fr(1, 8)));
		chords.add(chordF(Companion.fr(1, 16)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow1Col3() {
		List<Chord> chords = alist();
		chords.add(chordC(Companion.fr(1, 16)));
		chords.add(chordC(Companion.fr(1, 8)));
		chords.add(chordC(Companion.fr(1, 8)));
		chords.add(chordC(Companion.fr(1, 16)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow1Col4() {
		List<Chord> chords = alist();
		chords.add(chordF(Companion.fr(1, 16)));
		chords.add(chordF(Companion.fr(1, 8)));
		chords.add(chordF(Companion.fr(1, 8)));
		chords.add(chordF(Companion.fr(1, 16)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow2Col1() {
		List<Chord> chords = alist();
		chords.add(chordC(Companion.fr(3, 16)));
		chords.add(chordC(Companion.fr(1, 16)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow2Col2() {
		List<Chord> chords = alist();
		chords.add(chordF(Companion.fr(3, 16)));
		chords.add(chordF(Companion.fr(1, 16)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow2Col3() {
		List<Chord> chords = alist();
		chords.add(chordC(Companion.fr(3, 32)));
		chords.add(chordC(Companion.fr(1, 32)));
		chords.add(chordC(Companion.fr(3, 32)));
		chords.add(chordC(Companion.fr(1, 32)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow2Col4() {
		List<Chord> chords = alist();
		chords.add(chordF(Companion.fr(3, 32)));
		chords.add(chordF(Companion.fr(1, 32)));
		chords.add(chordF(Companion.fr(3, 32)));
		chords.add(chordF(Companion.fr(1, 32)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow2Col5() {
		List<Chord> chords = alist();
		chords.add(chordC(Companion.fr(1, 8)));
		chords.add(chordC(Companion.fr(1, 32)));
		chords.add(chordC(Companion.fr(3, 32)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow2Col6() {
		List<Chord> chords = alist();
		chords.add(chordF(Companion.fr(1, 8)));
		chords.add(chordF(Companion.fr(1, 32)));
		chords.add(chordF(Companion.fr(3, 32)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow3Col2() {
		List<Chord> chords = alist();
		chords.add(chordC(Companion.fr(1, 16)));
		chords.add(chordC(Companion.fr(1, 8)));
		chords.add(chordC(Companion.fr(1, 16)));
		chords.add(chordC(Companion.fr(1, 8)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow3Col4() {
		List<Chord> chords = alist();
		chords.add(chordC(Companion.fr(1, 8)));
		chords.add(chordC(Companion.fr(1, 16)));
		chords.add(chordC(Companion.fr(3, 16)));
		return beamFromChordsUnchecked(chords);
	}

	public Beam exampleRow3Col6() {
		List<Chord> chords = alist();
		chords.add(chordC(Companion.fr(7, 32)));
		chords.add(chordC(Companion.fr(1, 32)));
		chords.add(chordC(Companion.fr(3, 32)));
		chords.add(chordC(Companion.fr(1, 32)));
		return beamFromChordsUnchecked(chords);
	}

	private Chord chordC(Fraction duration) {
		return new Chord(new Note(pi(0, 0, 5)), duration);
	}

	private Chord chordF(Fraction duration) {
		return new Chord(new Note(pi(3, 0, 4)), duration);
	}

}
