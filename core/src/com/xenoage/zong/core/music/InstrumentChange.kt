package com.xenoage.zong.core.music

import com.xenoage.zong.core.instrument.Instrument
import com.xenoage.zong.core.music.direction.Direction
import com.xenoage.zong.core.music.direction.DirectionContainer
import com.xenoage.zong.core.position.MPElement

/**
 * This element indicates the change of an instrument
 * within a staff.
 */
class InstrumentChange(
		/** The instrument to switch to.  */
		val instrument: Instrument
) : MeasureElement {

	override var parent: Measure? = null

}