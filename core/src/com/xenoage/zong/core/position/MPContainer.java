package com.xenoage.zong.core.position;



/**
 * Interface for all elements which contain {@link MPElement}s.
 * 
 * @author Andreas Wenger
 */
public interface MPContainer {

	/**
	 * Gets the {@link MP} of the given child, or null, if the given child is not part of this element
	 * or if this element is not part of a score.
	 */
	public MP getMP(MPElement child);

}
