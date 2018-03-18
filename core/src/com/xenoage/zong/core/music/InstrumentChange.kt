package com.xenoage.zong.core.music

import com.xenoage.zong.core.instrument.Instrument
import com.xenoage.zong.core.position.MPElement

/**
 * This element indicates the change of an instrument
 * within a staff.
 */
class InstrumentChange(
		/** The instrument to switch to.  */
		val instrument: Instrument
) : MPElement {

	/** Back reference: The parent measure, or null, if not part of a measure. */
	override var parent: Measure? = null

}
