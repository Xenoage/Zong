package com.xenoage.utils.iterators

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [ReverseIterator].
 */
class ReverseIteratorTest {

	@Test fun test() {
		var count = 5
		val a = (0..count).toList()
		for (i in a.reverseIt())
			assertEquals(count--, i)
	}

}
