package com.xenoage.zong.musiclayout.notator.beam.range;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.beam.Beam.beamFromChords;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.notations.chord.NoteDisplacementTest.note;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NotesNotation;
import com.xenoage.zong.musiclayout.notator.beam.lines.BeamLinesRules;
import com.xenoage.zong.musiclayout.notator.beam.lines.Beam8thRules;

/**
 * Tests for {@link OneMeasureOneStaff}.
 * 
 * This test will only work correct if the values of Ted Ross
 * are selected in the {@link Beam8thRules} strategy.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class OneMeasureOneStaffTest {

	private OneMeasureOneStaff testee = new OneMeasureOneStaff();
	
	private int staff5Lines = 5;
	private int beam8th = 1;
	private int beam16th = 2;
	
	private float[] twoStemsX = { 2, 8 };
	private float[] twoStemsCloserX = { 2, 4 };
	private float[] fourStemsX = { 2, 4, 6, 8 };
	private float[] sixStemsX = { 2, 3, 4, 5, 6, 8 };
	

	@Test public void computeTestBottom() {
		int[][] notes = { { 6 }, { 7 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, twoStemsX, staff5Lines, beam8th, Down);
		//check
		assertEquals(-0.5, chords.get(0).stem.endLp, df);
		assertEquals(0, chords.get(1).stem.endLp, df);
	}
	
	@Test public void computeTestTop() {
		int[][] notes = { { -1, 1 }, { -1, 1 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, twoStemsX, staff5Lines, beam8th, Up);
		//check
		assertEquals(8, chords.get(0).stem.endLp, df);
		assertEquals(8, chords.get(1).stem.endLp, df);
	}
	
	@Test public void computeTestMiddle() {
		int[][] notes = { { 6 }, { 2 }, { 3 }, { 4 }, { 5 }, { 7 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, sixStemsX, staff5Lines, beam8th, Down);
		//check
		assertEquals(1f, chords.get(5).stem.endLp - chords.get(0).stem.endLp, df);
		assertEquals(-4.5, chords.get(0).stem.endLp, df);
		assertEquals(-3.5, chords.get(5).stem.endLp, df);
	}
	
	/**
	 * Note: In this example, Ted Ross breaks his own rules!
	 */
	@Test public void computeTestBelowStaff() {
		int[][] notes = { { 7 }, { 8 }, { 5 }, { 5 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, fourStemsX, staff5Lines, beam8th, Down);
		//check
		assertEquals(-1, chords.get(0).stem.endLp, df);
		assertEquals(-2, chords.get(3).stem.endLp, df);
	}
	
	@Test public void computeTestMiddle2() {
		int[][] notes = { { 1 }, { -1 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, twoStemsX, staff5Lines, beam8th, Up);
		//check
		assertEquals(7, chords.get(0).stem.endLp, df);
		assertEquals(6, chords.get(1).stem.endLp, df);
	}
	
	@Test public void computeTestAboveStaff() {
		int[][] notes = { { 0 }, { -1 }, { 2 }, { 5 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, fourStemsX, staff5Lines, beam8th, Up);
		//check
		assertEquals(8.5, chords.get(0).stem.endLp, df);
		assertEquals(11, chords.get(3).stem.endLp, df);
	}
	
	@Test public void computeTestMiddle3() {
		int[][] notes = { { 11 }, { 15 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, twoStemsX, staff5Lines, beam8th, Down);
		//check
		assertEquals(3, chords.get(0).stem.endLp, df);
		assertEquals(4, chords.get(1).stem.endLp, df);
	}
	
	@Test public void computeTestMiddle4() {
		int[][] notes = { { 11 }, { 12 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, twoStemsX, staff5Lines, beam8th, Down);
		//check
		assertEquals(3.5, chords.get(0).stem.endLp, df);
		assertEquals(4, chords.get(1).stem.endLp, df);
	}
	
	@Test public void computeTestCloserSpacing() {
		int[][] notes = { { 9 }, { 5 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, twoStemsCloserX, staff5Lines, beam8th, Down);
		//check
		assertEquals(0, chords.get(0).stem.endLp, df);
		assertEquals(-1, chords.get(1).stem.endLp, df);
	}
	
	@Test public void computeTestMiddle5() {
		int[][] notes = { { 9 }, { 13 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, twoStemsX, staff5Lines, beam8th, Down);
		//check
		assertEquals(3, chords.get(0).stem.endLp, df);
		assertEquals(4, chords.get(1).stem.endLp, df);
	}
	
	@Test public void computeTestBottom2() {
		int[][] notes = { { 5 }, { 6 }, { 9 }, { 8 }, { 7 }, { 8 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, sixStemsX, staff5Lines, beam8th, Down);
		//check
		assertEquals(-1, chords.get(0).stem.endLp, df);
		assertEquals(1, chords.get(5).stem.endLp, df);
	}
	
	@Test public void computeTestHorizontal1() {
		int[][] notes = { { 5 }, { 6 }, { 9 }, { 8 }, { 7 }, { 8 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, sixStemsX, staff5Lines, beam8th, Up);
		//check
		assertEquals(0, chords.get(5).stem.endLp - chords.get(0).stem.endLp, df);
	}
	
	@Test public void computeTestHorizontal2() {
		int[][] notes = { { -2 }, { -5 }, { -2 }, { -5 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, fourStemsX, staff5Lines, beam8th, Up);
		//check
		assertEquals(4, chords.get(0).stem.endLp, df);
		assertEquals(4, chords.get(3).stem.endLp, df);
	}
	
	@Test public void computeTest16th() {
		int[][] notes = { { -2 }, { -5 }, { -2 }, { -5 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, fourStemsX, staff5Lines, beam16th, Up);
		//check
		assertEquals(4.5, chords.get(0).stem.endLp, df);
		assertEquals(4.5, chords.get(3).stem.endLp, df);
	}
	
	@Test public void computeTestHorizontal3() {
		int[][] notes = { { 13 }, { 11 }, { 12 }, { 11 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, fourStemsX, staff5Lines, beam8th, Down);
		//check
		assertEquals(4, chords.get(0).stem.endLp, df);
		assertEquals(4, chords.get(3).stem.endLp, df);
	}
	
	@Test public void computeTest16th2() {
		int[][] notes = { { 13 }, { 11 }, { 12 }, { 11 } };
		List<ChordNotation> chords = createChordNotations(notes);
		//compute
		testee.compute(null, chords, fourStemsX, staff5Lines, beam16th, Down);
		//check
		assertEquals(4, chords.get(0).stem.endLp, df);
		assertEquals(4, chords.get(3).stem.endLp, df);
	}

	private List<ChordNotation> createChordNotations(int[][] notes) {
		List<ChordNotation> ret = alist(notes.length);
		for (int i : range(notes)) {
			NoteDisplacement notesDis[] = new NoteDisplacement[notes[i].length];
			for (int iNote : range(notesDis))
				notesDis[iNote] = note(notes[i][iNote]);
			NotesNotation notesNot = new NotesNotation(1, 1, notesDis, new float[] {}, new int[] { 1 }, 0, false);
			ChordNotation chordNot = new ChordNotation(null);
			chordNot.notes = notesNot;
			ret.add(chordNot);
		}
		return ret;
	}

	

}
