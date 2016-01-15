package com.xenoage.zong.core.music.beam;

import static org.junit.Assert.assertEquals;
import material.beam.fragments.ChlapikBeamFragments;

import org.junit.Test;

/**
 * Tests for {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public class BeamTest {
	
	@Test public void getMaxLinesCountTest() {
		ChlapikBeamFragments source = new ChlapikBeamFragments();
		assertEquals(2, source.exampleRow1Col1().getMaxLinesCount());
		assertEquals(2, source.exampleRow1Col2().getMaxLinesCount());
		assertEquals(2, source.exampleRow1Col3().getMaxLinesCount());
		assertEquals(2, source.exampleRow1Col4().getMaxLinesCount());
		assertEquals(2, source.exampleRow2Col1().getMaxLinesCount());
		assertEquals(2, source.exampleRow2Col2().getMaxLinesCount());
		assertEquals(3, source.exampleRow2Col3().getMaxLinesCount());
		assertEquals(3, source.exampleRow2Col4().getMaxLinesCount());
		assertEquals(3, source.exampleRow2Col5().getMaxLinesCount());
		assertEquals(3, source.exampleRow2Col6().getMaxLinesCount());
		assertEquals(2, source.exampleRow3Col2().getMaxLinesCount());
		assertEquals(2, source.exampleRow3Col4().getMaxLinesCount());
		assertEquals(3, source.exampleRow3Col6().getMaxLinesCount());
	}

}
