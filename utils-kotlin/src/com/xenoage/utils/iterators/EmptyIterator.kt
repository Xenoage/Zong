package com.xenoage.utils.iterators

/**
 * Empty iterator. It can be used within a foreach statement.
 */
object emptyIterator : Iterator<Nothing>, Iterable<Nothing> {

	override fun hasNext() = false

	override fun next() = throw NoSuchElementException()

	override fun iterator() = this

}
