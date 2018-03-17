package com.xenoage.zong.core.music.util

import com.xenoage.zong.core.position.Beat

/**
 * Element at a beat.
 *
 * This is a wrapper class to combine any object with
 * the beat it belongs to.
 */
data class BeatE<T>(
	/** The element at the beat  */
	val element: T,
	/** The beat where the element can be found  */
	val beat: Beat
) {

	override fun toString() = "$element at beat $beat"

	companion object {
		/**
		 * Returns the latest of the given elements. If none are given or
		 * no items with beats are given, null is returned.
		 */
		fun <T2> selectLatest(vararg elements: BeatE<T2>): BeatE<T2>? {
			var ret: BeatE<T2>? = null
			for (element in elements) {
				if (ret == null || element.beat.compareTo(ret.beat) > 0)
					ret = element
			}
			return ret
		}
	}

}
