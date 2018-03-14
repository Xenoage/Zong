package com.xenoage.zong.core.music.clef

import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MPContainer
import lombok.NonNull


/**
 * Class for a clef.
 */
class Clef(
		/** The type of the clef. */
		val type: ClefType
) : MeasureElement {

	/** Back reference: the parent element, or null, if not part of a score.  */
	override var parent: MPContainer? = null

	override val elementType: MusicElementType
		get() = MusicElementType.Clef

}
