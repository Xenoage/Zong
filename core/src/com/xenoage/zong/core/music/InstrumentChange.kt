package com.xenoage.zong.core.music

import com.xenoage.zong.core.instrument.Instrument
import com.xenoage.zong.core.position.MP

/**
 * This element indicates the change of an instrument
 * within a staff.
 */
class InstrumentChange(
		/** The instrument to switch to.  */
		val instrument: Instrument
) : MeasureElement {

	override val parent: Measure? = null

	override val elementType: MusicElementType
		get() = MusicElementType.InstrumentChange

}
