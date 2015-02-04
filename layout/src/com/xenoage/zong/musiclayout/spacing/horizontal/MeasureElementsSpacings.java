package com.xenoage.zong.musiclayout.spacing.horizontal;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;

/**
 * This class contains the spacings of the {@link MeasureElement}s
 * and {@link ColumnElement}s of a single measure, like clefs
 * and key signatures.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class MeasureElementsSpacings {
	
	public static final MeasureElementsSpacings empty = new MeasureElementsSpacings(new SpacingElement[0]);

	public final SpacingElement[] elements;

}
