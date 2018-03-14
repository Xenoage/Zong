package com.xenoage.zong.core.position


/**
 * Interface for all elements which contain [MPElement]s.
 */
interface MPContainer {

	/**
	 * Gets the [MP] of the given child, or null,
	 * if the given child is not part of this element
	 * or if this element is not part of a score.
	 */
	fun getChildMP(child: MPElement<*>): MP?

}
