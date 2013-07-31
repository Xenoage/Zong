package com.xenoage.zong.core.music;

import com.xenoage.zong.core.position.MPElement;


/**
 * Measure elements are {@link MPElement}s, which can
 * appear in the middle of a measure and apply to
 * all voices of the measure.
 * 
 * This is for example the case for clefs, directions and keys.
 * 
 * @author Andreas Wenger
 */
public interface MeasureElement
	extends MPElement {

}
