package com.xenoage.utils.color

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [Color].
 */
class ColorTest {

	@Test
	fun toColorTest() {
		var c = Color(0, 0, 1, 0)
		assertEquals(c, "#00000001".toColor())
		c = Color(0, 0, 1, 0xFF)
		assertEquals(c, "#FF000001".toColor())
		assertEquals(c, "#000001".toColor())
		c = Color(0x21, 0x16, 0x0B, 0x2C)
		assertEquals(c, "#2C21160B".toColor())
		assertEquals(c, "#2c21160b".toColor())
	}

	@Test
	fun toHexTest() {
		var c = Color(0, 0, 1, 0)
		assertEquals("#00000001", c.toHex())
		assertEquals("#000001", c.toHex(false))
		c = Color(0x21, 0x16, 0x0B, 0x2C)
		assertEquals("#2C21160B", c.toHex())
	}

}
