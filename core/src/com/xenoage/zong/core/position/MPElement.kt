package com.xenoage.zong.core.position

import com.xenoage.zong.core.music.MusicElement


/**
 * Interface for all musical elements, for which an [MP] can be retrieved.
 */
interface MPElement<C : MPContainer> : MusicElement {

	/**
	 * The parent element, or null if not part of a score.
	 */
	var parent: C?

	/**
	 * Gets the [MP] of this element by querying its parent, or null if it is unknown.
	 */
	val mp: MP?
		get() = this.parent?.getChildMP(this)

}
