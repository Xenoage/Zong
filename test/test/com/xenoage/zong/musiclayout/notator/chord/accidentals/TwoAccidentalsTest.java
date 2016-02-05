package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.notation.chord.NoteDisplacementTest.note;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.Strategy.getParams;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.TwoAccidentals.twoAccidentals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.musiclayout.notation.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notation.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notator.chord.accidentals.TwoAccidentals;

/**
 * Tests for {@link TwoAccidentals}.
 * 
 * @author Andreas Wenger
 */
public class TwoAccidentalsTest
	extends TestData {
	
	private TwoAccidentals testee = twoAccidentals;
	
	
	/**
	 * Tests some chords with two notes and two accidentals.
	 * The examples are taken from "Ross: The Art of Music Engraving",
	 * page 131 f.
	 */
	@Test public void test2Notes() {
		//F#4, F#5 (p. 131, F# instead of F(nat))
		AccidentalsNotation accs = testee.compute(getParams(
			alist(pi(3, 1, 4), pi(3, 1, 5)), new NoteDisplacement[] { note(1), note(8) }, 2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		//G#4, B#5 (p. 131)
		accs = testee.compute(getParams(
			alist(pi(4, 1, 4), pi(6, 1, 5)), new NoteDisplacement[] { note(2), note(11) }, 2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		//Bb4, Ab5 (p. 131)
		accs = testee.compute(getParams(
			alist(pi(6, -1, 4), pi(5, -1, 5)), new NoteDisplacement[] { note(4), note(10) }, 2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToNoteGap, accs.widthIs, df);
		//Ab4, Bb4 (p. 132)
		accs = testee.compute(getParams(
			alist(pi(5, -1, 4), pi(6, -1, 4)), new NoteDisplacement[] { note(3), note(4, cw.quarter, susRight) },
			2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[1].xIs, df);
		//A#4, F#5 (p. 132)
		accs = testee.compute(getParams(
			alist(pi(5, 1, 4), pi(3, 1, 5)), new NoteDisplacement[] { note(3), note(8) }, 2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[1].xIs, df);
		//Db5, Eb5 (p. 132)
		accs = testee.compute(getParams(
			alist(pi(1, -1, 5), pi(2, -1, 5)),
			new NoteDisplacement[] { note(6, 0, susLeft), note(7, cw.quarter, susNone) }, 2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[1].xIs, df);
	}

	/**
	 * Tests some chords with three notes and two accidentals.
	 * The examples are taken from "Ross: The Art of Music Engraving",
	 * page 132.
	 */
	@Test public void test3Notes() {
		//E4, G#4, C#5 (no accidental at bottom note)
		AccidentalsNotation accs = testee.compute(getParams(
			alist(pi(2, 0, 4), pi(4, 1, 4), pi(0, 1, 5)), new NoteDisplacement[] { note(0),
				note(2), note(5) }, 2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(2, accs.accidentals[0].yLp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[1].xIs, df);
		assertEquals(5, accs.accidentals[1].yLp);
		//Eb4, G(nat)4, C5 with contextAccG4 (no accidental at top note)
		accs = testee.compute(getParams(
			alist(pi(2, -1, 4), pi(4, 0, 4), pi(0, 0, 5)),
			new NoteDisplacement[] { note(0), note(2), note(5) }, 2, cw, contextAccG4));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.natural + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(0, accs.accidentals[0].yLp);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[1].xIs, df);
		assertEquals(2, accs.accidentals[1].yLp);
		//Eb4, G4, C(nat)5 with contextAccC5 (no accidental at middle note)
		//TODO: Eb4-accidental can be placed nearer to the chord
		accs = testee.compute(getParams(
			alist(pi(2, -1, 4), pi(4, 0, 4), pi(0, 0, 5)),
			new NoteDisplacement[] { note(0), note(2), note(5) }, 2, cw, contextAccC5));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.natural + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(0, accs.accidentals[0].yLp);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[1].xIs, df);
		assertEquals(5, accs.accidentals[1].yLp);
		//F4, G#4, D(nat)5 with contextAccD5
		//(no accidental at bottom note, middle note suspended)
		accs = testee.compute(getParams(
			alist(pi(3, 0, 4), pi(4, 1, 4), pi(1, 0, 5)),
			new NoteDisplacement[] { note(1), note(2, cw.quarter, susRight), note(6) }, 2, cw, contextAccD5));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.natural + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(cw.natural + cw.accToAccGap, accs.accidentals[0].xIs, df);
		assertEquals(2, accs.accidentals[0].yLp);
		assertEquals(0f, accs.accidentals[1].xIs, df);
		assertEquals(6, accs.accidentals[1].yLp);
		//F#4, C5, D#5
		//(no accidental at middle note, top note suspended)
		accs = testee.compute(getParams(
			alist(pi(3, 1, 4), pi(0, 0, 5), pi(1, 1, 5)),
			new NoteDisplacement[] { note(1), note(5), note(6, cw.quarter, susRight) }, 2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(1, accs.accidentals[0].yLp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[1].xIs, df);
		assertEquals(6, accs.accidentals[1].yLp);
		//Ab4, Eb5, F5
		//(no accidental at top note, middle note suspended)
		//TODO: accidentals nearer to chord (Ab4-accidental
		//has enough room under Eb5)
		accs = testee.compute(getParams(
			alist(pi(5, -1, 4), pi(2, -1, 5), pi(3, 0, 5)),
			new NoteDisplacement[] { note(3, cw.quarter, susNone), note(7, 0, susLeft),
				note(8, cw.quarter, susNone) }, 2, cw, contextC));
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[0].xIs, df);
		assertEquals(3, accs.accidentals[0].yLp);
		assertEquals(0f, accs.accidentals[1].xIs, df);
		assertEquals(7, accs.accidentals[1].yLp);
	}

}
