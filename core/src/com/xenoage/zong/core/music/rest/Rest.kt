package com.xenoage.zong.core.music.rest

import com.xenoage.zong.core.music.Voice
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.util.Duration

/**
 * Class for a rest.
 */
class Rest(
		/** The duration of this rest.  */
		override var duration: Duration
) : VoiceElement {

	/** True, if this rest has cue size, otherwise false.  */
	var isCue: Boolean = false

	/** True, if this rest is not visible (e.g. because it is at the beginning of a second voice).  */
	var isHidden: Boolean = false

	/** Back reference: the parent voice, or null if not part of a score.  */
	override var parent: Voice? = null

	override fun toString() = "rest(dur:$duration)"

}
