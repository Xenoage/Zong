package com.xenoage.zong.core.music

import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.music.direction.DirectionContainer
import com.xenoage.zong.core.music.util.Duration
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement


/**
 * Interface for all classes that are child of a voice
 * and have a duration.
 *
 * These are chords and rests.
 */
interface VoiceElement : MPElement, DirectionContainer {

	/** The duration of this element. */
	var duration: Duration

	/** Back reference: The parent voice, or null if not part of a voice. */
	var parentVoice: Voice?
		get() = parent as? Voice?
		set(value) { parent = value }

	/** All children have the same musical position as this element. */
	override fun getChildMP(child: MPElement) = mp

}
