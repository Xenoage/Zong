package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.zong.musiclayout.notations.chord.NoteDisplacementTest.note;
import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.Left;
import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.None;
import static com.xenoage.zong.musiclayout.notations.chord.NoteSuspension.Right;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.musiclayout.notations.chord.NoteDisplacement;
import com.xenoage.zong.musiclayout.notations.chord.NoteSuspension;

/**
 * Tests for {@link LegerLinesStamper}.
 * 
 * @author Andreas Wenger
 */
public class LegerLinesStamperTest {
	
	private LegerLinesStamper stamper = LegerLinesStamper.legerLinesStamper;
	
	private NoteDisplacement[] noteAlignmentsTopLeftSuspended = {
		note(-2),
		note(11, -1, NoteSuspension.Left),
		note(12),
	};
	
	private NoteDisplacement[] noteAlignmentsBottomRightSuspended = {
		note(-4),
		note(-3, 1, NoteSuspension.Right),
		note(10),
	};
	
	
	@Test public void getBottomCountTest() {
		assertEquals(2, stamper.getBottomCount(-5));
		assertEquals(2, stamper.getBottomCount(-4));
		assertEquals(1, stamper.getBottomCount(-3));
		assertEquals(1, stamper.getBottomCount(-2));
		for (int lp : range(-1, 13))
			assertEquals(0, stamper.getBottomCount(lp));
	}
	
	@Test public void getTopCountTest() {
		int lines = 5;
		for (int lp : range(-5, 9))
			assertEquals(0, stamper.getTopCount(lp, lines));
		assertEquals(1, stamper.getTopCount(10, lines));
		assertEquals(1, stamper.getTopCount(11, lines));
		assertEquals(2, stamper.getTopCount(12, lines));
		assertEquals(2, stamper.getTopCount(13, lines));
	}
	
	@Test public void getXMmTest() {
		assertEquals(6.6f, stamper.getXMm(5, 2, None, 1.6f), df);
		assertEquals(5, stamper.getXMm(5, 2, Left, 1.6f), df);
		assertEquals(8.2f, stamper.getXMm(5, 2, Right, 1.6f), df);
	}
	
	@Test public void getBottomSuspensionTest() {
		assertEquals(None, stamper.getBottomSuspension(noteAlignmentsTopLeftSuspended));
		assertEquals(Right, stamper.getBottomSuspension(noteAlignmentsBottomRightSuspended));
	}
	
	@Test public void getTopSuspensionTest() {
		assertEquals(Left, stamper.getTopSuspension(noteAlignmentsTopLeftSuspended, 5));
		assertEquals(None, stamper.getTopSuspension(noteAlignmentsBottomRightSuspended, 5));
	}

	@Test public void getTopLpsTest() {
		assertArrayEquals(new int[]{}, stamper.getTopLps(0, 5));
		assertArrayEquals(new int[]{10}, stamper.getTopLps(1, 5));
		assertArrayEquals(new int[]{10, 12}, stamper.getTopLps(2, 5));
		assertArrayEquals(new int[]{10, 12, 14}, stamper.getTopLps(3, 5));
	}
	
	@Test public void getBottomLpsTest() {
		assertArrayEquals(new int[]{}, stamper.getBottomLps(0));
		assertArrayEquals(new int[]{-2}, stamper.getBottomLps(1));
		assertArrayEquals(new int[]{-4, -2}, stamper.getBottomLps(2));
		assertArrayEquals(new int[]{-6, -4, -2}, stamper.getBottomLps(3));
	}

}
