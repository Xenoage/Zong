package com.xenoage.utils.sequences

/**
 * A sequence that returns projected values from the underlying [sequence] that match
 * the specified [predicate].
 */
class FilterAndMapSequence<I, O>(
		private val sequence: Sequence<I>,
		private val predicateAndMap: (current: I) -> Pair<Boolean, O>
) : Sequence<O> {

	override fun iterator(): Iterator<O> = object : Iterator<O> {

		val iterator = sequence.iterator()
		var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
		var nextItem: O? = null

		private fun calcNext() {
			while (iterator.hasNext()) {
				val item = iterator.next()
				val (isFiltered, mappedResult) = predicateAndMap(item)
				if (isFiltered) {
					nextItem = mappedResult
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
			val result = nextItem!!
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

/** See [FilterWithMapSequence]. */
fun <I, O> Sequence<I>.filterAndMap(
		predicateAndMap: (current: I) -> Pair<Boolean, O>): Sequence<O> =
		FilterAndMapSequence(this, predicateAndMap)