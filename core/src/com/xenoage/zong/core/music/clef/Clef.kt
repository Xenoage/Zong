package com.xenoage.zong.core.music.clef

import com.xenoage.zong.core.music.Measure
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.position.MPElement


/**
 * Class for a clef.
 */
class Clef(
		/** The type of the clef. */
		val type: ClefType
) : MeasureElement {

	override var parent: Measure? = null

}
