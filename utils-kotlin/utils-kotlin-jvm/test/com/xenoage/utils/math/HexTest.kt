package com.xenoage.utils.math

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

/**
 * Tests for Hex.kt
 */
class HexTest {

	@Test fun toHexTest() {
		assertEquals("0", 0.toHex())
		assertEquals("F", 15.toHex())
		assertEquals("10", 16.toHex())
		assertEquals("1F", 31.toHex())
		assertEquals("20", 32.toHex())
		assertEquals("FFF", 4095.toHex())
		assertEquals("75BCD15", 123456789.toHex())
		assertFails("must fail for negative numbers") { (-1).toHex() }
	}

}