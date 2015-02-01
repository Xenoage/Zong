package com.xenoage.zong.musiclayout.notator.beam.direction;

import static com.xenoage.zong.musiclayout.notator.beam.direction.OneMeasureOneStaff.oneMeasureOneStaff;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.chord.ChordLps;

/**
 * Tests for {@link OneMeasureOneStaff}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class OneMeasureOneStaffTest {

	OneMeasureOneStaff testee = oneMeasureOneStaff;
	

	@Test public void computeTest() {
		int linescount = 5;
		int[][] lineposition = new int[4][1];
		StemDirection[] dirs;

		lineposition[0][0] = 1;
		lineposition[1][0] = 2;
		lineposition[2][0] = 3;
		lineposition[3][0] = 6;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Up, dirs[0]);

		lineposition[0][0] = 5;
		lineposition[1][0] = 3;
		lineposition[2][0] = 2;
		lineposition[3][0] = 0;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Up, dirs[0]);

		lineposition[0][0] = 5;
		lineposition[1][0] = 3;
		lineposition[2][0] = 0;
		lineposition[3][0] = 5;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Up, dirs[0]);

		lineposition = new int[6][1];

		lineposition[0][0] = -2;
		lineposition[1][0] = -1;
		lineposition[2][0] = 0;
		lineposition[3][0] = 1;
		lineposition[4][0] = 2;
		lineposition[5][0] = 3;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Up, dirs[0]);

		lineposition[0][0] = 2;
		lineposition[1][0] = 3;
		lineposition[2][0] = 4;
		lineposition[3][0] = 5;
		lineposition[4][0] = 6;
		lineposition[5][0] = 7;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Down, dirs[0]);

		lineposition[0][0] = 2;
		lineposition[1][0] = 3;
		lineposition[2][0] = 5;
		lineposition[3][0] = 7;
		lineposition[4][0] = 5;
		lineposition[5][0] = 3;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Down, dirs[0]);

		lineposition[0][0] = 14;
		lineposition[1][0] = 9;
		lineposition[2][0] = 5;
		lineposition[3][0] = 7;
		lineposition[4][0] = 1;
		lineposition[5][0] = -2;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Down, dirs[0]);

		lineposition[0][0] = 0;
		lineposition[1][0] = -1;
		lineposition[2][0] = -2;
		lineposition[3][0] = 1;
		lineposition[4][0] = 3;
		lineposition[5][0] = 5;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Up, dirs[0]);

		lineposition[0][0] = -2;
		lineposition[1][0] = 4;
		lineposition[2][0] = 7;
		lineposition[3][0] = 8;
		lineposition[4][0] = 7;
		lineposition[5][0] = 4;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Down, dirs[0]);

		lineposition[0][0] = -3;
		lineposition[1][0] = 4;
		lineposition[2][0] = 7;
		lineposition[3][0] = 8;
		lineposition[4][0] = 7;
		lineposition[5][0] = 4;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Down, dirs[0]);

		lineposition[0][0] = 0;
		lineposition[1][0] = 3;
		lineposition[2][0] = 5;
		lineposition[3][0] = 7;
		lineposition[4][0] = 5;
		lineposition[5][0] = 3;
		dirs = testee.compute(clps(lineposition), linescount);
		assertEquals(StemDirection.Up, dirs[0]);
	}

	private ChordLps[] clps(int[][] linePositions) {
		ChordLps[] ret = new ChordLps[linePositions.length];
		for (int i = 0; i < linePositions.length; i++)
			ret[i] = new ChordLps(linePositions[i]);
		return ret;
	}

}
