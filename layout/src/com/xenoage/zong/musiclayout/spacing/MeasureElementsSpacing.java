package com.xenoage.zong.musiclayout.spacing;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;

/**
 * This class contains the spacings of the {@link MeasureElement}s
 * and {@link ColumnElement}s of a single measure, like clefs
 * and key signatures.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class MeasureElementsSpacing {
	
	/** Empty spacing. */
	public static final MeasureElementsSpacing empty = new MeasureElementsSpacing(
		Collections.<ElementSpacing>emptyList());

	
	/** The {@link ElementSpacing}s of this measure. */
	public final List<ElementSpacing> elements;

}
