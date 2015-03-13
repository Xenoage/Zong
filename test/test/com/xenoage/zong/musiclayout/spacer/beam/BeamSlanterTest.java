package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSlanter.beamSlanter;
import static material.Examples.filter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import material.Suite;
import material.beam.slant.ChlapikBeamSlant;
import material.beam.slant.Example;
import material.beam.slant.RossBeamSlant;

import org.junit.Test;

/**
 * Tests for {@link BeamSlanter}.
 * 
 * @author Andreas Wenger
 */
public class BeamSlanterTest {
	
	private BeamSlanter testee = beamSlanter;
	
	
	@Test public void isFirstAndLastNoteEqualTest() {
		//inspired by Ross, p. 115, row 1
		assertTrue(testee.isFirstAndLastNoteEqual(new int[]{5, 6, 4, 5}));
		assertFalse(testee.isFirstAndLastNoteEqual(new int[]{5, 6, 4, 6}));
	}
	
	@Test public void containsMiddleExtremumTest() {
		//inspired by Ross, p. 115, row 2 and 3
		assertTrue(testee.containsMiddleExtremum(new int[]{7, 3, 5}, Down));
		assertFalse(testee.containsMiddleExtremum(new int[]{7, 3, 5}, Up));
		assertTrue(testee.containsMiddleExtremum(new int[]{8, 6, 9}, Down));
		assertFalse(testee.containsMiddleExtremum(new int[]{8, 6, 9}, Up));
		assertTrue(testee.containsMiddleExtremum(new int[]{1, 4, 2}, Up));
		assertFalse(testee.containsMiddleExtremum(new int[]{1, 4, 2}, Down));
		assertTrue(testee.containsMiddleExtremum(new int[]{3, 4, 1}, Up));
		assertFalse(testee.containsMiddleExtremum(new int[]{3, 4, 1}, Down));
		//inspired by Ross, p. 115, rows 4
		assertTrue(testee.containsMiddleExtremum(new int[]{3, 1, 3, 1}, Up));
		assertTrue(testee.containsMiddleExtremum(new int[]{3, 10, 3, 10}, Down));
		assertTrue(testee.containsMiddleExtremum(new int[]{3, 10, 3, 10, 3, 10, 3, 10}, Down));
		//inspired by Ross, p. 115, row 5
		assertTrue(testee.containsMiddleExtremum(new int[]{10, 6, 7, 9}, Down));
		assertFalse(testee.containsMiddleExtremum(new int[]{10, 6, 7, 9}, Up));
		assertTrue(testee.containsMiddleExtremum(new int[]{10, 6, 11, 9}, Down));
		//inspired by Ross, p. 115, row 6
		assertTrue(testee.containsMiddleExtremum(new int[]{5, 4, 8, 7}, Down));
		//inspired by Ross, p. 116, rows 1-2
		assertTrue(testee.containsMiddleExtremum(new int[]{5, 7, 2, 7}, Down));
		assertTrue(testee.containsMiddleExtremum(new int[]{9, 6, 10, 11}, Down));
		//inspired by Ross, p. 116, rows 3-6
		assertTrue(testee.containsMiddleExtremum(new int[]{12, 5, 5, 5}, Down));
		assertTrue(testee.containsMiddleExtremum(new int[]{5, 5, 5, 12}, Down));
		assertTrue(testee.containsMiddleExtremum(new int[]{2, 2, 2, -3}, Up));
		assertTrue(testee.containsMiddleExtremum(new int[]{-4, 1, 1, 1}, Up));
		//inspired by Ross, p. 116, row 7
		assertTrue(testee.containsMiddleExtremum(new int[]{1, 4, 3, 2}, Up));
		assertTrue(testee.containsMiddleExtremum(new int[]{1, 2, 2, 0}, Up));
	}
	
	@Test public void is3NotesMiddleEqualsOuterTest() {
		//inspired by Ross, p. 97, row 3
		assertTrue(testee.is3NotesMiddleEqualsOuter(new int[]{7, 5, 5}));
		assertTrue(testee.is3NotesMiddleEqualsOuter(new int[]{5, 5, 7}));
		assertTrue(testee.is3NotesMiddleEqualsOuter(new int[]{0, 2, 2}));
		assertTrue(testee.is3NotesMiddleEqualsOuter(new int[]{2, 2, 0}));
		assertFalse(testee.is3NotesMiddleEqualsOuter(new int[]{7, 5, 7}));
		assertFalse(testee.is3NotesMiddleEqualsOuter(new int[]{4, 5, 6}));
	}
	
	@Test public void get4NotesRossSpecialDirTest() {
		//inspired by Ross, p. 97, row 4
		assertEquals(-1, testee.get4NotesRossSpecialDir(new int[]{7, 8, 5, 5}, Down)); //Ross
		assertEquals(0, testee.get4NotesRossSpecialDir(new int[]{7, 7, 5, 5}, Down));
		assertEquals(0, testee.get4NotesRossSpecialDir(new int[]{7, 5, 8, 5}, Down));
		assertEquals(1, testee.get4NotesRossSpecialDir(new int[]{5, 5, 8, 7}, Down)); //Ross
		assertEquals(0, testee.get4NotesRossSpecialDir(new int[]{5, 5, 7, 7}, Down));
		assertEquals(0, testee.get4NotesRossSpecialDir(new int[]{5, 8, 5, 7}, Down));
		assertEquals(1, testee.get4NotesRossSpecialDir(new int[]{1, 0, 3, 3}, Up)); //Ross
		assertEquals(0, testee.get4NotesRossSpecialDir(new int[]{1, 1, 3, 3}, Up));
		assertEquals(0, testee.get4NotesRossSpecialDir(new int[]{1, 3, 0, 3}, Up));
		assertEquals(-1, testee.get4NotesRossSpecialDir(new int[]{3, 3, 0, 1}, Up)); //Ross
		assertEquals(0, testee.get4NotesRossSpecialDir(new int[]{3, 3, 1, 1}, Up));
		assertEquals(0, testee.get4NotesRossSpecialDir(new int[]{3, 0, 3, 1}, Up));
	}
	
