package com.xenoage.zong.core.position;

import com.xenoage.zong.core.music.MusicElement;


/**
 * Interface for all musical elements, for which an {@link MP} can be retrieved.
 * 
 * @author Andreas Wenger
 */
public interface MPElement
	extends MusicElement {

	/**
	 * Gets the parent element, or null.
	 */
	public MPContainer getParent();

}
