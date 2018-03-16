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
		StaffLines testee = StaffLines.Companion.getStaff5Lines();
		assertEquals(5, testee.getCount());
		assertEquals(0, testee.getBottomLp());
		assertEquals(4, testee.getMiddleLp());
		assertEquals(8, testee.getTopLp());
		assertEquals(-2, testee.getBottomLegerLp());
		assertEquals(10, testee.getTopLegerLp());
	}
	
	@Test public void test6Lines() {
		StaffLines testee = Companion.staffLines(6);
		assertEquals(6, testee.getCount());
		assertEquals(0, testee.getBottomLp());
		assertEquals(5, testee.getMiddleLp());
		assertEquals(10, testee.getTopLp());
		assertEquals(-2, testee.getBottomLegerLp());
		assertEquals(12, testee.getTopLegerLp());
	}

}
