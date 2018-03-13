package com.xenoage.zong.core.instrument

/**
 * Instrument group, like woodwind, brass or keyboard.
 */
data class InstrumentGroup(
	/** The unique ID of the instrument group */
	val id: String,
	/** International name  */
	val name: String) {
}
