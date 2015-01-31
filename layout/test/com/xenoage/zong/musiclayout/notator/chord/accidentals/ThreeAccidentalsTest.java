package com.xenoage.zong.musiclayout.notator.chord.accidentals;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.notations.chord.NoteDisplacementTest.note;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.Strategy.getParams;
import static com.xenoage.zong.musiclayout.notator.chord.accidentals.ThreeAccidentals.threeAccidentals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.musiclayout.notations.chord.AccidentalsNotation;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notator.chord.accidentals.ThreeAccidentals;

/**
 * Tests for {@link ThreeAccidentals}.
 * 
 * @author Andreas Wenger
 */
public class ThreeAccidentalsTest
	extends TestData {
	
	private ThreeAccidentals testee = threeAccidentals;
	
	
	/**
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 132/133, rule 1.
	 */
	@Test public void testRule1() {
		//D#4, F#4, C#5
		AccidentalsNotation accs = testee.compute(getParams(
			alist(pi(1, 1, 4), pi(3, 1, 4), pi(0, 1, 5)),
			new NoteDisplacement[] { note(-1), note(1), note(5) }, 3, cw, contextC));
		assertEquals(3, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[0].xIs, df);
		assertEquals(-1, accs.accidentals[0].yLp);
		assertEquals(0f, accs.accidentals[1].xIs, df);
		assertEquals(1, accs.accidentals[1].yLp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[2].xIs, df);
		assertEquals(5, accs.accidentals[2].yLp);
	}

	/**
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 132/133, rule 2.
	 */
	@Test public void testRule2() {
		//D#4, F#4, B4 with contextAccB4
		AccidentalsNotation accs = testee.compute(getParams(
			alist(pi(1, 1, 4), pi(3, 1, 4), pi(6, 0, 4)),
			new NoteDisplacement[] { note(-1), note(1), note(4) }, 3, cw, contextAccB4));
		assertEquals(3, accs.accidentals.length);
		assertEquals(2 * (cw.sharp + cw.accToAccGap) + cw.natural + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[0].xIs, df);
		assertEquals(-1, accs.accidentals[0].yLp);
		assertEquals(0f, accs.accidentals[1].xIs, df);
		assertEquals(1, accs.accidentals[1].yLp);
		assertEquals(2 * (cw.sharp + cw.accToAccGap), accs.accidentals[2].xIs, df);
		assertEquals(4, accs.accidentals[2].yLp);
	}

	/**
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 132/133, rule 3.
	 */
	@Test public void testRule3() {
		//D#4, E#4, C#5
		AccidentalsNotation accs = testee.compute(getParams(
			alist(pi(1, 1, 4), pi(2, 1, 4), pi(0, 1, 5)),
			new NoteDisplacement[] { note(-1), note(0, cw.quarter, susRight), note(5) }, 3, cw, contextC));
		assertEquals(3, accs.accidentals.length);
		assertEquals(2 * (cw.sharp + cw.accToAccGap) + cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(-1, accs.accidentals[0].yLp);
		assertEquals(2 * (cw.sharp + cw.accToAccGap), accs.accidentals[1].xIs, df);
		assertEquals(0, accs.accidentals[1].yLp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[2].xIs, df);
		assertEquals(5, accs.accidentals[2].yLp);
	}

	/**
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 133, rule 4.
	 */
	@Test public void testRule4() {
		//D#4, C#5, D#5
		AccidentalsNotation accs = testee.compute(getParams(
			alist(pi(1, 1, 4), pi(0, 1, 5), pi(1, 1, 5)),
			new NoteDisplacement[] { note(-1), note(5), note(6, cw.quarter, susRight) }, 3, cw, contextC));
		assertEquals(3, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[0].xIs, df);
		assertEquals(-1, accs.accidentals[0].yLp);
		assertEquals(0f, accs.accidentals[1].xIs, df);
		assertEquals(5, accs.accidentals[1].yLp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[2].xIs, df);
		assertEquals(6, accs.accidentals[2].yLp);
	}

	/**
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 133, rule 5.
	 */
	@Test public void testRule5() {
		//D4, Ab4, Bb4 with contextAccD4
		//TODO: natural can be indented nearer to the chord
		AccidentalsNotation accs = testee.compute(getParams(
			alist(pi(1, 0, 4), pi(5, -1, 4), pi(6, -1, 4)),
			new NoteDisplacement[] { note(-1), note(3), note(4, cw.quarter, susRight) }, 3, cw, contextAccD4));
		assertEquals(3, accs.accidentals.length);
		assertEquals(
			cw.flat + cw.accToAccGap + cw.natural + cw.accToAccGap + cw.flat + cw.accToNoteGap,
			accs.widthIs, df);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[0].xIs, df);
		assertEquals(-1, accs.accidentals[0].yLp);
		assertEquals(0f, accs.accidentals[1].xIs, df);
		assertEquals(3, accs.accidentals[1].yLp);
		assertEquals(cw.flat + cw.accToAccGap + cw.natural + cw.accToAccGap,
			accs.accidentals[2].xIs, df);
		assertEquals(4, accs.accidentals[2].yLp);
	}

	/**
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 133, rule 6.
	 */
	@Test public void testRule6() {
		//D4, E#4, B4 with contextAccsD4B4
		//TODO: natural can be indented nearer to the chord
		AccidentalsNotation accs = testee.compute(getParams(
			alist(pi(1, 0, 4), pi(2, 1, 4), pi(6, 0, 4)),
			new NoteDisplacement[] { note(-1), note(0, cw.quarter, susRight), note(4) }, 3, cw, contextAccsD4B4));
		assertEquals(3, accs.accidentals.length);
		assertEquals(cw.natural + cw.accToAccGap + cw.natural + cw.accToAccGap + cw.sharp +
			cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].xIs, df);
		assertEquals(-1, accs.accidentals[0].yLp);
		assertEquals(2 * (cw.natural + cw.accToAccGap), accs.accidentals[1].xIs, df);
		assertEquals(0, accs.accidentals[1].yLp);
		assertEquals(cw.natural + cw.accToAccGap, accs.accidentals[2].xIs, df);
		assertEquals(4, accs.accidentals[2].yLp);
	}

	/**
	 * These examples are taken from "Ross: The Art of Music Engraving",
	 * page 133.
	 */
	@Test public void testSuspendedLeft() {
		/* TODO: can not be solved with the current algorithm
		//Db5, Eb5, A#5
		ChordAccidentalsAlignment caa = new ChordAccidentalsAlignment(
		  new Pitch[]{
		    pi1, -1, 5), pi2, -1, 5), pi6, 1, 5)},
		  new NoteAlignment[]{new NoteAlignment(6, 0, susLeft),
		    new NoteAlignment(7, cw.quarter),
		    new NoteAlignment(10, cw.quarter)}, contextC);
		assertEquals(3, caa.accidentals.length);
		assertEquals(2 * (cw.flat + cw.accToAccGap) +
		  cw.sharp + cw.accToNoteGap,
		  caa.width, df);
		assertEquals(0f,
		  caa.accidentals[0].offsetIs, df);
		assertEquals(6, caa.accidentals[0].lp);
		assertEquals(cw.flat + cw.accToAccGap,
		  caa.accidentals[1].offsetIs, df);
		assertEquals(7, caa.accidentals[1].lp);
		assertEquals(2 * (cw.flat + cw.accToAccGap),
		  caa.accidentals[2].offsetIs, df);
		assertEquals(10, caa.accidentals[2].lp);
		//A#4, B#4, A#5
		caa = new ChordAccidentalsAlignment(
		  new Pitch[]{
		    pi5, 1, 4), pi6, 1, 4), pi6, 1, 5)},
		  new NoteAlignment[]{new NoteAlignment(3, 0, susLeft),
		    new NoteAlignment(4, cw.quarter),
		    new NoteAlignment(10, cw.quarter)}, contextC);
		assertEquals(3, caa.accidentals.length);
		assertEquals(2 * (cw.sharp + cw.accToAccGap) +
		  cw.sharp + cw.accToNoteGap,
		  caa.width, df);
		assertEquals(0f,
		  caa.accidentals[0].offsetIs, df);
		assertEquals(3, caa.accidentals[0].lp);
		assertEquals(cw.sharp + cw.accToAccGap,
		  caa.accidentals[1].offsetIs, df);
		assertEquals(4, caa.accidentals[1].lp);
		assertEquals(2 * (cw.sharp + cw.accToAccGap),
		  caa.accidentals[2].offsetIs, df);
		assertEquals(10, caa.accidentals[2].lp);
		//C#5, G5, A5 with contextAccsG5A5
		caa = new ChordAccidentalsAlignment(
		  new Pitch[]{
		    pi0, 1, 5), pi4, 0, 5), pi5, 0, 5)},
		  new NoteAlignment[]{new NoteAlignment(5, cw.quarter),
		    new NoteAlignment(9, 0, susLeft),
		    new NoteAlignment(10, cw.quarter)}, contextAccsG5A5);
		assertEquals(3, caa.accidentals.length);
		assertEquals(2 * (cw.natural + cw.accToAccGap) +
		  cw.sharp + cw.accToNoteGap,
		  caa.width, df);
		assertEquals(2 * (cw.natural + cw.accToAccGap),
		  caa.accidentals[0].offsetIs, df);
		assertEquals(5, caa.accidentals[0].lp);
		assertEquals(0f,
		  caa.accidentals[1].offsetIs, df);
		assertEquals(9, caa.accidentals[1].lp);
		assertEquals(cw.natural + cw.accToAccGap,
		  caa.accidentals[2].offsetIs, df);
		assertEquals(10, caa.accidentals[2].lp); */
	}

}
