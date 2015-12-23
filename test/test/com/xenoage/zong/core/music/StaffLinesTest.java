package com.xenoage.zong.core.music;

import static com.xenoage.zong.core.music.StaffLines.staffLines;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for {@link StaffLines}.
 * 
 * @author Andreas Wenger
 */
public class StaffLinesTest {
	
	@Test public void test5Lines() {
		StaffLines testee = StaffLines.staff5Lines;
		assertEquals(5, testee.count);
		assertEquals(0, testee.bottomLp);
		assertEquals(4, testee.middleLp);
		assertEquals(8, testee.topLp);
		assertEquals(-2, testee.bottomLegerLp);
		assertEquals(10, testee.topLegerLp);
	}
	
	@Test public void test6Lines() {
		StaffLines testee = staffLines(6);
		assertEquals(6, testee.count);
		assertEquals(0, testee.bottomLp);
		assertEquals(5, testee.middleLp);
		assertEquals(10, testee.topLp);
		assertEquals(-2, testee.bottomLegerLp);
		assertEquals(12, testee.topLegerLp);
	}

}
