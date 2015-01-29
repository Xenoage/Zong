package com.xenoage.zong.musiclayout.notator;

import static com.xenoage.utils.math.Delta.Df;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.musiclayout.notator.ChordDisplacementPolicy.chordDisplacementPolicy;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.ChordDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteSuspension;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Tests for {@link ChordDisplacementPolicy}.
 *
 * @author Andreas Wenger
 */
public class ChordDisplacementPolicyTest {

	private ChordDisplacementPolicy testee = chordDisplacementPolicy;
	
	private MusicContext context = MusicContext.simpleInstance;
	private ChordWidths cw = ChordWidths.defaultValue;
	private float n = cw.quarter;
	private float dg = cw.dotGap;

	/**
	 * Tests a C5, 1/4. Stem: left, down. Width: 1x quarter.
	 */
	@Test public void testSingleNoteC5() {
		Chord chord = chord(pi(0, 0, 5), fr(1, 4));
		ChordDisplacement cna = testee.computeChordDisplacement(chord, StemDirection.Down, cw, context);
		assertEquals(0, cna.stemOffsetIs, Df);
		assertEquals(n, cna.widthIs, Df);
		NoteDisplacement note = cna.getNote(0);
		assertEquals(5, note.lp);
		assertEquals(0, note.offsetIs, Df);
		assertEquals(NoteSuspension.None, note.suspension);
	}

	/**
	 * Tests a F4, 1/2. Stem: right, up. Width: 1x half.
	 */
	@Test public void testSingleNoteF4() {
		Chord chord = chord(pi(3, 0, 4), fr(1, 2));
		ChordDisplacement cna = testee.computeChordDisplacement(chord, StemDirection.Up, cw, context);
		assertEquals(n, cna.stemOffsetIs, Df);
		assertEquals(n, cna.widthIs, Df);
		NoteDisplacement note = cna.getNote(0);
		assertEquals(1, note.lp);
		assertEquals(0, note.offsetIs, Df);
		assertEquals(NoteSuspension.None, note.suspension);
	}

	/**
	 * Tests a C5/D5, 1/4. Stem: left, down. Width: 1x quarter.
	 */
	@Test public void testChordC5D5() {
		Chord chord = chord(new Pitch[] { pi(0, 0, 5), pi(1, 0, 5) }, fr(1, 4));
		ChordDisplacement cna = testee.computeChordDisplacement(chord, StemDirection.Down, cw, context);
		;
		assertEquals(n, cna.stemOffsetIs, Df);
		assertEquals(2 * n, cna.widthIs, Df);
		NoteDisplacement note = cna.getNote(0);
		assertEquals(5, note.lp);
		assertEquals(0, note.offsetIs, Df);
		assertEquals(NoteSuspension.Left, note.suspension);
		note = cna.getNote(1);
		assertEquals(6, note.lp);
		assertEquals(n, note.offsetIs, Df);
		assertEquals(NoteSuspension.None, note.suspension);
	}

	/**
	 * Tests a C4-E4-G4, 3/4. Stem: right, up. Width: 1x half + 1x dot.
	 */
	@Test public void testChordC4E4G4() {
		Chord chord = chord(new Pitch[] { pi(0, 0, 4), pi(2, 0, 4), pi(4, 0, 4) },
			fr(3, 4));
		ChordDisplacement cna = testee.computeChordDisplacement(chord, StemDirection.Up, cw, context);
		;
		assertEquals(n, cna.stemOffsetIs, Df);
		assertEquals(n + dg, cna.widthIs, Df);
		assertEquals(0, cna.getNote(0).offsetIs, Df);
		assertEquals(NoteSuspension.None, cna.getNote(0).suspension);
		assertEquals(0, cna.getNote(1).offsetIs, Df);
		assertEquals(NoteSuspension.None, cna.getNote(1).suspension);
		assertEquals(0, cna.getNote(2).offsetIs, Df);
		assertEquals(NoteSuspension.None, cna.getNote(2).suspension);
	}

