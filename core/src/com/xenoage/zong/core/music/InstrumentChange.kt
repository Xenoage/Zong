package com.xenoage.zong.core.music

import com.xenoage.zong.core.instrument.Instrument

/**
 * This element indicates the change of an instrument
 * within a staff.
 */
class InstrumentChange(
		/** The instrument to switch to.  */
		val instrument: Instrument
) : MeasureElement {

	override val parent: Measure? = null

}