	@Test public void getInnerRunDirTest() {
		//inspired by Ross, p. 97, row 5-6
		assertEquals(1, testee.getInnerRunDir(new int[]{6, 2, 3, 4, 5, 7})); //Ross
		assertEquals(1, testee.getInnerRunDir(new int[]{6, 2, 4, 6, 8, 10})); //not a scale, but ascending
		assertEquals(0, testee.getInnerRunDir(new int[]{6, 6, 4, 6, 8, 10})); //6-6-4
		assertEquals(0, testee.getInnerRunDir(new int[]{6, 2, 4, 4, 8, 10})); //2x 4
		assertEquals(0, testee.getInnerRunDir(new int[]{6, 2, 4, 6, 10, 10})); //2x 10
		assertEquals(0, testee.getInnerRunDir(new int[]{6, 2, 4, 9, 8, 10})); //4-9-8
		assertEquals(-1, testee.getInnerRunDir(new int[]{6, 5, 4, 3, 2, 7})); //Ross
		assertEquals(-1, testee.getInnerRunDir(new int[]{10, 9, 8, 3, 2, 7})); //not a scale, but descending
		assertEquals(0, testee.getInnerRunDir(new int[]{10, 10, 8, 3, 2, 7})); //2x 10
		assertEquals(0, testee.getInnerRunDir(new int[]{10, 9, 9, 3, 2, 7})); //2x 9
		assertEquals(0, testee.getInnerRunDir(new int[]{10, 9, 8, 3, 7, 7})); //3-7-7
		assertEquals(0, testee.getInnerRunDir(new int[]{10, 9, 8, 3, 4, 7})); //3-4-7
	}
	
	@Test public void isCloseSpacingTest() {
		assertFalse(testee.isCloseSpacing(new float[]{2, 6}));
		assertTrue(testee.isCloseSpacing(new float[]{2, 5}));
		assertFalse(testee.isCloseSpacing(new float[]{10, 14, 17, 21})); //avg 3.67
		assertTrue(testee.isCloseSpacing(new float[]{10, 14, 17, 20})); //avg 3.3
	}
	
	@Test public void computeCloseTest() {
		//use tests from Ross
		for (Example example : filter(RossBeamSlant.examples, "close", 9)) {
			float expectedSlant = example.getSlantIs();
			Slant slant = testee.computeClose(example.getNotesLp(), example.getStemDir());
			assertSlantContains(expectedSlant, slant, example.name);
		}
	}
	
	@Test public void computeNormalTest() {
		//use tests from Ross
		for (Example example : filter(RossBeamSlant.examples, "interval", 14)) {
			float expectedSlant = example.getSlantIs();
			Slant slant = testee.computeNormal(example.leftNoteLp, example.rightNoteLp);
			assertSlantContains(expectedSlant, slant, example.name);
		}
	}
	
	private void assertSlantContains(float expected, Slant slant, String testName) {
		if (false == slant.contains(expected))
			fail(testName + ": expected slant " + expected + ", but not in range of " + slant);
	}
	
	@Test public void limitSlantForExtremeNotesTest() {
		//inspired by Ross, p. 111, row 1-2
		assertEquals(0.5, testee.limitSlantForExtremeNotes(
			Slant.slant(2), new int[]{-9, -3}, Up, 5).minIs, df); //Ross
		assertEquals(0.5, testee.limitSlantForExtremeNotes(
			Slant.slant(2), new int[]{-4, -3}, Up, 5).minIs, df); //Ross
		assertEquals(-0.5, testee.limitSlantForExtremeNotes(
			Slant.slant(-2), new int[]{16, 12}, Down, 5).minIs, df); //Ross
		assertEquals(-0.5, testee.limitSlantForExtremeNotes(
			Slant.slant(-2), new int[]{12, 12}, Down, 5).minIs, df); //Ross
		assertEquals(2, testee.limitSlantForExtremeNotes(
			Slant.slant(2), new int[]{-9, -2}, Up, 5).minIs, df); //too high
		assertEquals(2, testee.limitSlantForExtremeNotes(
			Slant.slant(2), new int[]{-4, -3}, Down, 5).minIs, df); //low, but stem down
		assertEquals(-2, testee.limitSlantForExtremeNotes(
			Slant.slant(-2), new int[]{16, 10}, Down, 5).minIs, df); //too low
		assertEquals(-2, testee.limitSlantForExtremeNotes(
			Slant.slant(-2), new int[]{12, 12}, Up, 5).minIs, df); //high, but stem up
	}
	
	@Test public void computeTest() {
		//use tests from Ross and Chlapik
		for (Suite<Example> suite : alist(new RossBeamSlant(), new ChlapikBeamSlant())) {
			for (Example example : suite.getExamples()) {
				float expectedSlant = example.getSlantIs();
				Slant slant = testee.compute(example.getNotesLp(), example.getStemDir(),
					example.getStemsXIs(), example.staffLines);
				assertSlantContains(expectedSlant, slant, suite.getName() + ": " + example.name);
			}
		}
	}

}
