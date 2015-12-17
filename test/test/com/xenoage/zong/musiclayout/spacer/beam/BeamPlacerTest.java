package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.core.music.StaffLines.staff5Lines;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.notation.BeamNotation.lineHeightIs;
import static com.xenoage.zong.musiclayout.notator.chord.stem.StemDrawer.stemDrawer;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.fromLp;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamPlacer.beamPlacer;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSlanter.beamSlanter;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.slant;
import static java.lang.Math.abs;
import static material.ExampleResult.accepted;
import static material.ExampleResult.failed;
import static material.ExampleResult.perfect;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.xenoage.utils.collections.CollectionUtils;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.chord.ChordLps;
import com.xenoage.zong.musiclayout.notator.ChordNotator;
import com.xenoage.zong.musiclayout.notator.chord.stem.StemDirector;
import com.xenoage.zong.musiclayout.notator.chord.stem.StemDrawer;

import material.ExampleResult;
import material.Examples;
import material.ExampleResult.Result;
import material.beam.slant.Example;
import material.beam.slant.RossBeamSlant;
import material.beam.stafftouch.TouchExample;

/**
 * Tests for {@link BeamPlacer}.
 * 
 * @author Andreas Wenger
 */
public class BeamPlacerTest {
	
	private BeamPlacer testee = beamPlacer;

	@Test public void getPlacementTest() {
		//exact result: no rounding required
		assertEqualsPlacement(new Placement(2, 3.5f), testee.getPlacement(2, 7, 3, 2.3f, 0.75f));
		//0.4 quarter spaces (= 0.2 LP) higher: should be rounded down to same result
		assertEqualsPlacement(new Placement(2, 3.5f), testee.getPlacement(2, 7, 3, 2.3f + 0.2f, 0.75f));
		//0.6 quarter spaces (= 0.3 LP) higher: should be rounded up to the next quarter space
		assertEqualsPlacement(new Placement(2.5f, 4), testee.getPlacement(2, 7, 3, 2.3f + 0.3f, 0.75f));
	}
	
