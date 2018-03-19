package com.xenoage.utils.sequences

/**
 * A sequence that returns projected values from the underlying [sequence] that match
 * the specified [predicate]. It is similar to the [FilterWithPreviousSequence]
 * but also includes a projection (i.e. "map").
 */
class FilterAndMapWithPreviousSequence<I, O>(
		private val sequence: Sequence<I>,
		private val predicateAndMap: (current: I, previous: O?) -> Pair<Boolean, O>
) : Sequence<O> {

	override fun iterator(): Iterator<O> = object : Iterator<O> {

		val iterator = sequence.iterator()
		var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
		var previousFilteredAndMappedItem: O? = null //at the beginning, no item was filtered/mapped yet

		private fun calcNext() {
			while (iterator.hasNext()) {
				val item = iterator.next()
				val (isFiltered, mappedResult) = predicateAndMap(item, previousFilteredAndMappedItem)
				if (isFiltered) {
					previousFilteredAndMappedItem = mappedResult
					nextState = 1
					return
				}
			}
			nextState = 0
		}

		override fun next(): O {
			if (nextState == -1)
				calcNext()
			if (nextState == 0)
				throw NoSuchElementException()
			val result = previousFilteredAndMappedItem!!
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

/** See [FilterAndMapWithPreviousSequence]. */
fun <I, O> Sequence<I>.filterAndMapWithPrevious(
		predicateAndMap: (current: I, previous: O?) -> Pair<Boolean, O>): Sequence<O> =
		FilterAndMapWithPreviousSequence(this, predicateAndMap)