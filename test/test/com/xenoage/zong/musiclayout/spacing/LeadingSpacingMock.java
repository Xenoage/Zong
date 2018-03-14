package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;

/**
 * Mock class that can be used when a
 * {@link LeadingSpacing} is needed
 * but when its content is unimportant.
 * 
 * @author Andreas Wenger
 */
public class LeadingSpacingMock {

	/**
	 * Creates an easy MeasureLeadingSpacing (with a g-clef)
	 * that has the given width in IS.
	 */
	public static LeadingSpacing createGClefSpacing(float widthIs) {
		ClefNotation notation = new ClefNotation(new Clef(ClefType.Companion.getClefTreble()), new ElementWidth(widthIs), 0, 1);
		ElementSpacing spacing = new SimpleSpacing(notation, Companion.fr(0), 0);
		return new LeadingSpacing(alist(spacing), widthIs);
	}

}
