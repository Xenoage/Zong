package com.xenoage.zong.core.music.key;

import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.Pitch;
import com.xenoage.zong.core.position.MPContainer;


/**
 * Interface for key signatures.
 * 
 * Key signatures may appear for the whole measure column
 * (i.e. for all staves, in {@link ColumnHeader})
 * or also only within single measures.
 * 
 * @author Andreas Wenger
 */
public interface Key
	extends MeasureElement, ColumnElement {

	/**
	 * Returns the alterations from the notes from C (0) to B (6) (see {@link Pitch} constants).
	 */
	int[] getAlterations();


	/**
	 * Returns the nearest higher {@link Pitch} in the current key.
	 */
	Pitch getNearestHigherPitch(Pitch pitch);


	/**
	 * Returns the nearest lower {@link Pitch} in the current key.
	 */
	Pitch getNearestLowerPitch(Pitch pitch);
	
	
	/**
	 * Back reference: the parent element, or null, if not part of a score.
	 */
	void setParent(MPContainer parent);

}
