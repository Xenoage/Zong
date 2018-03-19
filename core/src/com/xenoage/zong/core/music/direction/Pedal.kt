package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.format.Positioning


/**
 * Class for a pedal direction. Start or stop.
 */
class Pedal(
		/** The start or stop marking type. */
		val type: Type
) : Direction {

	override var positioning: Positioning? = null

	override var parent: DirectionContainer? = null

	/** Start and stop marking type. */
	enum class Type {
		/** Depress the pedal. */
		Start,
		/** Release the pedal. */
		Stop
	}

}
