package com.xenoage.utils.math

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [Rectangle2f].
 */
class Rectangle2fTest {

	@Test
	fun extend() {
		//test 1
		var r1 = Rectangle2f(2f, 4f, 6f, 8f)
		var r2 = Rectangle2f(1f, -4f, 12f, 20f)
		r1 = r1.extend(r2)
		assertEquals(r1.position.x, 1f)
		assertEquals(r1.position.y, -4f)
		assertEquals(r1.size.width, 12f)
		assertEquals(r1.size.height, 20f)
		//test 2
		r1 = Rectangle2f(2f, 4f, 6f, 8f)
		r2 = Rectangle2f(3f, 8f, 4f, 1f)
		r1 = r1.extend(r2)
		assertEquals(r1.position.x, 2f)
		assertEquals(r1.position.y, 4f)
		assertEquals(r1.size.width, 6f)
		assertEquals(r1.size.height, 8f)
		//test 3
		r1 = Rectangle2f(2f, 2f, 4f, 4f)
		r2 = Rectangle2f(0f, 0f, 4f, 4f)
		r1 = r1.extend(r2)
		assertEquals(r1.position.x, 0f)
		assertEquals(r1.position.y, 0f)
		assertEquals(r1.size.width, 6f)
		assertEquals(r1.size.height, 6f)
	}

}
