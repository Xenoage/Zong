package com.xenoage.zong.musiclayout.spacing.horizontal;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.math.Fraction.fr;

import com.xenoage.zong.core.format.ScoreFormat;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.musiclayout.notations.ClefNotation;

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
	 * that has the given width in mm.
	 */
	public static LeadingSpacing createGClefSpacing(float widthMm) {
		float widthIS = widthMm / ScoreFormat.defaultInterlineSpace;
		ClefNotation notation = new ClefNotation(new Clef(ClefType.clefTreble), new ElementWidth(widthIS), 0, 1);
		SpacingElement spacing = new SpacingElement(notation.getMusicElement(), fr(0), 0);
		return new LeadingSpacing(ilist(spacing), widthIS);
	}

}
