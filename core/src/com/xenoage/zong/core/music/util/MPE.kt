package com.xenoage.zong.core.music.util

import com.xenoage.zong.core.position.MP


/**
 * Musically positioned element.
 *
 * This is a wrapper class to combine any object with
 * the [MP] it belongs to.
 */
class MPE<T>(
	val element: T,
	val mp: MP
) {

	override fun toString() = "$element at MP $mp"

}
