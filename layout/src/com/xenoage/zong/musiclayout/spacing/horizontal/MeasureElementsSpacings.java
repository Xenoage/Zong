package com.xenoage.zong.musiclayout.spacing.horizontal;

import com.xenoage.utils.base.annotations.MaybeEmpty;
import com.xenoage.utils.pdlib.IVector;
import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.MeasureElement;


/**
 * This class contains the spacings of the {@link MeasureElement}s
 * and {@link ColumnElement}s of a single measure, like clefs
 * and key signatures.
 * 
 * @author Andreas Wenger
 */
public class MeasureElementsSpacings
{
	
	@MaybeEmpty public final Vector<SpacingElement> elements;

	public static final MeasureElementsSpacings empty = new MeasureElementsSpacings(
		new IVector<SpacingElement>().close());
	
	
	public MeasureElementsSpacings(Vector<SpacingElement> elements)
	{
		this.elements = elements;
	}

}