	private void assertEqualsPlacement(Placement expected, Placement actual) {
		assertEquals(expected.leftEndLp, actual.leftEndLp, df);
		assertEquals(expected.rightEndLp, actual.rightEndLp, df);
	}
	
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
			assertEquals(lp, testee.getDistanceToLineLp(lp, i, slant, left, right), df);
		//ascending line
		slant = 2;
		assertEquals(lp, testee.getDistanceToLineLp(lp, left, slant, left, right), df);
		assertEquals(lp - 4/3f, testee.getDistanceToLineLp(lp, 3, slant, left, right), df);
		assertEquals(lp - 4, testee.getDistanceToLineLp(lp, right, slant, left, right), df);
		//descending line
		slant = -4;
		assertEquals(lp, testee.getDistanceToLineLp(lp, left, slant, left, right), df);
		assertEquals(lp + 8/3f, testee.getDistanceToLineLp(lp, 3, slant, left, right), df);
		assertEquals(lp + 8, testee.getDistanceToLineLp(lp, right, slant, left, right), df);
	}
	
	
	@Test public void shortenTest() {
		//the following tests use arbitrary horizontal positions, but normal spacing (not close spacing)
		//p104 r1 c1: could be 3.5/sit, but is 3.25/straddle
		Placement old = new Placement(5f, 5f);
		Placement shortened = testee.shorten(old, Up, new int[]{-2, -2},
			new float[]{5, 10}, 1, staff5Lines);
		assertEquals(new Placement(4.5f, 4.5f), shortened);
		//p104 r6 c1: could be 3.75/straddle and 3.5/hang, but is 3.5/sit and 3.25/straddle
		old = new Placement(-2.5f, -3f);
		shortened = testee.shorten(old, Down, new int[]{5, 4},
			new float[]{10, 15}, 1, staff5Lines);
		assertEquals(new Placement(-2f, -2.5f), shortened);
		//p104 r7 c1: is not shortened, since then a stem would be shorter than 3 spaces
		old = new Placement(-1f, -0.5f);
		shortened = testee.shorten(old, Down, new int[]{5, 6},
			new float[]{0, 5}, 1, staff5Lines);
		assertEquals(old, shortened);
		//p105 r1 c1: not shortened, because beam line would be within white space
		old = new Placement(6f, 6f);
		shortened = testee.shorten(old, Up, new int[]{-1, -1},
			new float[]{0, 8}, 1, staff5Lines);
		assertEquals(old, shortened);
		//p105 r1 c2: could be 3.5/hang, but is 3.25/staddle
		old = new Placement(-1f, -1f);
		shortened = testee.shorten(old, Down, new int[]{6, 6},
			new float[]{10, 15}, 1, staff5Lines);
		assertEquals(new Placement(-0.5f, -0.5f), shortened);
	}
	
	
	/**
	 * Tests with examples from Ross.
	 */
	@Test public void computeForOneStaffTestRoss() {
		List<Example> examples = new RossBeamSlant().getExamples();
		List<ExampleResult> results = alist();
		for (Example example : examples) {
			//collect data
			int notesLp[] = example.getNotesLp();
			StemDirection stemDir = example.getStemDir();
			float[] stemsXIs = getStemsXIs(example, notesLp.length);
			Slant slant = beamSlanter.compute(notesLp, stemDir, stemsXIs, 5);
			float[] stemsLengthIs = new float[notesLp.length];
			for (int i : range(notesLp)) {
				stemsLengthIs[i] = stemDrawer.getPreferredStemLengthIs(
					new ChordLps(notesLp[i]), stemDir, staff5Lines);
			}
			//run test
			Placement offset = testee.compute(slant, notesLp, stemDir, stemsXIs,
				stemsLengthIs, 1, StaffLines.staff5Lines);
			//check result
			ExampleResult result = check(offset, example);
			results.add(result);
		}
		//success, when 100% of the examples are perfect or at least accepted
		//print accepted and failed results
		int perfect = 0, accepted = 0, failed = 0;
		for (ExampleResult result : results) {
			if (result.getResult() != Result.Perfect) {
				System.out.print(result.getExample().getName() + ": ");
				if (result.getResult() == Result.Accepted) {
					accepted++;
					System.out.print("not perfect, but accepted");
				}
				else {
					failed++;
					System.out.print("FAILED");
				}
				if (result.getComment() != null)
					System.out.print("; " + result.getComment());
				System.out.println();
			}
			else {
				perfect++;
			}
		}
		System.out.println(BeamPlacerTest.class.getSimpleName() + ": " +
			perfect + " perfect, " + accepted + " accepted, " + failed + " failed");
		if (failed > 0)
			fail();
	}
	
	private float[] getStemsXIs(Example example, int chordsCount) {
		float[] stemsXIs = new float[chordsCount];
		float distance = example.getStemsDistanceIs();
		for (int i : range(chordsCount))
			stemsXIs[i] = i * distance;
		return stemsXIs;
	}
	
	private ExampleResult check(Placement actual, Example example) {
		Placement expected = example.getPlacement();
		//check result
		if (abs(actual.leftEndLp - expected.leftEndLp) < df &&
			abs(actual.rightEndLp - expected.rightEndLp) < df) {
			//perfect solution
			return perfect(example);
		}
		else {
			//not the perfect solution, but maybe it is still ok
			//it is still ok, when the slant is smaller than expected and when
			//the beam lines have a valid anchor
			String comment = "expected [" + expected.leftEndLp + "," + expected.rightEndLp +
				"] but was [" + actual.leftEndLp + "," + actual.rightEndLp + "]";
			if (isAcceptedBeam(expected, actual, example.getStemDir()))
				return accepted(example, comment);
			else
				return failed(example, comment);
		}
	}
	
	/**
	 * Checks, if the given 8th beam line in a 5 line staff is at least accepted.
	 * It is accepted, when it meets the following minimal requirements defined by Ross:
	 * <ul>
	 * 	<li>When the slant is smaller than expected, at most 1 IS in total
	 *      (p. 98: when in doubt, do not exceed a slant of one space)</li>
	 *  <li>When the beam touches a staff line, use the correct anchors to avoid
	 *      white edges (p. 98 bottom). Otherwise, this is not needed (p. 103,
	 *      last sentence before the box).</li>
	 * </ul>
	 */
	private boolean isAcceptedBeam(Placement expected, Placement actual, StemDirection stemDir) {
		//amout of slant: must be equal or smaller than expected, but at most 1 IS
		float absSlantExpected = abs(expected.leftEndLp - expected.rightEndLp);
		float absSlantActual = abs(actual.leftEndLp - actual.rightEndLp);
		if (absSlantActual > absSlantExpected || absSlantActual > 1 * 2 /* 1 IS */)
			return false;
		//when beam touches staff line, anchors must be correct
		if (beamPlacer.isTouchingStaff( //method is tested, so we can use it here
			actual, stemDir, lineHeightIs, staff5Lines)) {
			Anchor leftAnchor = fromLp(actual.leftEndLp, stemDir);
			Anchor rightAnchor = fromLp(actual.rightEndLp, stemDir);
			if (false == beamPlacer.isAnchor8thCorrect( //method is tested, so we can use it here
				leftAnchor, rightAnchor, actual.getDirection()))
				return false;
		}
		return true;
	}

}
