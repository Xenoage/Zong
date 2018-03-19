package com.xenoage.utils

import com.xenoage.utils.annotations.Untested

/**
 * A simple cache for reusing objects.
 * When the maximum number of chached objects is reached, the oldest entry is removed.
 *
 * TODO: detect overusage: if not at least 90% (?) of the requests return cached objects,
 * as soon as it is full, the cache does not work well and we should not cache at all.
 */
class Cache<ID, Value>(
		/** The maximum number of entries. 0 for an unlimited number. */
		var maxSize: Int = 100
) {

	private val pool = linkedMapOf<ID, Value>()

	/**
	 * Returns the cached object for the given key, or creates it using
	 * the given producer function.
	 */
	operator fun get(id: ID, newInstance: () -> Value): Value {
		var value = pool[id]
		if (value == null) {
			value = newInstance()
			pool[id] = value
			cleanUp()
		}
		return value!!
	}

	@Untested
	private fun cleanUp() {
		if (maxSize > 0 && pool.size > maxSize) {
			val oldestKey = pool.iterator().next().key
			pool.remove(oldestKey)
		}
	}

	companion object {
		val unlimitedSize = 0
	}

}