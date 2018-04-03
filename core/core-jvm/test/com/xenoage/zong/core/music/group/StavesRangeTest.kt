package com.xenoage.zong.core.music.group

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


/**
 * Tests for [StavesRange].
 */
class StavesRangeTest {

	@Test
	fun insertTest() {
		var r: StavesRange
		//insert before
		r = StavesRange(5, 10)
		r.insert(4, 10)
		assertEquals(StavesRange(15, 20), r)
		//insert within
		r = StavesRange(5, 10)
		r.insert(5, 10)
		assertEquals(StavesRange(5, 20), r)
		//insert within
		r = StavesRange(5, 10)
		r.insert(10, 10)
		assertEquals(StavesRange(5, 20), r)
		//insert after
		r = StavesRange(5, 10)
		r.insert(11, 10)
		assertEquals(StavesRange(5, 10), r)
	}


	@Test
	fun removeTest() {
		var r: StavesRange
		//remove completely
		r = StavesRange(5, 10)
		assertTrue(r.remove(5, 6))
		//remove completely
		r = StavesRange(5, 10)
		assertTrue(r.remove(4, 7))
		//remove completely
		r = StavesRange(5, 10)
		assertTrue(r.remove(5, 7))
		//remove completely
		r = StavesRange(5, 10)
		assertTrue(r.remove(4, 8))
		//remove before
		r = StavesRange(5, 10)
		assertFalse(r.remove(2, 2))
		assertEquals(StavesRange(3, 8), r)
		//remove around start
		r = StavesRange(5, 10)
		assertFalse(r.remove(2, 4))
		assertEquals(StavesRange(2, 5), r)
		//remove within
		r = StavesRange(5, 10)
		assertFalse(r.remove(5, 5))
		assertEquals(StavesRange(5, 5), r)
		//remove within
		r = StavesRange(5, 10)
		assertFalse(r.remove(6, 5))
		assertEquals(StavesRange(5, 5), r)
		//remove around end
		r = StavesRange(5, 10)
		assertFalse(r.remove(8, 4))
		assertEquals(StavesRange(5, 7), r)
		//remove after
		r = StavesRange(5, 10)
		assertFalse(r.remove(11, 4))
		assertEquals(StavesRange(5, 10), r)
	}

}
