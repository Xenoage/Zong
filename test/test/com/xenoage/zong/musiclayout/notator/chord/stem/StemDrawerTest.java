package com.xenoage.zong.musiclayout.notator.chord.stem;

import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.musiclayout.notator.chord.stem.StemDrawer.stemDrawer;
import static material.Examples.test;
import static org.junit.Assert.assertEquals;
import material.stem.length.Example;

import org.junit.Test;

/**
 * Tests for {@link StemDrawer}.
 * 
 * @author Andreas Wenger
 */
public class StemDrawerTest {
	
	private StemDrawer testee = stemDrawer;
	
	@Test public void getPreferredStemLengthTest() {
		test(Example.all, (suite, example) -> {
			float expectedLengthIs = example.stemLengthIs;
			float lengthIs = testee.getPreferredStemLength(new int[]{example.noteLp},
				example.stemDir, example.staffLines);
			assertEquals(suite.getName() + ": " + example.name + " expected stem length of " +
				expectedLengthIs + ", but was " + lengthIs,
				expectedLengthIs, lengthIs, df);
		});
	}
	
	@Test public void isStemLengthenedToMiddleLineTest() {
		test(Example.all, (suite, example) -> {
			boolean expected = example.isLengthenedToMiddleLine;
			boolean lengthened = testee.isStemLengthenedToMiddleLine(new int[]{example.noteLp},
				example.stemDir, example.staffLines);
			assertEquals(suite.getName() + ": " + example.name + " expected " +
				(expected ? "" : "NO ") + "stem lengthening but it was " +
				(lengthened ? "" : "NOT ") + "lengthened.", expected, lengthened);
		});
	}

}
