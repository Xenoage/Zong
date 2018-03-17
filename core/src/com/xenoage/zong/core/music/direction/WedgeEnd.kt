package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.format.Positioning

/**
 * End marker for a [Wedge].
 */
class WedgeEnd(
		/** The wedge that is ended by this marker.  */
		val wedge: Wedge
) : Direction {

	override var positioning: Positioning? = null

	override var parent: Chord? = null

}
