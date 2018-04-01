package com.xenoage.utils

import kotlin.math.abs
import kotlin.test.assertTrue

/** Asserts that the [expected] value is equal to the [actual] value, with the
 * given [delta] tolerance value. */
fun assertEquals(expected: Float, actual: Float, delta: Float) {
	assertTrue(abs(expected - actual) <= delta)
}