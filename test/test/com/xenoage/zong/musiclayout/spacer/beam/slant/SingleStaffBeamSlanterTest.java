package com.xenoage.zong.musiclayout.spacer.beam.slant;

import com.xenoage.utils.collections.CList;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.spacer.beam.Slant;
import com.xenoage.zong.musiclayout.spacer.beam.stem.BeamedStem;
import com.xenoage.zong.musiclayout.spacer.beam.stem.BeamedStems;
import lombok.val;
import material.Examples;
import material.Suite;
import material.beam.slant.ChlapikBeamSlant;
import material.beam.slant.Example;
import material.beam.slant.RossBeamSlant;
import org.junit.Test;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.slant;
import static com.xenoage.zong.musiclayout.spacer.beam.slant.SingleStaffBeamSlanter.singleStaffBeamSlanter;
import static java.lang.Math.round;
import static org.junit.Assert.*;

/**
 * Tests for {@link SingleStaffBeamSlanter}.
 * 
 * @author Andreas Wenger
 */
public class SingleStaffBeamSlanterTest {
	
	private SingleStaffBeamSlanter testee = singleStaffBeamSlanter;

	
	@Test public void containsMiddleExtremumTest() {
		//inspired by Ross, p. 115, row 2 and 3
		assertTrue(testee.containsMiddleExtremeNote(noteLps(7, 3, 5), Down));
		assertFalse(testee.containsMiddleExtremeNote(noteLps(7, 3, 5), Up));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(8, 6, 9), Down));
		assertFalse(testee.containsMiddleExtremeNote(noteLps(8, 6, 9), Up));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(1, 4, 2), Up));
		assertFalse(testee.containsMiddleExtremeNote(noteLps(1, 4, 2), Down));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(3, 4, 1), Up));
		assertFalse(testee.containsMiddleExtremeNote(noteLps(3, 4, 1), Down));
		//inspired by Ross, p. 115, rows 4
		assertTrue(testee.containsMiddleExtremeNote(noteLps(3, 1, 3, 1), Up));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(3, 10, 3, 10), Down));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(3, 10, 3, 10, 3, 10, 3, 10), Down));
		//inspired by Ross, p. 115, row 5
		assertTrue(testee.containsMiddleExtremeNote(noteLps(10, 6, 7, 9), Down));
		assertFalse(testee.containsMiddleExtremeNote(noteLps(10, 6, 7, 9), Up));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(10, 6, 11, 9), Down));
		//inspired by Ross, p. 115, row 6
		assertTrue(testee.containsMiddleExtremeNote(noteLps(5, 4, 8, 7), Down));
		//inspired by Ross, p. 116, rows 1-2
		assertTrue(testee.containsMiddleExtremeNote(noteLps(5, 7, 2, 7), Down));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(9, 6, 10, 11), Down));
		//inspired by Ross, p. 116, rows 3-6
		assertTrue(testee.containsMiddleExtremeNote(noteLps(12, 5, 5, 5), Down));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(5, 5, 5, 12), Down));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(2, 2, 2, -3), Up));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(-4, 1, 1, 1), Up));
		//inspired by Ross, p. 116, row 7
		assertTrue(testee.containsMiddleExtremeNote(noteLps(1, 4, 3, 2), Up));
		assertTrue(testee.containsMiddleExtremeNote(noteLps(1, 2, 2, 0), Up));
	}
	
	@Test public void is3NotesMiddleEqualsOuterTest() {
		//inspired by Ross, p. 97, row 3
		assertTrue(testee.is3NotesMiddleEqualsOuter(noteLps(7, 5, 5)));
		assertTrue(testee.is3NotesMiddleEqualsOuter(noteLps(5, 5, 7)));
		assertTrue(testee.is3NotesMiddleEqualsOuter(noteLps(0, 2, 2)));
		assertTrue(testee.is3NotesMiddleEqualsOuter(noteLps(2, 2, 0)));
		assertFalse(testee.is3NotesMiddleEqualsOuter(noteLps(7, 5, 7)));
		assertFalse(testee.is3NotesMiddleEqualsOuter(noteLps(4, 5, 6)));
	}
	
	@Test public void get4NotesRossSpecialDirTest() {
		//inspired by Ross, p. 97, row 4
		assertEquals(-1, testee.get4NotesRossSpecialDir(noteLps(7, 8, 5, 5), Down)); //Ross
		assertEquals(0, testee.get4NotesRossSpecialDir(noteLps(7, 7, 5, 5), Down));
		assertEquals(0, testee.get4NotesRossSpecialDir(noteLps(7, 5, 8, 5), Down));
		assertEquals(1, testee.get4NotesRossSpecialDir(noteLps(5, 5, 8, 7), Down)); //Ross
		assertEquals(0, testee.get4NotesRossSpecialDir(noteLps(5, 5, 7, 7), Down));
		assertEquals(0, testee.get4NotesRossSpecialDir(noteLps(5, 8, 5, 7), Down));
		assertEquals(1, testee.get4NotesRossSpecialDir(noteLps(1, 0, 3, 3), Up)); //Ross
		assertEquals(0, testee.get4NotesRossSpecialDir(noteLps(1, 1, 3, 3), Up));
		assertEquals(0, testee.get4NotesRossSpecialDir(noteLps(1, 3, 0, 3), Up));
		assertEquals(-1, testee.get4NotesRossSpecialDir(noteLps(3, 3, 0, 1), Up)); //Ross
		assertEquals(0, testee.get4NotesRossSpecialDir(noteLps(3, 3, 1, 1), Up));
		assertEquals(0, testee.get4NotesRossSpecialDir(noteLps(3, 0, 3, 1), Up));
	}
	
	@Test public void getInnerRunDirTest() {
		//inspired by Ross, p. 97, row 5-6
		assertEquals(1, testee.getInnerRunDir(noteLps(6, 2, 3, 4, 5, 7))); //Ross
		assertEquals(1, testee.getInnerRunDir(noteLps(6, 2, 4, 6, 8, 10))); //not a scale, but ascending
		assertEquals(0, testee.getInnerRunDir(noteLps(6, 6, 4, 6, 8, 10))); //6-6-4
		assertEquals(0, testee.getInnerRunDir(noteLps(6, 2, 4, 4, 8, 10))); //2x 4
		assertEquals(0, testee.getInnerRunDir(noteLps(6, 2, 4, 6, 10, 10))); //2x 10
		assertEquals(0, testee.getInnerRunDir(noteLps(6, 2, 4, 9, 8, 10))); //4-9-8
		assertEquals(-1, testee.getInnerRunDir(noteLps(6, 5, 4, 3, 2, 7))); //Ross
		assertEquals(-1, testee.getInnerRunDir(noteLps(10, 9, 8, 3, 2, 7))); //not a scale, but descending
		assertEquals(0, testee.getInnerRunDir(noteLps(10, 10, 8, 3, 2, 7))); //2x 10
		assertEquals(0, testee.getInnerRunDir(noteLps(10, 9, 9, 3, 2, 7))); //2x 9
		assertEquals(0, testee.getInnerRunDir(noteLps(10, 9, 8, 3, 7, 7))); //3-7-7
		assertEquals(0, testee.getInnerRunDir(noteLps(10, 9, 8, 3, 4, 7))); //3-4-7
	}
	
	@Test public void isCloseSpacingTest() {
		assertFalse(testee.isCloseSpacing(stemXIss(2, 6)));
		assertTrue(testee.isCloseSpacing(stemXIss(2, 5)));
		assertFalse(testee.isCloseSpacing(stemXIss(10, 14, 17, 21))); //avg 3.67
		assertTrue(testee.isCloseSpacing(stemXIss(10, 14, 17, 20))); //avg 3.3
	}
	
	@Test public void computeCloseTest() {
		Examples.test(Example.all, "close", 9, (suite, example) -> {
			float expectedSlant = example.getSlantIs();
			Slant slant = testee.computeClose(example.getStems(), example.getStemDir());
			assertSlantContains(expectedSlant, slant, example.name);
		});
	}
	
	@Test public void computeNormalTest() {
		Examples.test(Example.all, "interval", 14, (suite, example) -> {
			float expectedSlant = example.getSlantIs();
			Slant slant = testee.computeNormal(example.leftNoteLp, example.rightNoteLp);
			assertSlantContains(expectedSlant, slant, example.name);
		});
	}
	
	private void assertSlantContains(float expectedIs, Slant slant, String testName) {
		int expectedQs = round(expectedIs * 4);
		if (false == slant.contains(expectedQs))
			fail(testName + ": expected slant " + expectedQs + ", but not in repetition of " + slant);
	}
	
	@Test public void limitSlantForExtremeNotesTest() {
		//inspired by Ross, p. 111, row 1-2
		assertEquals(0.5 * 4, testee.limitSlantForExtremeNotes(
			slant(2), noteLps(-9, -3), Up, 5).maxAbsQs, df); //Ross
		assertEquals(0.5 * 4, testee.limitSlantForExtremeNotes(
			slant(2), noteLps(-4, -3), Up, 5).maxAbsQs, df); //Ross
		assertEquals(0.5 * 4, testee.limitSlantForExtremeNotes(
			slant(-2), noteLps(16, 12), Down, 5).maxAbsQs, df); //Ross
		assertEquals(0.5 * 4, testee.limitSlantForExtremeNotes(
			slant(-2), noteLps(12, 12), Down, 5).maxAbsQs, df); //Ross
		assertEquals(2 * 4, testee.limitSlantForExtremeNotes(
			slant(2), noteLps(-9, -2), Up, 5).maxAbsQs, df); //too high
		assertEquals(2 * 4, testee.limitSlantForExtremeNotes(
			slant(2), noteLps(-4, -3), Down, 5).maxAbsQs, df); //low, but stem down
		assertEquals(2 * 4, testee.limitSlantForExtremeNotes(
			slant(-2), noteLps(16, 10), Down, 5).maxAbsQs, df); //too low
		assertEquals(2 * 4, testee.limitSlantForExtremeNotes(
			slant(-2), noteLps(12, 12), Up, 5).maxAbsQs, df); //high, but stem up
	}
	
	@Test public void computeTest() {
		//use tests from Ross and Chlapik
		for (Suite<Example> suite : alist(new RossBeamSlant(), new ChlapikBeamSlant())) {
			for (Example example : suite.getExamples()) {
				float expectedSlant = example.getSlantIs();
				Slant slant = testee.compute(example.getStems(), example.staffLines);
				assertSlantContains(expectedSlant, slant, suite.getName() + ": " + example.name);
			}
		}
	}

	private BeamedStems noteLps(int... noteLps) {
		val stems = new CList<BeamedStem>();
		for (int noteLp : noteLps)
			stems.add(new BeamedStem(0, StemDirection.None, noteLp, 0));
		return new BeamedStems(stems.close());
	}

	private BeamedStems stemXIss(int... stemXIss) {
		val stems = new CList<BeamedStem>();
		for (int stemXIs : stemXIss)
			stems.add(new BeamedStem(stemXIs, StemDirection.None, 0, 0));
		return new BeamedStems(stems.close());
	}

}
