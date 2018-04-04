package com.xenoage.zong.core.music.clef

import com.xenoage.zong.core.music.Measure
import com.xenoage.zong.core.music.MeasureElement


/**
 * Class for a clef.
 */
class Clef(
		/** The type of the clef. */
		val type: ClefType
) : MeasureElement {

	override var parentMeasure: Measure? = null

}
