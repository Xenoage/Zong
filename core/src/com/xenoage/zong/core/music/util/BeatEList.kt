package com.xenoage.zong.core.music.util

import com.xenoage.utils.annotations.Optimized
import com.xenoage.utils.annotations.Reason.MemorySaving
import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.iterators.emptyIterator
import com.xenoage.utils.iterators.reverseIt
import com.xenoage.utils.sequences.filterWithPrevious
import com.xenoage.zong.core.music.util.Interval.Result.True
import com.xenoage.zong.core.position.Beat
import com.xenoage.zong.core.position.MPContainer

/**
 * This is a wrapper class to combine a list of objects with
 * the beats they belong to.
 *
 * All methods are defined as extension methods to the [BeatEList]?
 * type to enable representation of empty lists by null for
 * saving memory.
 */
@Optimized(MemorySaving)
class BeatEList<T> : Iterable<BeatE<out T>> {

  /** The list of elements, sorted in ascending beat order */
  val elements: MutableList<BeatE<out T>> = mutableListOf()

	override fun iterator() = elements.iterator()

}

/** All used beats (each beat only one time). */
@Untested
val <T> BeatEList<T>?.beats: Sequence<Beat>
	get() = if (this == null) emptySequence() else
		elements.asSequence().map { it.beat }.filterWithPrevious { beat, lastBeat -> beat != lastBeat }

/** The first element, or null if the list is empty. */
val <T> BeatEList<T>?.first: BeatE<out T>?
	get() = if (this == null) null else elements.firstOrNull()

/** The last element, or null if the list is empty. */
val <T> BeatEList<T>?.last: BeatE<out T>?
	get() = if (this == null) null else elements.lastOrNull()

/** All data elements. */
val <T> BeatEList<T>?.dataElements: Sequence<T>
	get() = if (this == null) emptySequence() else
		elements.asSequence().map { it.element }

/** Gets the first element at the given beat, or null if there is none. */
operator fun <T> BeatEList<T>?.get(beat: Beat): T? =
		if (this == null) null else elements.find { it.beat == beat }?.element

/** Gets all elements at the given beat. */
fun <T> BeatEList<T>?.getAll(beat: Beat): Sequence<T> =
		if (this == null) emptySequence() else
			elements.asSequence().filter { it.beat == beat }.map { it.element }

/**
 * Returns the last element at or before the given beat.
 * If there is none, null is returned.
 */
fun <T> BeatEList<T>?.getLastBefore(endpoint: Interval, beat: Beat): BeatE<out T>? {
	if (this == null) return null
	val index = elements.indexOfLast { endpoint.isInInterval(it.beat, beat) === True }
	return elements.getOrNull(index)
}

/** The number of elements in this list */
val <T> BeatEList<T>?.size: Int
	get() = if (this == null) 0 else elements.size

/** Gets an [Iterable] to iterate in reverse order, from highest to lowest beat. */
fun <T> BeatEList<T>?.reverseIt(): Iterable<BeatE<out T>> =
		if (this == null) emptyIterator else elements.reverseIt()

/**
 * Returns a new [BeatEList] with only the elements which appear in the
 * given interval relative to the given beat.
 */
fun <T> BeatEList<T>?.filter(interval: Interval, beat: Beat): Sequence<BeatE<out T>> =
		if (this == null) emptySequence() else
			elements.asSequence().filter { interval.isInInterval(it.beat, beat) === True }

/**
 * Adds the given positioned element.
 * If there are already elements at this beat, it is added
 * after the existing ones, but nothing is removed.
 * The modified list is returned (since it could have been null before).
 */
fun <T> BeatEList<T>?.add(element: BeatE<out T>): BeatEList<T> {
	val ret = this ?: BeatEList<T>()
	val insertIndex = ret.elements.indexOfFirst { it.beat > element.beat }
	if (insertIndex > -1)
		ret.elements.add(insertIndex, element)
	else
		ret.elements.add(element)
	return ret
}

/**
 * Adds the given positioned element.
 * If there are already elements at this beat, it is added
 * after the existing ones, but nothing is removed.
 * The modified list is returned (since it could have been null before).
 */
fun <T> BeatEList<T>?.add(element: T, beat: Beat): BeatEList<T> =
		add(BeatE(element, beat))

/**
 * Adds the given positioned elements.
 * If there are already elements at the respective beat, the given elements are added
 * after the existing ones, but nothing is removed.
 * The modified list is returned (since it could have been null before).
 */
fun <T> BeatEList<T>?.addAll(list: BeatEList<out T>): BeatEList<T> {
	var ret = this ?: BeatEList<T>()
	list.forEach { ret = ret.add(it) }
	return ret
}

/**
 * Adds the given element at the given beat. If there is already
 * a element, it is replaced by the given one and returned (otherwise null).
 * Also the modified list is returned (since it could have been null before).
 */
fun <T> BeatEList<T>?.set(element: BeatE<T>): Pair<BeatEList<T>, T?> {
	var ret = this ?: BeatEList<T>()
	for (i in 0 until ret.elements.size) {
		val (e, b) = ret.elements[i]
		val compare = element.beat.compareTo(b)
		if (compare == 0) { //element at beat found; replace it
			ret.elements[i] = element
			return Pair(ret, e)
		} else if (compare < 0) { //insert element here
			ret.elements.add(i, element)
			return Pair(ret, null)
		}
	}
	ret.elements.add(element) //add as last element
	return Pair(ret, null)
}

/**
 * Adds the given element at the given beat. If there is already
 * a element, it is replaced by the given one and returned (otherwise null).
 * Also the modified list is returned (since it could have been null before).
 */
operator fun <T> BeatEList<T>?.set(element: T, beat: Beat): Pair<BeatEList<T>, T?> =
		set(BeatE(element, beat))

/**
 * Removes the first occurrence of the given element.
 * If found, it is returned, otherwise null.
 * Also the modified list is returned (since it could have been null before).
 * If the list is empty after the operation, the returned list is null.
 */
fun <T> BeatEList<T>?.remove(element: T): Pair<BeatEList<T>?, T?> {
	val ret = this ?: BeatEList<T>()
	val index = ret.elements.indexOfFirst { it.element === element }
	return ret.remove(index)
}

/**
 * Removes and returns the first element at the given beat (if there is any).
 * If not found, null is returned.
 * Also the modified list is returned (since it could have been null before).
 * If the list is empty after the operation, the returned list is null.
 */
fun <T> BeatEList<T>?.remove(beat: Beat): Pair<BeatEList<T>?, T?> {
	val ret = this ?: BeatEList<T>()
	val index = ret.elements.indexOfFirst { it.beat == beat }
	return ret.remove(index)
}

/**
 * Removes and returns the first element at the given index.
 * If the index is -1, null is returned.
 * Also the modified list is returned (since it could have been null before).
 * If the list is empty after the operation, the returned list is null.
 */
private fun <T> BeatEList<T>.remove(index: Int): Pair<BeatEList<T>?, T?> {
	val oldE = if (index > -1) elements.removeAt(index).element else null
	return if (size > 0) Pair(this, oldE) else Pair(null, oldE)
}

