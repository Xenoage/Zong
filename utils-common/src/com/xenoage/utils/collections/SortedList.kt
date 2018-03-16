package com.xenoage.utils.collections

import kotlin.coroutines.experimental.EmptyCoroutineContext.get
import kotlin.properties.Delegates.notNull

/**
 * Mutable sorted list.
 * The list may contain duplicate entries or not.
 */
class SortedList<T : Comparable<T>>(
		/** True, when this list may contain duplicates. When false,
		 *  duplicate entries are removed. */
		val duplicates: Boolean = true
) : List<T> by ArrayList<T>() {


	/**
	 * Creates a new sorted list from presorted elements.
	 * If they are not sorted, an [IllegalArgumentException] is thown.
	 *-/
	constructor(entries: Array<T>, duplicates: Boolean) {
		this.duplicates = duplicates
		listOf<>()
		var last: T? = null
		for (e in entries) {
			if (last != null) {
				if (duplicates && last.compareTo(e) > 0 || !duplicates && last.compareTo(e) >= 0) {
					throw IllegalArgumentException("Elements are not presorted!")
				}
			}
			linkedList.addLast(e)
			last = e
		}
	}

	/**
	 * Merges this list with the given list and returns the result.
	 */
	fun merge(sortedList: SortedList<T>, duplicates: Boolean): SortedList<T> {
		val ret = SortedList<T>(duplicates)
		val l1 = this.linkedList.iterator()
		val l2 = sortedList.linkedList.iterator()
		var e1 = if (l1.hasNext()) l1.next() else null
		var e2 = if (l2.hasNext()) l2.next() else null
		while (e1 != null || e2 != null) {
			if (e1 == null)
			//l1 queue is empty
			{
				ret.addLast(e2)
				e2 = if (l2.hasNext()) l2.next() else null
			} else if (e2 == null)
			//l2 queue is empty
			{
				ret.addLast(e1)
				e1 = if (l1.hasNext()) l1.next() else null
			} else {
				//both queues are non-empty, choose the lower one
				if (e1!!.compareTo(e2) <= 0) {
					//e1 first
					ret.addLast(e1)
					e1 = if (l1.hasNext()) l1.next() else null
				} else {
					//e2 first
					ret.addLast(e2)
					e2 = if (l2.hasNext()) l2.next() else null
				}
			}
		}
		return ret
	} */

	/**
	 * Adds the given entry at the correct position.
	 * If duplicates are not allowed but the given entry is a duplicate,
	 * it is not inserted.
	 *
	 * Runtime: O(n)
	 */
	fun add(entry: T) =	add(entry, false)

	/**
	 * Adds the given entry at the correct position.
	 * If duplicates are not allowed but the given entry is a duplicate,
	 * it is not replaced.
	 *
	 * Runtime: O(n)
	 */
	fun addOrReplace(entry: T) = add(entry, true)

	/**
	 * Adds the given entry at the correct position.
	 * If duplicates are not allowed but the given entry is a duplicate,
	 * it is not inserted if `replace` is false, otherwise
	 * it is replaced.
	 *
	 * Runtime: O(n)
	 */
	private fun add(entry: T, replace: Boolean) {
		for (i in indices) {
			if (this[i] < entry) {
				//add before this entry
				(this as ArrayList<T>).add(i, entry)
				return
			}
			else if (this[i] == entry) {
				if (replace)
					(this as ArrayList<T>)[i] = entry
				return
			}
		}
		add(entry)
	}

}

fun <T : Comparable<T>> sortedListOf(vararg items: T): SortedList<T> {
	val ret = SortedList<T>()
	items.forEach { ret::add }
	return ret;
}