package com.xenoage.zong.musiclayout.notator.beam;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.HookLeft;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.HookRight;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.None;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.Start;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.Stop;
import static com.xenoage.zong.musiclayout.notator.beam.BeamFragmenter.beamFragmenter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import material.beam.fragments.ChlapikBeamFragments;

import org.junit.Test;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.notation.beam.Fragment;
import com.xenoage.zong.musiclayout.notation.beam.Fragments;

/**
 * Tests for {@link BeamFragmenter}.
 * 
 * @author Andreas Wenger
 */
public class BeamFragmenterTest {
	
	private BeamFragmenter testee = beamFragmenter;
	

	@Test public void computeTest() {
		ChlapikBeamFragments source = new ChlapikBeamFragments();
		Beam b;

		//example of row 1, column 1
		b = source.exampleRow1Col1();
		try {
			testee.compute(b, 0, null); //not working for 8th lines
			fail();
		} catch (IllegalArgumentException ex) {
			//ok
		}
		Fragments wp = testee.compute(b, 2, null);
		assertEqualsList(wp, None, None, None); //32th
		assertEqualsList(testee.compute(b, 1, wp), HookRight, None, HookLeft); //16th

		//example of row 1, column 2
		b = source.exampleRow1Col2();
		assertEqualsList(testee.compute(b, 1, null), HookRight, None, HookLeft); //16th

		//example of row 1, column 3
		b = source.exampleRow1Col3();
		assertEqualsList(testee.compute(b, 1, null), HookRight, None, None, HookLeft); //16th

		//example of row 1, column 4
		b = source.exampleRow1Col4();
		assertEqualsList(testee.compute(b, 1, null), HookRight, None, None, HookLeft); //16th

		//example of row 2, column 1
		b = source.exampleRow2Col1();
		assertEqualsList(testee.compute(b, 1, null), None, HookLeft); //16th

		//example of row 2, column 2
		b = source.exampleRow2Col2();
		assertEqualsList(testee.compute(b, 1, null), None, HookLeft); //16th

		//example of row 2, column 3
		b = source.exampleRow2Col3();
		wp = testee.compute(b, 2, null);
		assertEqualsList(wp, None, HookLeft, None, HookLeft); //32th
		assertEqualsList(testee.compute(b, 1, wp), Start, None, None, Stop); //16th

		//example of row 2, column 4
		b = source.exampleRow2Col4();
		wp = testee.compute(b, 2, null);
		assertEqualsList(wp, None, HookLeft, None, HookLeft); //32th
		assertEqualsList(testee.compute(b, 1, wp), Start, None, None, Stop); //16th

		//example of row 3, column 2
		b = source.exampleRow3Col2();
		assertEqualsList(testee.compute(b, 1, null), HookRight, None, HookLeft, None); //16th

		//example of row 3, column 4
		b = source.exampleRow3Col4();
		assertEqualsList(testee.compute(b, 1, null), None, HookRight, None); //16th

		//example of row 3, column 6
		b = source.exampleRow3Col6();
		wp = testee.compute(b, 2, null);
		assertEqualsList(wp, None, HookLeft, None, HookLeft); //32th
		assertEqualsList(testee.compute(b, 1, wp), None, HookLeft, Start, Stop); //16th

	}

	private void assertEqualsList(Fragments fragments, Fragment... expected) {
		assertEquals(expected.length, fragments.size());
		for (int i : range(expected)) {
			assertEquals("Fail at position " + i, expected[i], fragments.get(i));
		}
	}
	
}
