package com.xenoage.zong.musiclayout.spacing.horizontal;

import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;

/**
 * This class contains the leading spacing
 * of a measure of a single staff.
 * 
 * This spacing contains for example elements
 * like initial clefs and key signatures.
 *
 * @author Andreas Wenger
 */
@Const @Getter public final class LeadingSpacing {

	/** The spacing elements of this measure leading. */
	private final IList<SpacingElement> spacingElements;
	/** The width of this leading spacing in interline spaces. */
	private final float width;


	/**
	 * Creates a leading spacing for an empty measure.
	 */
	public LeadingSpacing(IList<SpacingElement> spacingElements, float width) {
		this.spacingElements = spacingElements;
		this.width = width;
	}

}
