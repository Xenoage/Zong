package com.xenoage.utils

/**
 * Like [forEach], but in reversed order.
 */
fun <T> List<T>.forEachReversed(action: (T) -> Unit) {
	var it = listIterator(size)
	while (it.hasPrevious())
		action(it.previous())
}

/**
 * Returns a new mutable list only of those given elements, that are not null.
 */
fun <T> mutableListOfNotNull(vararg elements: T?): MutableList<T> {
	val ret = ArrayList<T>(elements.size)
	elements.forEach { if (it != null) ret.add(it) }
	return ret
}