package com.xenoage.utils.iterators

/**
 * Iterable iterator around a [List] with [RandomAccess],
 * that begins with the last element and ends with the first one,
 * allowing no modifications.
 * It can be used within a foreach statement.
 */
class ReverseIterator<T>(
		/** The [List] to iterate through. If null is given, a valid iterator with no elements is returned. */
		val list: List<T>?
) : Iterator<T>, Iterable<T> {

	private var currentIndex: Int = 0


	init {
		if (list != null) {
			if (list !is RandomAccess)
				throw IllegalArgumentException("Use this iterator only for RandomAccess lists!")
			currentIndex = list.size
		}
	}

	override fun hasNext() = currentIndex > 0

	override fun next(): T {
		if (hasNext()) {
			currentIndex--
			return list!![currentIndex]
		} else {
			throw NoSuchElementException()
		}
	}

	override fun iterator() = this

}

/**
 * Creates a new [ReverseIterator] for the given [List].
 * If null is given, a valid iterator with no elements is returned.
 */
fun <T> List<T>?.reverseIt() = ReverseIterator(this)
