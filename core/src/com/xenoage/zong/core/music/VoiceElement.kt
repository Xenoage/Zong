package com.xenoage.zong.core.music

import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.music.util.Duration
import com.xenoage.zong.core.position.MPElement


/**
 * Interface for all classes that are child of a voice
 * and have a duration.
 *
 * These are chords and rests.
 */
interface VoiceElement : MPElement<Voice> {

	/** The duration of this element. */
	var duration: Duration

	/** The parent voice, or null if not part of a voice. */
	override var parent: Voice?

}
