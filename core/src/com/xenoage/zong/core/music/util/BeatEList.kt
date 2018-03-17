package com.xenoage.zong.core.music.util

import com.xenoage.utils.iterators.reverseIt
import com.xenoage.zong.core.music.MusicElement
import com.xenoage.zong.core.music.util.Interval.Result.True
import com.xenoage.zong.core.position.Beat
import kotlin.reflect.KClass

/**
 * This is a wrapper class to combine a list of objects with
 * the beats they belong to.
 */
class BeatEList<T>(
		/** The list of elements, sorted in ascending beat order */
		val elements: MutableList<BeatE<out T>> = mutableListOf()
) : Iterable<BeatE<out T>> {

	/** All used beats (each beat only one time) */
	val beats: List<Beat>
		get() {
			val ret = mutableListOf<Beat>()
			var lastBeat: Beat? = null
			for ((_, beat) in elements) {
				if (false == beat.equals(lastBeat)) {
					ret.add(beat)
					lastBeat = beat
				}
			}
			return ret
		}

	/** The first element, or null if the list is empty */
	val first: BeatE<out T>?
		get() = elements.firstOrNull()

	/** The last element, or null if the list is empty */
	val last: BeatE<out T>?
		get() = elements.lastOrNull()

	/** All data elements */
	val dataElements: List<T>
		get() = elements.map { it.element }

	/**
	 * Gets the first element at the given beat, or null if there is none.
	 */
	operator fun get(beat: Beat): T? =
			elements.find { it.beat == beat }?.element

	/**
	 * Gets all elements at the given beat in a new list, or an empty list if there are none.
	 */
	fun getAll(beat: Beat): List<T> =
			elements.filter { it.beat == beat }.map { it.element }

	/**
	 * Adds the given positioned element.
	 * If there are already elements at this beat, it is added
	 * after the existing ones, but nothing is removed.
	 */
	fun add(element: BeatE<out T>) {
		val insertIndex = elements.indexOfFirst { it.beat > element.beat }
		if (insertIndex > -1)
			elements.add(insertIndex, element)
		else
			elements.add(element)
	}

	/**
	 * Adds the given positioned element.
	 * If there are already elements at this beat, it is added
	 * after the existing ones, but nothing is removed.
	 */
	fun add(element: T, beat: Beat) =
		add(BeatE(element, beat))

	/**
	 * Adds the given positioned elements.
	 * If there are already elements at the respective beat, the given elements are added
	 * after the existing ones, but nothing is removed.
	 */
	fun addAll(list: BeatEList<out T>) =
		list.forEach { add(it) }

	/**
	 * Adds the given element at the given beat. If there is already
	 * a element, it is replaced by the given one and returned (otherwise null).
	 */
	fun set(element: BeatE<T>): T? {
		for (i in 0 until elements.size) {
			val (e, b) = elements[i]
			val compare = element.beat.compareTo(b)
			if (compare == 0) { //element at beat found; replace it
				elements[i] = element
				return e
			} else if (compare < 0) { //insert element here
				elements.add(i, element)
				return null
			}
		}
		elements.add(element) //add as last element
		return null
	}

	/**
	 * Adds the given element at the given beat. If there is already
	 * a element, it is replaced by the given one and returned (otherwise null).
	 */
	operator fun set(element: T, beat: Beat): T? =
		set(BeatE(element, beat))

	/**
	 * Removes the first occurrence of the given element.
	 * If found, it is returned, otherwise null.
	 */
	fun remove(element: T): T? {
		val index = elements.indexOfFirst { it.element === element }
		return when {
			index > -1 -> elements.removeAt(index).element
			else -> null
		}
	}

	/**
	 * Removes and returns the first element at the given beat (if there is any).
	 * If not found, null is returned.
	 */
	fun remove(beat: Beat): T? {
		val index = elements.indexOfFirst { it.beat == beat }
		return when {
			index > -1 -> elements.removeAt(index).element
			else -> null
		}
	}

	/**
	 * Returns the last element at or before the given beat.
	 * If there is none, null is returned.
	 */
	fun getLastBefore(endpoint: Interval, beat: Beat): BeatE<out T>? {
		val index = elements.indexOfLast { endpoint.isInInterval(it.beat, beat) === True }
		return elements.getOrNull(index)
	}

	/** The number of elements in this list */
	val size: Int
		get() = elements.size


	override fun iterator() = elements.iterator()

	/** Gets an [Iterable] to iterate in reverse order, from highest to lowest beat. */
	fun reverseIt() = elements.reverseIt()

	/**
	 * Returns a new [BeatEList] with only the elements which appear in the
	 * given interval relative to the given beat.
	 */
	fun filter(interval: Interval, beat: Beat) =
			BeatEList(elements.filter { interval.isInInterval(it.beat, beat) === True }.toMutableList())

}
