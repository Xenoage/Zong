package com.xenoage.zong.core.music.direction

import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.Measure
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.format.Positioning
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement

import lombok.Data
import lombok.EqualsAndHashCode


/**
 * Class for a pedal direction. Start or stop.
 */
class Pedal(
		/** The start or stop marking type. */
		val type: Type
) : Direction {

	override var positioning: Positioning? = null

	override var parent: Chord? = null

	override val elementType: MusicElementType
		get() = MusicElementType.Pedal

	/** Start and stop marking type. */
	enum class Type {
		/** Depress the pedal. */
		Start,
		/** Release the pedal. */
		Stop
	}

}
