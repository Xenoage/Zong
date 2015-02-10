package com.xenoage.zong.musiclayout.notator.chord;

import static com.xenoage.utils.math.Delta.Df;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.musiclayout.notator.chord.NotesNotator.notesNotator;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.core.music.MusicContext;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteSuspension;
import com.xenoage.zong.musiclayout.notator.chord.NotesNotator;
import com.xenoage.zong.musiclayout.settings.ChordWidths;

/**
 * Tests for {@link NotesNotator}.
 *
 * @author Andreas Wenger
 */
public class NotesNotatorTest {

	private NotesNotator testee = notesNotator;
	
	private MusicContext context = MusicContext.simpleInstance;
	private ChordWidths cw = ChordWidths.defaultValue;
	private float n = cw.quarter;
	private float dg = cw.dotGap;

	/**
	 * Tests a C5, 1/4. Stem: left, down. Width: 1x quarter.
	 */
	@Test public void testSingleNoteC5() {
		Chord chord = chord(pi(0, 0, 5), fr(1, 4));
		NotesNotation notes = testee.compute(chord, StemDirection.Down, cw, context);
		assertEquals(0, notes.stemOffsetIs, Df);
		assertEquals(n, notes.widthIs, Df);
		NoteDisplacement note = notes.getNote(0);
		assertEquals(5, note.lp);
		assertEquals(0, note.xIs, Df);
		assertEquals(NoteSuspension.None, note.suspension);
	}

	/**
	 * Tests a F4, 1/2. Stem: right, up. Width: 1x half.
	 */
	@Test public void testSingleNoteF4() {
		Chord chord = chord(pi(3, 0, 4), fr(1, 2));
		NotesNotation notes = testee.compute(chord, StemDirection.Up, cw, context);
		assertEquals(n, notes.stemOffsetIs, Df);
		assertEquals(n, notes.widthIs, Df);
		NoteDisplacement note = notes.getNote(0);
		assertEquals(1, note.lp);
		assertEquals(0, note.xIs, Df);
		assertEquals(NoteSuspension.None, note.suspension);
	}

	/**
	 * Tests a C5/D5, 1/4. Stem: left, down. Width: 1x quarter.
	 */
	@Test public void testChordC5D5() {
		Chord chord = chord(new Pitch[] { pi(0, 0, 5), pi(1, 0, 5) }, fr(1, 4));
		NotesNotation notes = testee.compute(chord, StemDirection.Down, cw, context);
		;
		assertEquals(n, notes.stemOffsetIs, Df);
		assertEquals(2 * n, notes.widthIs, Df);
		NoteDisplacement note = notes.getNote(0);
		assertEquals(5, note.lp);
		assertEquals(0, note.xIs, Df);
		assertEquals(NoteSuspension.Left, note.suspension);
		note = notes.getNote(1);
		assertEquals(6, note.lp);
		assertEquals(n, note.xIs, Df);
		assertEquals(NoteSuspension.None, note.suspension);
	}

	/**
	 * Tests a C4-E4-G4, 3/4. Stem: right, up. Width: 1x half + 1x dot.
	 */
	@Test public void testChordC4E4G4() {
		Chord chord = chord(new Pitch[] { pi(0, 0, 4), pi(2, 0, 4), pi(4, 0, 4) },
			fr(3, 4));
		NotesNotation notes = testee.compute(chord, StemDirection.Up, cw, context);
		;
		assertEquals(n, notes.stemOffsetIs, Df);
		assertEquals(n + dg, notes.widthIs, Df);
		assertEquals(0, notes.getNote(0).xIs, Df);
		assertEquals(NoteSuspension.None, notes.getNote(0).suspension);
		assertEquals(0, notes.getNote(1).xIs, Df);
		assertEquals(NoteSuspension.None, notes.getNote(1).suspension);
		assertEquals(0, notes.getNote(2).xIs, Df);
		assertEquals(NoteSuspension.None, notes.getNote(2).suspension);
	}

	/**
	 * Tests a A4-C5-D5, 1/4. Stem: left, down. Width: 2x quarter.
	 */
	@Test public void testChordA4C5D5() {
		Chord chord = chord(new Pitch[] { pi(5, 0, 4), pi(0, 0, 5), pi(1, 0, 5) },
			fr(1, 4));
		NotesNotation notes = testee.compute(chord, StemDirection.Down, cw, context);
		;
		assertEquals(n, notes.stemOffsetIs, Df);
		assertEquals(2 * n, notes.widthIs, Df);
		assertEquals(n, notes.getNote(0).xIs, Df);
		assertEquals(NoteSuspension.None, notes.getNote(0).suspension);
		assertEquals(0, notes.getNote(1).xIs, Df);
		assertEquals(NoteSuspension.Left, notes.getNote(1).suspension);
		assertEquals(n, notes.getNote(2).xIs, Df);
		assertEquals(NoteSuspension.None, notes.getNote(2).suspension);
	}

	/**
	 * Tests the dot positions for some chords.
	 */
	@Test public void testDotPositions() {
		//C5: position 6
		Chord chord = chord(pi(0, 0, 5), fr(3, 4));
		NotesNotation notes = testee.compute(chord, StemDirection.Down, cw, context);
		assertEquals(1, notes.getDotsPerNoteCount());
		assertEquals(1, notes.dotsLp.length);
		assertEquals(5, notes.dotsLp[0]);
		//B4: position 6
		chord = chord(pi(6, 0, 4), fr(7, 8));
		notes = testee.compute(chord, StemDirection.Down, cw, context);
		assertEquals(2, notes.getDotsPerNoteCount());
		assertEquals(1, notes.dotsLp.length);
		assertEquals(5, notes.dotsLp[0]);
		//F4, F4: position 1
		chord = chord(new Pitch[] { pi(3, 0, 4), pi(3, 0, 4) }, fr(7, 16));
		notes = testee.compute(chord, StemDirection.Down, cw, context);
		assertEquals(2, notes.getDotsPerNoteCount());
		assertEquals(1, notes.dotsLp.length);
		assertEquals(1, notes.dotsLp[0]);
		//F5, A5, B5: positions 7, 9, 11
		chord = chord(new Pitch[] { pi(3, 0, 5), pi(5, 0, 5), pi(6, 0, 5) }, fr(3, 2));
		notes = testee.compute(chord, StemDirection.Down, cw, context);
		assertEquals(1, notes.getDotsPerNoteCount());
		assertEquals(3, notes.dotsLp.length);
		assertEquals(7, notes.dotsLp[0]);
		assertEquals(9, notes.dotsLp[1]);
		assertEquals(11, notes.dotsLp[2]);
	}

	/**
	 * Tests a C5/C5 (unison) chord, 1/4. Stem: left, down. Width: 2x quarter.
	 */
	@Test public void testChordC5C5() {
		Chord chord = chord(new Pitch[] { pi(0, 0, 5), pi(0, 0, 5) }, fr(1, 4));
		NotesNotation notes = testee.compute(chord, StemDirection.Down, cw, context);
		assertEquals(n, notes.stemOffsetIs, Df);
		assertEquals(2 * n, notes.widthIs, Df);
		NoteDisplacement note = notes.getNote(0);
		assertEquals(5, note.lp);
		assertEquals(0, note.xIs, Df);
		assertEquals(NoteSuspension.Left, note.suspension);
		note = notes.getNote(1);
		assertEquals(5, note.lp);
		assertEquals(n, note.xIs, Df);
		assertEquals(NoteSuspension.None, note.suspension);
	}

}
