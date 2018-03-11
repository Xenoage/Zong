package com.xenoage.zong.core.position

import com.xenoage.zong.core.music.MusicElement


/**
 * Interface for all musical elements, for which an [MP] can be retrieved.
 */
interface MPElement : MusicElement {

	/**
	 * Gets the parent element, or null if unknown.
	 */
	val parent: MPContainer

	/**
	 * Gets the [MP], or null if unknown.
	 */
	val mp: MP

}
