package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.zong.core.music.Staff;
import org.junit.Test;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static org.junit.Assert.*;

/**
 * Tests for {@link StavesSpacing}.
 * 
 * @author Andreas Wenger
 */
public class StavesSpacingTest {
	
	//test system
	private StavesSpacing staves = new StavesSpacing(alist(
		new Staff(alist(), 5, 1f), new Staff(alist(), 4, 2f), new Staff(alist(), 5, null)),
		new float[] { 30, 20 }, 3);
	
	
	@Test public void getStaffDistanceMm() {
		assertEquals(0, staves.getStaffDistanceMm(0), df);
		assertEquals(30, staves.getStaffDistanceMm(1), df);
		assertEquals(20, staves.getStaffDistanceMm(2), df);
	}
		
	@Test public void getStaffYOffsetMmTest() {
	 	assertEquals(0, staves.getStaffYOffsetMm(0), df);
	 	assertEquals(4 * 1 + 30, staves.getStaffYOffsetMm(1), df);
	 	assertEquals(4 * 1 + 30 + 3 * 2 + 20, staves.getStaffYOffsetMm(2), df);
	}
	
	@Test public void getStaffHeightMmTest() {
		assertEquals(4 * 1, staves.getStaffHeightMm(0), df);
		assertEquals(3 * 2, staves.getStaffHeightMm(1), df);
		assertEquals(4 * 3, staves.getStaffHeightMm(2), df);
	}
	
	@Test public void getTotalHeightMmTest() {
		assertEquals(4 * 1 + 30 + 3 * 2 + 20 + 4 * 3, staves.getTotalHeightMm(), df);
	}
	
	@Test public void getYMmTest() {
		//staff 0
		assertEquals(4 * 1, staves.getYMm(0, 0), df);
		assertEquals(2 * 1, staves.getYMm(0, 4), df);
		assertEquals(0, staves.getYMm(0, 8), df);
		//staff 1
		float s1y = 4 * 1 + 30;
		assertEquals(s1y + 3 * 2, staves.getYMm(1, 0), df);
		assertEquals(s1y + 1.5f * 2, staves.getYMm(1, 3), df);
		assertEquals(s1y, staves.getYMm(1, 6), df);
		//staff 2
		float s2y = 4 * 1 + 30 + 3 * 2 + 20;
		assertEquals(s2y + 4 * 3, staves.getYMm(2, 0), df);
		assertEquals(s2y + 2 * 3, staves.getYMm(2, 4), df);
		assertEquals(s2y, staves.getYMm(2, 8), df);
	}
	
	@Test public void getYLpTest() {
		//staff 0
		assertEquals(0, staves.getLp(0, 4 * 1), df);
		assertEquals(4, staves.getLp(0, 2 * 1), df);
		assertEquals(8, staves.getLp(0, 0), df);
		//staff 1
		float s1y = 4 * 1 + 30;
		assertEquals(0, staves.getLp(1, s1y + 3 * 2), df);
		assertEquals(3, staves.getLp(1, s1y + 1.5f * 2), df);
		assertEquals(6, staves.getLp(1, s1y), df);
		//staff 2
		float s2y = 4 * 1 + 30 + 3 * 2 + 20;
		assertEquals(0, staves.getLp(2, s2y + 4 * 3), df);
		assertEquals(4, staves.getLp(2, s2y + 2 * 3), df);
		assertEquals(8, staves.getLp(2, s2y), df);
	}

}
