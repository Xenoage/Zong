package com.xenoage.zong.musiclayout.spacer.beam.slant;

import com.xenoage.zong.musiclayout.spacer.beam.Slant;
import org.junit.Test;

import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Ascending;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Descending;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.slant;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.slantIs;
import static org.junit.Assert.*;

/**
 * Tests for {@link Slant}.
 * 
 * @author Andreas Wenger
 */
public class SlantTest {
	
	@Test public void slantDirTest() {
		Slant slant = slantIs(1, 2, Ascending);
		assertEquals(1, slant.getMinIs(), df);
		assertEquals(2, slant.getMaxIs(), df);
		assertEquals(1, slant.getFlattestIs(), df);
		slant = slantIs(1, 2, Descending);
		assertEquals(-2, slant.getMinIs(), df);
		assertEquals(-1, slant.getMaxIs(), df);
		assertEquals(-1, slant.getFlattestIs(), df);
	}

	@Test public void limitQsTest() {
		Slant slant = slant(0).limitQs(1);
		assertEquals(0, slant.getMinIs(), df);
		assertEquals(0, slant.getMaxIs(), df);
		slant = slantIs(0, 2, Ascending).limitQs(1 * 4);
		assertEquals(0, slant.getMinIs(), df);
		assertEquals(1, slant.getMaxIs(), df);
		slant = slantIs(2, 3, Ascending).limitQs(1 * 4);
		assertEquals(1, slant.getMinIs(), df);
		assertEquals(1, slant.getMaxIs(), df);
		slant = slantIs(0, 1, Descending).limitQs(1 * 4);
		assertEquals(-1, slant.getMinIs(), df);
		assertEquals(0, slant.getMaxIs(), df);
		slant = slantIs(2, 3, Descending).limitQs(1 * 4);
		assertEquals(-1, slant.getMinIs(), df);
		assertEquals(-1, slant.getMaxIs(), df);
	}
	
}
