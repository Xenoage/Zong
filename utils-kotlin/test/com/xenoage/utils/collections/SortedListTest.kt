package com.xenoage.utils.collections

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [SortedList].
 */
class SortedListTest {

	@Test
	fun addWithDuplicatesTest() {
		val duplicates = true
		//test presorted numbers
		var list = SortedList<Int>(duplicates)
		list.add(0)
		list.add(1)
		list.add(2)
		list.add(2)
		list.add(3)
		for (i in 0..4) {
			val x = if (i > 2) i - 1 else i
			assertEquals(x, list[i])
		}
		//test mixed numbers
		list = SortedList<Int>(duplicates)
		list.add(2)
		list.add(1)
		list.add(3)
		list.add(2)
		list.add(0)
		for (i in 0..4) {
			val x = if (i > 2) i - 1 else i
			assertEquals(x, list[i])
		}
		//test reversely presorted numbers
		list = SortedList<Int>(duplicates)
		list.add(3)
		list.add(2)
		list.add(2)
		list.add(1)
		list.add(0)
		for (i in 0..4) {
			val x = if (i > 2) i - 1 else i
			assertEquals(x, list[i])
		}
	}

	@Test
	fun addWithoutDuplicatesTest() {
		val duplicates = false
		//test presorted numbers
		var list = SortedList<Int>(duplicates)
		list.add(0)
		list.add(0)
		list.add(1)
		list.add(2)
		list.add(2)
		list.add(3)
		for (i in 0..3) {
			assertEquals(i, list[i])
		}
		//test mixed numbers
		list = SortedList<Int>(duplicates)
		list.add(2)
		list.add(0)
		list.add(1)
		list.add(0)
		list.add(0)
		list.add(3)
		list.add(2)
		list.add(0)
		for (i in 0..3) {
			assertEquals(i, list[i])
		}
		//test reversely presorted numbers
		list = SortedList<Int>(duplicates)
		list.add(3)
		list.add(2)
		list.add(2)
		list.add(1)
		list.add(0)
		list.add(0)
		for (i in 0..3) {
			assertEquals(i, list[i])
		}
	}

	@Test
	fun addOrReplaceTest() {
		val duplicates = false
		val list = SortedList<Comp>(duplicates)
		//add numbers
		list.addOrReplace(Comp(0, 100))
		list.addOrReplace(Comp(0, 101))
		list.addOrReplace(Comp(1, 102))
		list.addOrReplace(Comp(2, 103))
		list.addOrReplace(Comp(2, 104))
		list.addOrReplace(Comp(3, 105))
		list.addOrReplace(Comp(2, 106))
		list.addOrReplace(Comp(0, 107))
		//test
		assertEquals(4, list.size)
		assertEquals(107, list[0].value)
		assertEquals(102, list[1].value)
		assertEquals(106, list[2].value)
		assertEquals(105, list[3].value)
	}

	@Test
	fun addAll_Duplicates_Test() {
		val list1 = SortedList<Int>(true)
		list1.add(4)
		list1.add(6)
		list1.add(7)
		val list2 = SortedList<Int>(true)
		list2.add(5)
		list2.add(7)
		list2.add(7)
		//test with duplicates
		list1.addAll(list2)
		assertEquals(listOf(4, 5, 6, 7, 7, 7), list1.toList())
	}

	@Test
	fun addAll_NoDuplicates_Test() {
		val list1 = SortedList<Int>(false)
		list1.add(4)
		list1.add(6)
		list1.add(7)
		val list2 = SortedList<Int>(true)
		list2.add(5)
		list2.add(7)
		list2.add(7)
		//test without duplicates
		list1.addAll(list2)
		assertEquals(listOf(4, 5, 6, 7), list1.toList())
	}


	@Test
	fun getFirstAndGetLastTest() {
		val list = SortedList<Int>(false)
		list.add(2)
		list.add(1)
		list.add(3)
		list.add(0)
		list.add(1)
		assertEquals(0, list.first())
		assertEquals(3, list.last())
	}


	class Comp(
			val id: Int,
			val value: Int
	) : Comparable<Comp> {

		override fun compareTo(other: Comp): Int =
				id.compareTo(other.id)

		override fun equals(other: Any?): Boolean =
				id == (other as Comp).id

	}

}
