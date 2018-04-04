package com.xenoage.zong.core.music.direction


/**
 * Class for a pedal direction. Start or stop.
 */
class Pedal(
		/** The start or stop marking type. */
		val type: Type
) : Direction() {

	/** Start and stop marking type. */
	enum class Type {
		/** Depress the pedal. */
		Start,
		/** Release the pedal. */
		Stop
	}

}
