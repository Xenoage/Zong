package com.xenoage.zong.core.music.util

import com.xenoage.utils.math.Fraction.Companion.fr
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [Duration].
 */
class DurationTest {

	@Test
	fun flagsCountTest() {
		//simple notes
		assertEquals(0, fr(1, 4).flagsCount)
		assertEquals(1, fr(1, 8).flagsCount)
		assertEquals(2, fr(1, 16).flagsCount)
		assertEquals(3, fr(1, 32).flagsCount)
		assertEquals(4, fr(1, 64).flagsCount)
		assertEquals(5, fr(1, 128).flagsCount)
		assertEquals(3, fr(2, 64).flagsCount)
		//single dotted quarter note
		assertEquals(0, fr(3, 8).flagsCount)
		//single dotted eighth note
		assertEquals(1, fr(3, 16).flagsCount)
		//double dotted quarter note
		assertEquals(0, fr(7, 16).flagsCount)
		//single dotted 32nd note
		assertEquals(3, fr(3, 64).flagsCount)
		//double dotted 32nd note
		assertEquals(3, fr(7, 128).flagsCount)
		//whole note
		assertEquals(0, fr(1, 1).flagsCount)
	}

	@Test
	fun prolongTest() {
		//no dots
		assertEquals(fr(1, 2), fr(1, 2).prolong(0))
		assertEquals(fr(1, 4), fr(1, 4).prolong(0))
		//one dot
		assertEquals(fr(3, 4), fr(1, 2).prolong(1))
		assertEquals(fr(3, 8), fr(1, 4).prolong(1))
		//two dots
		assertEquals(fr(7, 8), fr(1, 2).prolong(2))
		assertEquals(fr(7, 16), fr(1, 4).prolong(2))
	}

	@Test
	fun dotsTest() {
		//no dots
		assertEquals(0, fr(1, 1).dots)
		assertEquals(0, fr(1, 2).dots)
		assertEquals(0, fr(1, 4).dots)
		assertEquals(0, fr(1, 8).dots)
		assertEquals(0, fr(1, 16).dots)
		assertEquals(0, fr(1, 32).dots)
		//one dot
		assertEquals(1, (fr(1, 1) + fr(1, 2)).dots)
		assertEquals(1, (fr(1, 2) + fr(1, 4)).dots)
		assertEquals(1, (fr(1, 4) + fr(1, 8)).dots)
		assertEquals(1, (fr(1, 8) + fr(1, 16)).dots)
		assertEquals(1, (fr(1, 16) + fr(1, 32)).dots)
		assertEquals(1, (fr(1, 32) + fr(1, 64)).dots)
		//two dots
		assertEquals(2, (fr(1, 1) + fr(1, 2) + fr(1, 4)).dots)
		assertEquals(2, (fr(1, 2) + fr(1, 4) + fr(1, 8)).dots)
		assertEquals(2, (fr(1, 4) + fr(1, 8) + fr(1, 16)).dots)
		assertEquals(2, (fr(1, 8) + fr(1, 16) + fr(1, 32)).dots)
		assertEquals(2, (fr(1, 16) + fr(1, 32) + fr(1, 64)).dots)
		assertEquals(2, (fr(1, 32) + fr(1, 64) + fr(1, 128)).dots)
	}

	@Test
	fun getBaseDurationTest() {
		//no dots
		assertEquals(fr(1, 2), fr(1, 2).baseDuration)
		assertEquals(fr(1, 4), fr(1, 4).baseDuration)
		//one dot
		assertEquals(fr(1, 2), fr(3, 4).baseDuration)
		assertEquals(fr(1, 4), fr(3, 8).baseDuration)
		//two dots
		assertEquals(fr(1, 2), fr(7, 8).baseDuration)
		assertEquals(fr(1, 4), fr(7, 16).baseDuration)
	}

}
