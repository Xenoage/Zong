package com.xenoage.utils.collections

/**
 * Workaround until Kotlin-Common supports LinkedList.
 */
typealias Queue<T> = ArrayList<T>

fun <T> Queue<T>.addFirst(e: T) =
		add(0, e)

fun <T> Queue<T>.addLast(e: T) =
		add(e)

fun <T> Queue<T>.removeFirst(): T =
		if (size == 0) throw NoSuchElementException() else removeAt(0)

fun <T> Queue<T>.removeLast(): T =
		if (size == 0) throw NoSuchElementException() else removeAt(size - 1)
