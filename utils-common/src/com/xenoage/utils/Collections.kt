package com.xenoage.utils

/**
 * Like [forEach], but in reversed order.
 */
fun <T> List<T>.forEachReversed(action: (T) -> Unit) {
	var it = listIterator(size)
	while (it.hasPrevious())
		action(it.previous())
}