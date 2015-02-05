package com.xenoage.zong.musiclayout.spacing.horizontal;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;

/**
 * The leading spacing of a measure of a single staff.
 * 
 * This spacing contains for example elements
 * like initial clefs and key signatures.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class LeadingSpacing {

	/** The spacing elements of this measure leading. */
	public final IList<SpacingElement> spacingElements;
	/** The width of this leading spacing in interline spaces. */
	public final float width;

}
