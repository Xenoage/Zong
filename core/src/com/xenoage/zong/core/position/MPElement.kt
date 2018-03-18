package com.xenoage.zong.core.position

import com.xenoage.zong.core.music.MusicElement


/**
 * Interface for all musical elements, for which an [MP] can be retrieved.
 */
interface MPElement : MusicElement {

	/** The parent element, or null if not part of a score. */
	var parent: MPContainer?

	/**
	 * Gets the [MP] of this element by querying its parent, or null if it is unknown.
	 */
	val mp: MP?
		get() = this.parent?.getChildMP(this)

}

/** Sets the parent of this given element to the given container and returns it. */
fun <T: MPElement> T.setParent(container: MPContainer?): T {
	if (this != null) parent = container
	return this
}

/** Sets the parent of this given element (if any) to null and returns it. */
fun <T: MPElement> T?.unsetParent(): T? {
	if (this != null) parent = null
	return this
}