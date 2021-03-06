package com.xenoage.zong.core.music

import com.xenoage.zong.core.music.util.Duration
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement


/**
 * Interface for all classes that are child of a voice
 * and have a duration.
 *
 * These are chords and rests.
 */
interface VoiceElement : MPElement, MPContainer {

	/** The duration of this element. */
	var duration: Duration

	/** Back reference: The parent voice, or null if not part of a voice. */
	override var parent: Voice?

	/** All children have the same musical position as this element. */
	override fun getChildMP(child: MPElement) = mp

}
