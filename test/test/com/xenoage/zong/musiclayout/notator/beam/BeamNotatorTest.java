package com.xenoage.zong.musiclayout.notator.beam;

import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link BeamNotator}.
 * 
 * @author Andreas Wenger
 */
public class BeamNotatorTest {

	BeamNotator testee = BeamNotator.beamNotator;


	@Test public void isBeamOutsideStaffTest() {
		//stem up, above staff
		assertTrue(testee.isBeamOutsideStaff(Up, 13, 13, 5, 2));
		assertTrue(testee.isBeamOutsideStaff(Up, 12.1f, 13, 5, 2));
		assertTrue(testee.isBeamOutsideStaff(Up, 13f, 12.1f, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Up, 11.9f, 11.9f, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Up, 11.9f, 13, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Up, 13f, 11.9f, 5, 2));
		//stem up, below staff
		assertTrue(testee.isBeamOutsideStaff(Up, -1, -1, 5, 2));
		assertTrue(testee.isBeamOutsideStaff(Up, -0.1f, -1, 5, 2));
		assertTrue(testee.isBeamOutsideStaff(Up, -1, -0.1f, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Up, 0.1f, 0.1f, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Up, 0.1f, -1, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Up, -1, 0.1f, 5, 2));
		//stem down, above staff
		assertTrue(testee.isBeamOutsideStaff(Down, 9, 9, 5, 2));
		assertTrue(testee.isBeamOutsideStaff(Down, 8.1f, 9, 5, 2));
		assertTrue(testee.isBeamOutsideStaff(Down, 9, 8.1f, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Down, 7.9f, 7.9f, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Down, 7.9f, 9, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Down, 9, 7.9f, 5, 2));
		//stem down, below staff
		assertTrue(testee.isBeamOutsideStaff(Down, -5, -5, 5, 2));
		assertTrue(testee.isBeamOutsideStaff(Down, -4.1f, -5, 5, 2));
		assertTrue(testee.isBeamOutsideStaff(Down, -5, -4.1f, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Down, -3.9f, -3.9f, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Down, -3.9f, -5, 5, 2));
		assertFalse(testee.isBeamOutsideStaff(Down, -5, -3.9f, 5, 2));
	}

}
