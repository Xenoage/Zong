package com.xenoage.zong.core.music

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [StaffLines].
 */
class StaffLinesTest {

	@Test
	fun test5Lines() {
		val testee = StaffLines.staff5Lines
		assertEquals(5, testee.count)
		assertEquals(0, testee.bottomLp)
		assertEquals(4, testee.middleLp)
		assertEquals(8, testee.topLp)
		assertEquals(-2, testee.bottomLegerLp)
		assertEquals(10, testee.topLegerLp)
	}

	@Test
	fun test6Lines() {
		val testee = StaffLines(6)
		assertEquals(6, testee.count)
		assertEquals(0, testee.bottomLp)
		assertEquals(5, testee.middleLp)
		assertEquals(10, testee.topLp)
		assertEquals(-2, testee.bottomLegerLp)
		assertEquals(12, testee.topLegerLp)
	}

}
