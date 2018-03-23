package com.xenoage.zong.core.music.util

import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.sequences.filterWithPrevious
import com.xenoage.zong.core.music.util.Interval.Result.True
import com.xenoage.zong.core.position.Beat

/**
 * This is a wrapper class to combine a list of objects with
 * the beats they belong to, sorted in ascending beat order.
 *
 * It is a typealias of a [List] and adds methods by extensions.
 * This allows us to work directly with the list instead of delegating the methods.
 */
typealias BeatEList<T> = List<BeatE<T>>

/** Creates a new [BeatEList]. */
fun <T> BeatEList(): BeatEList<T> = mutableListOf()

/** All used beats in ascending order (each beat only one time). */
@Untested
val <T> BeatEList<T>.beats: Sequence<Beat>
	get() = asSequence().map { it.beat }.filterWithPrevious { beat, lastBeat -> beat != lastBeat }

/** All data elements, sorted by ascending beat. */
val <T> BeatEList<T>.elements: Sequence<T>
	get() = asSequence().map { it.element }

/** Gets the first element at the given beat, or null if there is none. */
operator fun <T> BeatEList<T>.get(beat: Beat): T? =
		find { it.beat == beat }?.element

/** Gets all elements at the given beat. */
fun <T> BeatEList<T>.getAll(beat: Beat): Sequence<T> =
		asSequence().filter { it.beat == beat }.map { it.element }

/**
 * Returns the last element at or before the given beat.
 * If there is none, null is returned.
 */
fun <T> BeatEList<T>.getLastBefore(endpoint: Interval, beat: Beat): BeatE<T>? =
		getOrNull(indexOfLast { endpoint.isInInterval(it.beat, beat) === True })

/**
 * Returns a new [BeatEList] with only the elements which appear in the
 * given interval relative to the given beat.
 */
fun <T> BeatEList<T>.filter(interval: Interval, beat: Beat): Sequence<BeatE<out T>> =
		asSequence().filter { interval.isInInterval(it.beat, beat) === True }

/**
 * Adds the given positioned element.
 * If there are already elements at this beat, it is added
 * after the existing ones, but nothing is removed.
 */
fun <T> BeatEList<T>.add(element: BeatE<T>) {
	val insertIndex = indexOfFirst { it.beat > element.beat }
	if (insertIndex > -1)
		(this as MutableList).add(insertIndex, element)
	else
		(this as MutableList).add(element)
}

/**
 * Adds the given positioned element.
 * If there are already elements at this beat, it is added
 * after the existing ones, but nothing is removed.
 */
fun <T> BeatEList<T>.add(element: T, beat: Beat) =
		add(BeatE(element, beat))

/**
 * Adds the given positioned elements.
 * If there are already elements at the respective beat, the given elements are added
 * after the existing ones, but nothing is removed.
 */
fun <T> BeatEList<T>.addAll(list: BeatEList<T>) {
	list.forEach(::add)
}

/**
 * Adds the given element at the given beat. If there is already
 * a element, it is replaced by the given one and returned (otherwise null).
 */
fun <T> BeatEList<T>.set(element: BeatE<T>): T? {
	for (i in 0 until size) {
		val (e, b) = this[i]
		val compare = element.beat.compareTo(b)
		if (compare == 0) { //element at beat found; replace it
			(this as MutableList)[i] = element
			return e
		} else if (compare < 0) { //insert element here
			(this as MutableList).add(i, element)
			return null
		}
	}
	(this as MutableList).add(element) //add as last element
	return null
}

/**
 * Adds the given element at the given beat. If there is already
 * a element, it is replaced by the given one and returned (otherwise null).
 */
operator fun <T> BeatEList<T>.set(element: T, beat: Beat): T? =
		set(BeatE(element, beat))

/**
 * Removes the first occurrence of the given element.
 * If found, it is returned, otherwise null.
 */
fun <T> BeatEList<T>.remove(element: T): T? =
	remove(indexOfFirst { it.element === element })

/**
 * Removes and returns the first element at the given beat (if there is any).
 * If not found, null is returned..
 */
fun <T> BeatEList<T>.remove(beat: Beat): T? =
	remove(indexOfFirst { it.beat == beat })

/**
 * Removes and returns the first element at the given index.
 * If the index is -1, null is returned.
 */
private fun <T> BeatEList<T>.remove(index: Int): T? =
	if (index > -1) (this as MutableList).removeAt(index).element else null