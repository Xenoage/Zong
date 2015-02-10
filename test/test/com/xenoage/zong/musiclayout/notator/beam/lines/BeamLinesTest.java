package com.xenoage.zong.musiclayout.notator.beam.lines;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.notator.beam.lines.BeamLines.isBeamOutsideStaff;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link BeamLines}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class BeamLinesTest {
	
	@Test public void testIsBeamOutsideStaff() {
		//stem up, above staff
		assertTrue(isBeamOutsideStaff(Up, 13, 13, 5, 2));
		assertTrue(isBeamOutsideStaff(Up, 12.1f, 13, 5, 2));
		assertTrue(isBeamOutsideStaff(Up, 13f, 12.1f, 5, 2));
		assertFalse(isBeamOutsideStaff(Up, 11.9f, 11.9f, 5, 2));
		assertFalse(isBeamOutsideStaff(Up, 11.9f, 13, 5, 2));
		assertFalse(isBeamOutsideStaff(Up, 13f, 11.9f, 5, 2));
		//stem up, below staff
		assertTrue(isBeamOutsideStaff(Up, -1, -1, 5, 2));
		assertTrue(isBeamOutsideStaff(Up, -0.1f, -1, 5, 2));
		assertTrue(isBeamOutsideStaff(Up, -1, -0.1f, 5, 2));
		assertFalse(isBeamOutsideStaff(Up, 0.1f, 0.1f, 5, 2));
		assertFalse(isBeamOutsideStaff(Up, 0.1f, -1, 5, 2));
		assertFalse(isBeamOutsideStaff(Up, -1, 0.1f, 5, 2));
		//stem down, above staff
		assertTrue(isBeamOutsideStaff(Down, 9, 9, 5, 2));
		assertTrue(isBeamOutsideStaff(Down, 8.1f, 9, 5, 2));
		assertTrue(isBeamOutsideStaff(Down, 9, 8.1f, 5, 2));
		assertFalse(isBeamOutsideStaff(Down, 7.9f, 7.9f, 5, 2));
		assertFalse(isBeamOutsideStaff(Down, 7.9f, 9, 5, 2));
		assertFalse(isBeamOutsideStaff(Down, 9, 7.9f, 5, 2));
		//stem down, below staff
		assertTrue(isBeamOutsideStaff(Down, -5, -5, 5, 2));
		assertTrue(isBeamOutsideStaff(Down, -4.1f, -5, 5, 2));
		assertTrue(isBeamOutsideStaff(Down, -5, -4.1f, 5, 2));
		assertFalse(isBeamOutsideStaff(Down, -3.9f, -3.9f, 5, 2));
		assertFalse(isBeamOutsideStaff(Down, -3.9f, -5, 5, 2));
		assertFalse(isBeamOutsideStaff(Down, -5, -3.9f, 5, 2));
	}

}
