package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.format.Positioning


/**
 * Class for a dynamic sign like forte, piano, sforzando and so on.
 */
class Dynamic(
		val value: DynamicValue
) : Direction {

	override var positioning: Positioning? = null

	override var parent: Chord? = null

}
