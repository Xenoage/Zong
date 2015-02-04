package com.xenoage.zong.musiclayout.notator;

import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.notations.Notation;

/**
 * Interface for the element specific notators.
 * 
 * @author Andreas Wenger
 */
public interface ElementNotator {

	Notation notate(MPElement element, Context context);
	
}