	/**
	 * Tests a A4-C5-D5, 1/4. Stem: left, down. Width: 2x quarter.
	 */
	@Test public void testChordA4C5D5() {
		Chord chord = chord(new Pitch[] { pi(5, 0, 4), pi(0, 0, 5), pi(1, 0, 5) },
			fr(1, 4));
		ChordDisplacement cna = testee.computeChordDisplacement(chord, StemDirection.Down, cw, context);
		;
		assertEquals(n, cna.stemOffsetIs, Df);
		assertEquals(2 * n, cna.widthIs, Df);
		assertEquals(n, cna.getNote(0).offsetIs, Df);
		assertEquals(NoteSuspension.None, cna.getNote(0).suspension);
		assertEquals(0, cna.getNote(1).offsetIs, Df);
		assertEquals(NoteSuspension.Left, cna.getNote(1).suspension);
		assertEquals(n, cna.getNote(2).offsetIs, Df);
		assertEquals(NoteSuspension.None, cna.getNote(2).suspension);
	}

	/**
	 * Tests the dot positions for some chords.
	 */
	@Test public void testDotPositions() {
		//C5: position 6
		Chord chord = chord(pi(0, 0, 5), fr(3, 4));
		ChordDisplacement cna = testee.computeChordDisplacement(chord, StemDirection.Down, cw, context);
		assertEquals(1, cna.getDotsPerNoteCount());
		assertEquals(1, cna.dotsLp.length);
		assertEquals(5, cna.dotsLp[0]);
		//B4: position 6
		chord = chord(pi(6, 0, 4), fr(7, 8));
		cna = testee.computeChordDisplacement(chord, StemDirection.Down, cw, context);
		assertEquals(2, cna.getDotsPerNoteCount());
		assertEquals(1, cna.dotsLp.length);
		assertEquals(5, cna.dotsLp[0]);
		//F4, F4: position 1
		chord = chord(new Pitch[] { pi(3, 0, 4), pi(3, 0, 4) }, fr(7, 16));
		cna = testee.computeChordDisplacement(chord, StemDirection.Down, cw, context);
		assertEquals(2, cna.getDotsPerNoteCount());
		assertEquals(1, cna.dotsLp.length);
		assertEquals(1, cna.dotsLp[0]);
		//F5, A5, B5: positions 7, 9, 11
		chord = chord(new Pitch[] { pi(3, 0, 5), pi(5, 0, 5), pi(6, 0, 5) }, fr(3, 2));
		cna = testee.computeChordDisplacement(chord, StemDirection.Down, cw, context);
		assertEquals(1, cna.getDotsPerNoteCount());
		assertEquals(3, cna.dotsLp.length);
		assertEquals(7, cna.dotsLp[0]);
		assertEquals(9, cna.dotsLp[1]);
		assertEquals(11, cna.dotsLp[2]);
	}

	/**
	 * Tests a C5/C5 (unison) chord, 1/4. Stem: left, down. Width: 2x quarter.
	 */
	@Test public void testChordC5C5() {
		Chord chord = chord(new Pitch[] { pi(0, 0, 5), pi(0, 0, 5) }, fr(1, 4));
		ChordDisplacement cna = testee.computeChordDisplacement(chord, StemDirection.Down, cw, context);
		assertEquals(n, cna.stemOffsetIs, Df);
		assertEquals(2 * n, cna.widthIs, Df);
		NoteDisplacement note = cna.getNote(0);
		assertEquals(5, note.lp);
		assertEquals(0, note.offsetIs, Df);
		assertEquals(NoteSuspension.Left, note.suspension);
		note = cna.getNote(1);
		assertEquals(5, note.lp);
		assertEquals(n, note.offsetIs, Df);
		assertEquals(NoteSuspension.None, note.suspension);
	}

}
