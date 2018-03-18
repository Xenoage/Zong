package com.xenoage.utils.sequences

/**
 * A sequence that returns the values from the underlying [sequence] that match
 * the specified [predicate]. It is identical to the [FilteringSequence] from the
 * standard library but also provides the previous filtered value at each item.
 */
class FilterWithPreviousSequence<T>(
		private val sequence: Sequence<T>,
		private val predicate: (current: T, previous: T?) -> Boolean
) : Sequence<T> {

	override fun iterator(): Iterator<T> = object : Iterator<T> {

		val iterator = sequence.iterator()
		var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
		var previousFilteredItem: T? = null //at the beginning, no item was filtered yet

		private fun calcNext() {
			while (iterator.hasNext()) {
				val item = iterator.next()
				if (predicate(item, previousFilteredItem)) {
					previousFilteredItem = item
					nextState = 1
					return
				}
			}
			nextState = 0
		}

		override fun next(): T {
			if (nextState == -1)
				calcNext()
			if (nextState == 0)
				throw NoSuchElementException()
			val result = previousFilteredItem!!
			nextState = -1
			return result
		}

		override fun hasNext(): Boolean {
			if (nextState == -1)
				calcNext()
			return nextState == 1
		}
	}
}

/** See [FilterWithPreviousSequence]. */
fun <T> Sequence<T>.filterWithPrevious(
		predicate: (current: T, previous: T?) -> Boolean): Sequence<T> =
		FilterWithPreviousSequence(this, predicate)