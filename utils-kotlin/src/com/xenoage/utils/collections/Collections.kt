package com.xenoage.utils.collections

import com.xenoage.utils.throwEx

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

/**
 * Returns this list with the given element added at the end.
 * If it is an empty list, a new list is created. Otherwise, the given list is reused.
 * This method is especially useful for dealing with immutable empty lists,
 * which are used to save memory.
 */
fun <T> List<T>.addOrNew(element: T): MutableList<T> {
	if (size == 0)
		return mutableListOf(element)
	val list = this as MutableList
	list.add(element)
	return list
}

/**
 * Returns this list with the given element removed.
 * If it is an empty list after removal, a shared empty list is returned.
 * Otherwise, the given list is reused.
 * This method is especially useful for dealing with immutable empty lists,
 * which are used to save memory.
 */
fun <T> List<T>.removeOrEmpty(element: T): List<T> {
	val index = indexOf(element)
	if (index == -1)
		return this
	if (size == 1)
		return emptyList()
	val list = this as MutableList
	list.removeAt(index)
	return list
}

/**
 * Sets the element at the given index in this list. If the index is out of
 * the bounds of this list, it is extended up to this index
 * and the gaps are filled with the given fillElement.
 */
fun <T> MutableList<T>.setExtend(index: Int, element: T, fillElement: T) {
	while (index >= size)
		add(fillElement)
	this[index] = element
}

/**
 * Sets the element at the given index in this list. If the index is out of
 * the bounds of this list, it is extended up to this index
 * and the gaps are filled with the elements produced by the given fillProducer.
 */
fun <T> MutableList<T>.setExtendBy(index: Int, element: T, fillProducer: () -> T) {
	while (index >= size)
		add(fillProducer())
	this[index] = element
}


fun <C, T: Comparable<T>> Collection<C>.max(project: (C) -> T, defaultValue: T): T {
	var ret = defaultValue
	forEach { ret = maxOf(ret, project(it)) }
	return ret
}

fun <C> Collection<C>.sumInt(project: (C) -> Int): Int {
	var ret = 0
	forEach { ret += project(it) }
	return ret
}

fun <C> Collection<C>.sumFloat(project: (C) -> Float): Float {
	var ret = 0f
	forEach { ret += project(it) }
	return ret
}

fun <T> Int.ifIndexFound(project: (Int) -> T): T? =
	if (this > -1) project(this) else null

fun Int.checkIndex(errorMesssage: () -> String): Int =
		if (this == -1) throwEx(errorMesssage()) else this


/**
 * Collects the values from the given [producer] until it returns null.
 */
inline fun <T> collect(producer: () -> T?): MutableList<T> {
	val ret = mutableListOf<T>()
	while (true) {
		val v = producer()
		if (v != null)
			ret.add(v)
		else
			return ret
	}
}