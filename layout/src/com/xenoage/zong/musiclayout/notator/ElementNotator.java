package com.xenoage.zong.musiclayout.notator;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.position.MPElement;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.Notations;

/**
 * Interface for the element specific notators.
 * 
 * @author Andreas Wenger
 */
public interface ElementNotator {
	
	/**
	 * Computes a notation of the given element in the given context.
	 * The already computed notations of other elements can be given
	 * and may be modified or extended, when this notator is able
	 * to compute additional information.
	 */
	Notation compute(MPElement element, Context context, @MaybeNull Notations notations);
	
}
