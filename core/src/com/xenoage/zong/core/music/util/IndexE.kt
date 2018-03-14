package com.xenoage.zong.core.music.util


/**
 * Element at an index.
 *
 * This is a wrapper class to combine any object with
 * its index it belongs to.
 */
class IndexE<T>(
	/** The element at the beat */
	val element: T,
	/** The index where the element can be found */
	val index: Int = 0
) {

	override fun toString() = "[$element at index $index]"

}
