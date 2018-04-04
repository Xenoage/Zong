package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.IS


/**
 * Class for crescendos and diminuendos.
 *
 * To create a wedge, create an instance of this class,
 * retrieve its end marker by calling [getWedgeEnd] and
 * place it anywhere after the instance of this class. The wedge ends
 * at the element at which the end marker is placed.
 */
class Wedge(
		/** Crescendo or diminuendo. */
		val type: WedgeType
) : Direction() {

	/** End marker of this wedge.  */
	val wedgeEnd = WedgeEnd(this)

	/** Height of the opening, or null for default. */
	var spread: IS? = null

}
