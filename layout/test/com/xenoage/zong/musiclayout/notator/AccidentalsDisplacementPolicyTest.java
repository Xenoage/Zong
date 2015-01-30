package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.musiclayout.notations.chord.NoteDisplacementTest.note;
import static com.xenoage.zong.musiclayout.notator.AccidentalsDisplacementPolicy.accidentalsDisplacementPolicy;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.musiclayout.notations.chord.AccidentalsDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteSuspension;
import com.xenoage.zong.musiclayout.notator.accidentals.Strategy;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Tests for {@link AccidentalsDisplacementPolicy}.
 *
 * @author Andreas Wenger
 */
public class AccidentalsDisplacementPolicyTest {

	private AccidentalsDisplacementPolicy testee = accidentalsDisplacementPolicy;

	private MusicContext contextC, contextEb;
	private MusicContext contextAccD4, contextAccG4, contextAccB4, contextAccC5, contextAccD5;
	private MusicContext contextAccsD4B4;//, contextAccsG5A5;
	private float noteOffset;

	private ChordWidths cw = ChordWidths.defaultValue;

	private NoteSuspension susNone = NoteSuspension.None;
	private NoteSuspension susLeft = NoteSuspension.Left;
	private NoteSuspension susRight = NoteSuspension.Right;


	@Before public void setUp() {
		ClefType clefG = ClefType.clefTreble;
		contextC = MusicContext.simpleInstance;
		//contextEb: key = Eb major, acc = Fbb5, G##5
		contextEb = new MusicContext(new Clef(clefG), new TraditionalKey(-3), new Pitch[] {
			pi(3, -2, 5), pi(4, 2, 5) }, 5);
		//contextAccD4: key = C major, acc = D#4
		contextAccD4 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(1, 1, 4) }, 5);
		//contextAccG4: key = C major, acc = G#4
		contextAccG4 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(4, 1, 4) }, 5);
		//contextAccB4: key = C major, acc = B#4
		contextAccB4 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(6, 1, 4) }, 5);
		//contextAccC5: key = C major, acc = C#5
		contextAccC5 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(0, 1, 5) }, 5);
		//contextAccD5: key = C major, acc = D#5
		contextAccD5 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(1, 1, 5) }, 5);
		//contextAccsD4B5: key = C major, acc = D#4 and B#4
		contextAccsD4B4 = new MusicContext(new Clef(clefG), new TraditionalKey(0), new Pitch[] {
			pi(1, 1, 4), pi(6, 1, 4) }, 5);
		//contextAccsG5A5: key = C major, acc = G#5 and A#5
		/*TODO contextAccsG5A5 = new MusicContext(
		  new Clef(clefG, null), new TraditionalKey(0),
		  new Pitch[]{pi4, 1, 5), pi5, 1, 5)});*/
		noteOffset = 1.2f; //typical quarter note width
	}
	
	@Test public void getAccNoteIndexTest() {
		//E4, G#4, C#5 (no accidental at bottom note)
		//must have accidentals on note 1 and 2
		List<Pitch> pitches = alist(pi(2, 0, 4), pi(4, 1, 4), pi(0, 1, 5));
		assertEquals(1, Strategy.getAccNoteIndex(pitches, 0, contextC));
		assertEquals(2, Strategy.getAccNoteIndex(pitches, 2, contextC));
	}

	/**
	 * Tests some chords with no accidentals
	 */
	@Test public void testNoAcc() {
		//C5
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(alist(pi(0, 0, 5)),
			new NoteDisplacement[] { note(5) }, cw, contextC);
		assertEmpty(accs);
		//C4, D4, G4
		accs = testee.computeAccidentalsAlignment(alist(pi(0, 0, 4), pi(1, 0, 4), pi(4, 0, 4)),
			new NoteDisplacement[] { note(-2), note(-1, noteOffset, susRight), note(2) }, cw, contextC);
		assertEmpty(accs);
		//Eb4, Ab4, G##5 with contextEb
		accs = testee.computeAccidentalsAlignment(alist(pi(2, -1, 4), pi(5, -1, 4), pi(4, 2, 5)),
			new NoteDisplacement[] { note(0), note(3), note(9) }, cw, contextEb);
		assertEmpty(accs);
	}
	
	private void assertEmpty(AccidentalsDisplacement caa) {
		assertEquals(0, caa.accidentals.length);
		assertEquals(0, caa.widthIs, df);
	}

	/**
	 * Tests some chords with one accidental.
	 */
	@Test public void test1Acc() {
		//C#5
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(alist(pi(0, 1, 5)),
			new NoteDisplacement[] { note(5) }, cw, contextC);
		assertEquals(1, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		//C##5
		accs = testee.computeAccidentalsAlignment(alist(pi(0, 2, 5)),
			new NoteDisplacement[] { note(5) }, cw, contextC);
		assertEquals(1, accs.accidentals.length);
		assertEquals(cw.doubleSharp + cw.accToNoteGap, accs.widthIs, df);
		//C4, D4, Gbb4
		accs = testee.computeAccidentalsAlignment(alist(pi(0, 0, 4), pi(1, 0, 4), pi(4, -2, 4)),
			new NoteDisplacement[] { note(-2), note(-1, noteOffset, susRight),
				note(2) }, cw, contextC);
		assertEquals(1, accs.accidentals.length);
		assertEquals(cw.doubleFlat + cw.accToNoteGap, accs.widthIs, df);
		//Eb4, A4, G##5 with contextEb
		accs = testee.computeAccidentalsAlignment(alist(pi(2, -1, 4), pi(5, 0, 4), pi(4, 2, 5)),
			new NoteDisplacement[] { note(0), note(3),
				note(9) }, cw, contextEb);
		assertEquals(1, accs.accidentals.length);
		assertEquals(cw.natural + cw.accToNoteGap, accs.widthIs, df);
	}

	/**
	 * Tests some chords with two notes and two accidentals.
	 * The examples are taken from "Ross: The Art of Music Engraving",
	 * page 131 f.
	 */
	@Test public void test2Acc2Notes() {
		//F#4, F#5 (p. 131, F# instead of F(nat))
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(alist(pi(3, 1, 4), pi(3, 1, 5)),
			new NoteDisplacement[] { note(1), note(8) }, cw, contextC);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		//G#4, B#5 (p. 131)
		accs = testee.computeAccidentalsAlignment(alist(pi(4, 1, 4), pi(6, 1, 5)), new NoteDisplacement[] {
			note(2), note(11) }, cw, contextC);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		//Bb4, Ab5 (p. 131)
		accs = testee.computeAccidentalsAlignment(alist(pi(6, -1, 4), pi(5, -1, 5)),
			new NoteDisplacement[] { note(4), note(10) }, cw, contextC);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToNoteGap, accs.widthIs, df);
		//Ab4, Bb4 (p. 132)
		accs = testee.computeAccidentalsAlignment(alist(pi(5, -1, 4), pi(6, -1, 4)),
			new NoteDisplacement[] { note(3), note(4, cw.quarter, susRight) },
			cw, contextC);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[1].offsetIs,
			df);
		//A#4, F#5 (p. 132)
		accs = testee.computeAccidentalsAlignment(alist(pi(5, 1, 4), pi(3, 1, 5)), new NoteDisplacement[] {
			note(3), note(8) }, cw, contextC);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[1].offsetIs,
			df);
		//Db5, Eb5 (p. 132)
		accs = testee.computeAccidentalsAlignment(alist(pi(1, -1, 5), pi(2, -1, 5)),
			new NoteDisplacement[] { note(6, 0, susLeft), note(7, cw.quarter, susNone) },
			cw, contextC);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[1].offsetIs,
			df);
	}

	/**
	 * Tests some chords with three notes and two accidentals.
	 * The examples are taken from "Ross: The Art of Music Engraving",
	 * page 132.
	 */
	@Test public void test2Acc3Notes() {
		//E4, G#4, C#5 (no accidental at bottom note)
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(
			alist(pi(2, 0, 4), pi(4, 1, 4), pi(0, 1, 5)), new NoteDisplacement[] { note(0),
				note(2), note(5) }, cw, contextC);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(2, accs.accidentals[0].lp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[1].offsetIs, df);
		assertEquals(5, accs.accidentals[1].lp);
		//Eb4, G(nat)4, C5 with contextAccG4 (no accidental at top note)
		accs = testee.computeAccidentalsAlignment(alist(pi(2, -1, 4), pi(4, 0, 4), pi(0, 0, 5)),
			new NoteDisplacement[] { note(0), note(2), note(5) }, cw, contextAccG4);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.natural + cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(0, accs.accidentals[0].lp);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[1].offsetIs, df);
		assertEquals(2, accs.accidentals[1].lp);
		//Eb4, G4, C(nat)5 with contextAccC5 (no accidental at middle note)
		//TODO: Eb4-accidental can be placed nearer to the chord
		accs = testee.computeAccidentalsAlignment(alist(pi(2, -1, 4), pi(4, 0, 4), pi(0, 0, 5)),
			new NoteDisplacement[] { note(0), note(2), note(5) }, cw, contextAccC5);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.natural + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(0, accs.accidentals[0].lp);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[1].offsetIs,
			df);
		assertEquals(5, accs.accidentals[1].lp);
		//F4, G#4, D(nat)5 with contextAccD5
		//(no accidental at bottom note, middle note suspended)
		accs = testee.computeAccidentalsAlignment(alist(pi(3, 0, 4), pi(4, 1, 4), pi(1, 0, 5)),
			new NoteDisplacement[] { note(1), note(2, cw.quarter, susRight), note(6) }, cw, contextAccD5);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.natural + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(cw.natural + cw.accToAccGap, accs.accidentals[0].offsetIs,
			df);
		assertEquals(2, accs.accidentals[0].lp);
		assertEquals(0f, accs.accidentals[1].offsetIs, df);
		assertEquals(6, accs.accidentals[1].lp);
		//F#4, C5, D#5
		//(no accidental at middle note, top note suspended)
		accs = testee.computeAccidentalsAlignment(alist(pi(3, 1, 4), pi(0, 0, 5), pi(1, 1, 5)),
			new NoteDisplacement[] { note(1), note(5), note(6, cw.quarter, susRight) }, cw, contextC);
		assertEquals(2, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(1, accs.accidentals[0].lp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[1].offsetIs,
			df);
		assertEquals(6, accs.accidentals[1].lp);
		//Ab4, Eb5, F5
		//(no accidental at top note, middle note suspended)
		//TODO: accidentals nearer to chord (Ab4-accidental
		//has enough room under Eb5)
		/* GOON caa = testee.computeAccidentalsAlignment(alist(pi(5, -1, 4), pi(2, -1, 5), pi(3, 0, 5)),
			new NoteDisplacement[] { note(3, cw.quarter, susNone), note(7, 0, susLeft),
				note(8, cw.quarter, susNone) }, cw, contextC);
		assertEquals(2, caa.accidentals.length);
		assertEquals(cw.flat + cw.accToAccGap + cw.flat + cw.accToNoteGap, caa.width,
			df);
		assertEquals(cw.flat + cw.accToAccGap, caa.accidentals[0].offsetIs,
			df);
		assertEquals(3, caa.accidentals[0].lp);
		assertEquals(0f, caa.accidentals[1].offsetIs, df);
		assertEquals(7, caa.accidentals[1].lp); */
	}

	/**
	 * Test a chord with three accidentals.
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 132/133, rule 1.
	 */
	@Test public void test3AccRule1() {
		//D#4, F#4, C#5
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(
			alist(pi(1, 1, 4), pi(3, 1, 4), pi(0, 1, 5)),
			new NoteDisplacement[] { note(-1), note(1), note(5) }, cw, contextC);
		assertEquals(3, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[0].offsetIs,
			df);
		assertEquals(-1, accs.accidentals[0].lp);
		assertEquals(0f, accs.accidentals[1].offsetIs, df);
		assertEquals(1, accs.accidentals[1].lp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[2].offsetIs,
			df);
		assertEquals(5, accs.accidentals[2].lp);
	}

	/**
	 * Test a chord with three accidentals.
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 132/133, rule 2.
	 */
	@Test public void test3AccRule2() {
		//D#4, F#4, B4 with contextAccB4
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(
			alist(pi(1, 1, 4), pi(3, 1, 4), pi(6, 0, 4)),
			new NoteDisplacement[] { note(-1), note(1), note(4) }, cw, contextAccB4);
		assertEquals(3, accs.accidentals.length);
		assertEquals(2 * (cw.sharp + cw.accToAccGap) + cw.natural + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[0].offsetIs,
			df);
		assertEquals(-1, accs.accidentals[0].lp);
		assertEquals(0f, accs.accidentals[1].offsetIs, df);
		assertEquals(1, accs.accidentals[1].lp);
		assertEquals(2 * (cw.sharp + cw.accToAccGap), accs.accidentals[2].offsetIs,
			df);
		assertEquals(4, accs.accidentals[2].lp);
	}

	/**
	 * Test a chord with three accidentals.
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 132/133, rule 3.
	 */
	@Test public void test3AccRule3() {
		//D#4, E#4, C#5
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(
			alist(pi(1, 1, 4), pi(2, 1, 4), pi(0, 1, 5)),
			new NoteDisplacement[] { note(-1), note(0, cw.quarter, susRight), note(5) }, cw, contextC);
		assertEquals(3, accs.accidentals.length);
		assertEquals(2 * (cw.sharp + cw.accToAccGap) + cw.sharp + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(-1, accs.accidentals[0].lp);
		assertEquals(2 * (cw.sharp + cw.accToAccGap), accs.accidentals[1].offsetIs,
			df);
		assertEquals(0, accs.accidentals[1].lp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[2].offsetIs,
			df);
		assertEquals(5, accs.accidentals[2].lp);
	}

	/**
	 * Test a chord with three accidentals.
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 133, rule 4.
	 */
	@Test public void test3AccRule4() {
		//D#4, C#5, D#5
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(
			alist(pi(1, 1, 4), pi(0, 1, 5), pi(1, 1, 5)),
			new NoteDisplacement[] { note(-1), note(5), note(6, cw.quarter, susRight) }, cw, contextC);
		assertEquals(3, accs.accidentals.length);
		assertEquals(cw.sharp + cw.accToAccGap + cw.sharp + cw.accToNoteGap, accs.widthIs,
			df);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[0].offsetIs,
			df);
		assertEquals(-1, accs.accidentals[0].lp);
		assertEquals(0f, accs.accidentals[1].offsetIs, df);
		assertEquals(5, accs.accidentals[1].lp);
		assertEquals(cw.sharp + cw.accToAccGap, accs.accidentals[2].offsetIs,
			df);
		assertEquals(6, accs.accidentals[2].lp);
	}

	/**
	 * Test a chord with three accidentals.
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 133, rule 5.
	 */
	@Test public void test3AccRule5() {
		//D4, Ab4, Bb4 with contextAccD4
		//TODO: natural can be indented nearer to the chord
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(
			alist(pi(1, 0, 4), pi(5, -1, 4), pi(6, -1, 4)),
			new NoteDisplacement[] { note(-1), note(3), note(4, cw.quarter, susRight) }, cw, contextAccD4);
		assertEquals(3, accs.accidentals.length);
		assertEquals(
			cw.flat + cw.accToAccGap + cw.natural + cw.accToAccGap + cw.flat + cw.accToNoteGap,
			accs.widthIs, df);
		assertEquals(cw.flat + cw.accToAccGap, accs.accidentals[0].offsetIs,
			df);
		assertEquals(-1, accs.accidentals[0].lp);
		assertEquals(0f, accs.accidentals[1].offsetIs, df);
		assertEquals(3, accs.accidentals[1].lp);
		assertEquals(cw.flat + cw.accToAccGap + cw.natural + cw.accToAccGap,
			accs.accidentals[2].offsetIs, df);
		assertEquals(4, accs.accidentals[2].lp);
	}

	/**
	 * Test a chord with three accidentals.
	 * The example is taken from "Ross: The Art of Music Engraving",
	 * page 133, rule 6.
	 */
	@Test public void test3AccRule6() {
		//D4, E#4, B4 with contextAccsD4B4
		//TODO: natural can be indented nearer to the chord
		AccidentalsDisplacement accs = testee.computeAccidentalsAlignment(
			alist(pi(1, 0, 4), pi(2, 1, 4), pi(6, 0, 4)),
			new NoteDisplacement[] { note(-1), note(0, cw.quarter, susRight), note(4) }, cw, contextAccsD4B4);
		assertEquals(3, accs.accidentals.length);
		assertEquals(cw.natural + cw.accToAccGap + cw.natural + cw.accToAccGap + cw.sharp +
			cw.accToNoteGap, accs.widthIs, df);
		assertEquals(0f, accs.accidentals[0].offsetIs, df);
		assertEquals(-1, accs.accidentals[0].lp);
		assertEquals(2 * (cw.natural + cw.accToAccGap), accs.accidentals[1].offsetIs,
			df);
		assertEquals(0, accs.accidentals[1].lp);
		assertEquals(cw.natural + cw.accToAccGap, accs.accidentals[2].offsetIs,
			df);
		assertEquals(4, accs.accidentals[2].lp);
	}

	/**
	 * Test some chords with three accidentals.
	 * These examples are taken from "Ross: The Art of Music Engraving",
	 * page 133.
	 */
	@Test public void test3AccSuspendedLeft() {
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
