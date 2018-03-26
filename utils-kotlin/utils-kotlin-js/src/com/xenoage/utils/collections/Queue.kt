package com.xenoage.utils.collections

/*
//Workaround until Kotlin-Common supports LinkedList
actual typealias Queue<T> = ArrayList<T>

actual fun <T> queue(): Queue<T> = ArrayList<T>()

actual fun <T> Queue<T>.addFirst(e: T) =
		add(0, e)

actual fun <T> Queue<T>.addLast(e: T) {
		add(e)
}

actual fun <T> Queue<T>.removeFirst(): T =
		if (size == 0) throw NoSuchElementException() else removeAt(0)

actual fun <T> Queue<T>.removeLast(): T =
		if (size == 0) throw NoSuchElementException() else removeAt(size - 1)
		*/