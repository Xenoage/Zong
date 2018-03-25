package com.xenoage.utils.sequences

/**
 * A sequence that returns projected values from the underlying [sequence].
 * It is identical to the [MappingSequence] from the
 * standard library but also provides the previous filtered value at each item.
 */
class MapWithPreviousSequence<I, O>(
		private val sequence: Sequence<I>,
		private val map: (current: I, previous: O?) -> O
) : Sequence<O> {

	override fun iterator(): Iterator<O> = object : Iterator<O> {

		val iterator = sequence.iterator()
		var nextState: Int = -1 // -1 for unknown, 0 for done, 1 for continue
		var previousMappedItem: O? = null //at the beginning, no item was mapped yet

		private fun calcNext() {
			while (iterator.hasNext()) {
				val item = iterator.next()
				previousMappedItem = map(item, previousMappedItem)
				nextState = 1
				return
			}
			nextState = 0
		}

		override fun next(): O {
			if (nextState == -1)
				calcNext()
			if (nextState == 0)
				throw NoSuchElementException()
			val result = previousMappedItem!!
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

/** See [MapWithPreviousSequence]. */
fun <I, O> Sequence<I>.mapWithPrevious(map: (current: I, previous: O?) -> O): Sequence<O> =
		MapWithPreviousSequence(this, map)