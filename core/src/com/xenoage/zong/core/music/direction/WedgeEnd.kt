package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.position.MP
import lombok.Data
import lombok.EqualsAndHashCode

/**
 * End marker for a [Wedge].
 */
class WedgeEnd(
		/** The wedge that is ended by this marker.  */
		val wedge: Wedge
) : Direction {

	override var positioning: Positioning? = null

	override var parent: Chord? = null

	override val elementType: MusicElementType
		get() = MusicElementType.WedgeEnd

}
