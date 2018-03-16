package com.xenoage.zong.utils.exceptions

import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.music.util.Duration


/**
 * This exception is thrown when an element
 * should be added to a measure, but there
 * were not enough beats for it between
 * the insert position and the end bar line.
 */
class MeasureFullException(
		val mp: MP,
		val requestedDuration: Duration
) : Exception() {

	override val message: String
		get() = "Measure is full. Requested duration: $requestedDuration. MP: $mp"

}
