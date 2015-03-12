package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.slant;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link Slant}.
 * 
 * @author Andreas Wenger
 */
public class SlantTest {

	@Test public void limitTest() {
		Slant slant = slant(0, 0).limit(1);
		assertEquals(0, slant.minIs, df);
		assertEquals(0, slant.maxIs, df);
		slant = slant(0, 2).limit(1);
		assertEquals(0, slant.minIs, df);
		assertEquals(1, slant.maxIs, df);
		slant = slant(2, 3).limit(1);
		assertEquals(1, slant.minIs, df);
		assertEquals(1, slant.maxIs, df);
		slant = slant(-1, 0).limit(1);
		assertEquals(-1, slant.minIs, df);
		assertEquals(0, slant.maxIs, df);
		slant = slant(-3, -2).limit(1);
		assertEquals(-1, slant.minIs, df);
		assertEquals(-1, slant.maxIs, df);
	}
	
}
