package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamPlacer.beamOffsetter;
import static org.junit.Assert.assertEquals;
import material.Examples;
import material.beam.stafftouch.TouchExample;

import org.junit.Test;

/**
 * Tests for {@link BeamPlacer}.
 * 
 * @author Andreas Wenger
 */
public class BeamPlacerTest {
	
	private BeamPlacer testee = beamOffsetter;

	
	@Test public void isTouchingStaffTest() {
		Examples.test(TouchExample.all, (suite, example) -> {
			boolean expected = example.touch;
			boolean touch = testee.isTouchingStaff(example.placement, example.stemDir,
				example.beamHeightIs, example.staffLines);
			assertEquals(suite.getName() + ": " + example.name, expected, touch);
		});
	}
	
	@Test public void getDictatorStemIndexTest() {
		float[] endLps = new float[]{10, 11, 13};
		float[] xs = new float[]{2, 4, 6};
		//horizontal line, downstem (find min LP)
		assertEquals(0, testee.getDictatorStemIndex(endLps, xs, 0, Down));
		//horizontal line, upstem (find max LP)
		assertEquals(2, testee.getDictatorStemIndex(endLps, xs, 0, Up));
		//ascending line, downstem (find min LP)
		assertEquals(0, testee.getDictatorStemIndex(endLps, xs, 0.95f, Down));
		assertEquals(1, testee.getDictatorStemIndex(endLps, xs, 1.05f, Down));
		assertEquals(1, testee.getDictatorStemIndex(endLps, xs, 1.95f, Down));
		assertEquals(2, testee.getDictatorStemIndex(endLps, xs, 2.05f, Down));
		//ascending line, upstem (find max LP)
		assertEquals(2, testee.getDictatorStemIndex(endLps, xs, 0.95f, Up));
		assertEquals(2, testee.getDictatorStemIndex(endLps, xs, 1.05f, Up));
		assertEquals(0, testee.getDictatorStemIndex(endLps, xs, 1.95f, Up));
		assertEquals(0, testee.getDictatorStemIndex(endLps, xs, 2.05f, Up));
		
		endLps = new float[]{13, 11, 10};
		xs = new float[]{2, 4, 6};
		//descending line, downstem (find min LP)
		assertEquals(2, testee.getDictatorStemIndex(endLps, xs, -0.95f, Down));
		assertEquals(1, testee.getDictatorStemIndex(endLps, xs, -1.05f, Down));
		assertEquals(1, testee.getDictatorStemIndex(endLps, xs, -1.95f, Down));
		assertEquals(0, testee.getDictatorStemIndex(endLps, xs, -2.05f, Down));
		//descending line, upstem (find max LP)
		assertEquals(0, testee.getDictatorStemIndex(endLps, xs, -0.95f, Up));
		assertEquals(0, testee.getDictatorStemIndex(endLps, xs, -1.05f, Up));
		assertEquals(2, testee.getDictatorStemIndex(endLps, xs, -1.95f, Up));
		assertEquals(2, testee.getDictatorStemIndex(endLps, xs, -2.05f, Up));
	}
	
	@Test public void getDistanceToLineIsTest() {
		int left = 2;
		int right = 5;
		float lp = 3;
		//horizontal line
		float slant = 0;
		for (int i : range(left, right))
			assertEquals(lp, testee.getDistanceToLineIs(lp, i, slant, left, right), df);
		//ascending line
		slant = 2;
		assertEquals(lp, testee.getDistanceToLineIs(lp, left, slant, left, right), df);
		assertEquals(lp - 4/3f, testee.getDistanceToLineIs(lp, 3, slant, left, right), df);
		assertEquals(lp - 4, testee.getDistanceToLineIs(lp, right, slant, left, right), df);
		//descending line
		slant = -4;
		assertEquals(lp, testee.getDistanceToLineIs(lp, left, slant, left, right), df);
		assertEquals(lp + 8/3f, testee.getDistanceToLineIs(lp, 3, slant, left, right), df);
		assertEquals(lp + 8, testee.getDistanceToLineIs(lp, right, slant, left, right), df);
	}
	
	
	/**
	 * Tests with examples from Ross.
	 * /
	@Test public void computeForOneStaffTestRoss() {
		List<String> failed = alist();
		List<Example> examples = new RossBeamSlant().examples;
		for (Example example : examples) {
			//collect data
			int notesLp[] = getNotesLp(example);
			StemDirection stemDir = getStemDir(example.stemDir, notesLp);
			float[] stemsXIs = getStemsXIs(example, notesLp.length);
			//run test
			Placement offset = testee.computeForOneStaff(notesLp, stemDir, stemsXIs, 1, staffLines);
			//check result
			String failMessage = check(offset, example, stemDir);
			if (failMessage != null)
				failed.add(failMessage);
		}
		//success, when >95% of the examples are correct
		if (1.0 * failed.size() / examples.size() > 0.05)
			fail("Beam slanting incorrect for " + failed.size() + " of " + examples.size() +
				" examples: \n" + failed);
	}
	
	private float[] getStemsXIs(Example example, int chordsCount) {
		float[] stemsXIs = new float[chordsCount];
		float distance = example.getStemsDistanceIs();
		for (int i : range(chordsCount))
			stemsXIs[i] = i * distance;
		return stemsXIs;
	}
	
	private String check(Placement offset, Example example, StemDirection stemDir) {
		float expectedLeftEndLp = example.leftNoteLp +
			stemDir.getSign() * example.leftStemLengthIs * 2;
		float expectedRightEndLp = example.rightNoteLp +
			stemDir.getSign() * example.rightStemLengthIs * 2;
		if (abs(offset.leftEndLp - expectedLeftEndLp) < df &&
			abs(offset.rightEndLp - expectedRightEndLp) < df) {
			return null; //ok
		}
		else {
			return example.name + ": expected [" + expectedLeftEndLp + "," + expectedRightEndLp +
				"] but was [" + offset.leftEndLp + "," + offset.rightEndLp + "]";
		}
	} */

}
