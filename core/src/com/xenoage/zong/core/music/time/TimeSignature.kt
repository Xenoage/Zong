package com.xenoage.zong.core.music.time

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.ColumnElement


/**
 * A time signature.
 */
class TimeSignature(
		/** The time signature type. */
		val type: TimeType
) : ColumnElement {

	override var parent: ColumnHeader? = null

	companion object {
		/** Implicit senza misura object. Do not use it within a score,
		 * but only as a return value to indicate that there is no time signature.  */
		val implicitSenzaMisura = TimeSignature(TimeType.timeSenzaMisura)
	}

}
