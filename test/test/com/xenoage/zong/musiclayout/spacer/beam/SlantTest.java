package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.slant;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.slantDir;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link Slant}.
 * 
 * @author Andreas Wenger
 */
public class SlantTest {
	
	@Test public void slantDirTest() {
		Slant slant = slantDir(1, 2, 1);
		assertEquals(1, slant.minIs, df);
		assertEquals(2, slant.maxIs, df);
		slant = slantDir(1, 2, -1);
		assertEquals(-2, slant.minIs, df);
		assertEquals(-1, slant.maxIs, df);
	}

	@Test public void limitTest() {
		Slant slant = slant(0).limit(1);
		assertEquals(0, slant.minIs, df);
		assertEquals(0, slant.maxIs, df);
		slant = slantDir(0, 2, 1).limit(1);
		assertEquals(0, slant.minIs, df);
		assertEquals(1, slant.maxIs, df);
		slant = slantDir(2, 3, 1).limit(1);
		assertEquals(1, slant.minIs, df);
		assertEquals(1, slant.maxIs, df);
		slant = slantDir(-1, 0, 1).limit(1);
		assertEquals(-1, slant.minIs, df);
		assertEquals(0, slant.maxIs, df);
		slant = slantDir(-3, -2, 1).limit(1);
		assertEquals(-1, slant.minIs, df);
		assertEquals(-1, slant.maxIs, df);
	}
	
}
