package com.xenoage.zong.core.music.util

import com.xenoage.utils.math.Fraction.Companion.fr
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [BeatE].
 */
class BeatEListTest {

	@Test
	fun addTest() {
		val list = BeatEList<Int>()
		list.add(50, fr(5))
		list.add(30, fr(3))
		list.add(20, fr(2))
		list.add(40, fr(4))
		list.add(31, fr(3))
		list.add(32, fr(3))
		list.add(80, fr(8))
		list.add(60, fr(6))
		assertEquals(8, list.size)
		assertEquals(20, list[0].element)
		assertEquals(30, list[1].element)
		assertEquals(31, list[2].element)
		assertEquals(32, list[3].element)
		assertEquals(40, list[4].element)
		assertEquals(50, list[5].element)
		assertEquals(60, list[6].element)
		assertEquals(80, list[7].element)
	}


	@Test
	fun setTest() {
		val list = BeatEList<Int>()
		list.set(50, fr(5))
		list.set(30, fr(3))
		list.set(20, fr(2))
		list.set(40, fr(4))
		list.set(31, fr(3))
		list.set(32, fr(3))
		list.set(80, fr(8))
		list.set(60, fr(6))
		assertEquals(6, list.size)
		assertEquals(20, list[0].element)
		assertEquals(32, list[1].element)
		assertEquals(40, list[2].element)
		assertEquals(50, list[3].element)
		assertEquals(60, list[4].element)
		assertEquals(80, list[5].element)
	}

	@Test
	fun filterTest() {
		val list = BeatEList<Int>()
		list.add(10, fr(1))
		list.add(20, fr(2))
		list.add(30, fr(3))
		list.add(31, fr(3))
		list.add(40, fr(4))
		//before
		var expected = BeatEList<Int>()
		expected.add(10, fr(1))
		expected.add(20, fr(2))
		var actual = list.filter(Interval.Before, fr(3)).toList()
		assertEquals(expected, actual)
		//at or after
		expected = BeatEList()
		expected.add(30, fr(3))
		expected.add(31, fr(3))
		expected.add(40, fr(4))
		actual = list.filter(Interval.AtOrAfter, fr(3)).toList()
		assertEquals(expected, actual)
		//after
		expected = BeatEList()
		expected.add(40, fr(4))
		actual = list.filter(Interval.After, fr(3)).toList()
		assertEquals(expected, actual)
	}

}
