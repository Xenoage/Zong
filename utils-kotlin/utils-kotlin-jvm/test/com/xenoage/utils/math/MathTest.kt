package com.xenoage.utils.math

import com.xenoage.utils.assertEquals
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

/**
 * Tests for [Math.kt].
 */
class MathTest {

	@Test
	fun modTest() {
		//mod 2
		run {
			var i = -20
			while (i <= 20) {
				assertEquals(0, mod(i, 2))
				i += 2
			}
		}
		run {
			var i = -19
			while (i <= 19) {
				assertEquals(1, mod(i, 2))
				i += 2
			}
		}
		//mod 4
		run {
			var i = -20
			while (i <= 20) {
				assertEquals(0, mod(i, 4))
				i += 4
			}
		}
		run {
			var i = -19
			while (i <= 17) {
				assertEquals(1, mod(i, 4))
				i += 4
			}
		}
		run {
			var i = -18
			while (i <= 18) {
				assertEquals(2, mod(i, 4))
				i += 4
			}
		}
		var i = -17
		while (i <= 19) {
			assertEquals(3, mod(i, 4))
			i += 4
		}
	}

	@Test
	fun modMinTest() {
		assertEquals(2, modMin(5, 3, 0))
		assertEquals(-8, modMin(1, 3, -10))
		assertEquals(2, modMin(-1, 3, 0))
		assertEquals(5, modMin(5, 3, 4))
		assertEquals(20, modMin(5, 3, 20))
		assertEquals(30, modMin(8, 1, 30))
		assertEquals(0, modMin(4, 4, 0))
		assertEquals(-6, modMin(-2, 4, -6))
	}

	/**
	 * Tests the rotate-method.
	 */
	@Test
	fun rotateTest() {
		val p = Point2f(10f, 5f)
		var res: Point2f
		//angle 0
		res = rotate(p, 0f)
		assertEquals(10f, res.x)
		assertEquals(5f, res.y)
		//angle 90
		res = rotate(p, 90f)
		assertEquals(5f, res.x)
		assertEquals(-10f, res.y)
		//angle 122
		res = rotate(p, 122f)
		val cos = Math.cos(122 * Math.PI / 180f).toFloat()
		val sin = Math.sin(122 * Math.PI / 180f).toFloat()
		assertEquals(10 * cos + 5 * sin, res.x, 0.001f)
		assertEquals(10 * -sin + 5 * cos, res.y, 0.001f)
	}

	@Test
	fun lowestPrimeNumberTest() {
		assertEquals(2, lowestPrimeNumber(8))
		assertEquals(3, lowestPrimeNumber(21))
		assertEquals(5, lowestPrimeNumber(65))
		assertEquals(11, lowestPrimeNumber(209))
	}

	@Test
	fun interpolateLinearTest() {
		assertEquals(5f, interpolateLinear(5f, 10f, 100f, 200f, 100f))
		assertEquals(10f, interpolateLinear(5f, 10f, 100f, 200f, 200f))
		assertEquals(6f, interpolateLinear(5f, 10f, 100f, 200f, 120f))
	}

	@Test
	fun log2Test() {
		//one single computation
		assertEquals(10, log2(1024))
		//exact result
		for (i in 0..10) {
			val number = Math.pow(2.0, i.toDouble()).toLong()
			assertEquals(i, log2(number), "log2($number)")
		}
		//lower result
		for (i in 2..10) {
			val number = Math.pow(2.0, i.toDouble()).toLong()
			assertEquals(i, log2(number + 1 + i.toLong()), "log2($number)")
		}
		//exception
		assertFails { log2(0) }
		assertFails { log2(-5) }
	}

}
