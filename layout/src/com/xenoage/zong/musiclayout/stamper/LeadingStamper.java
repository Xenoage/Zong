package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.musiclayout.stamper.MeasureElementStamper.measureElementStamper;
import static com.xenoage.zong.musiclayout.stampings.Stamping.emptyList;

import java.util.List;

import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * Creates the {@link Stamping}s for the leading elements of a measure.
 * 
 * @author Andreas Wenger
 */
public class LeadingStamper {
	
	public static final LeadingStamper leadingStamper = new LeadingStamper();

	
	public List<Stamping> stamp(MeasureSpacing measure, float measureOffsetXMm,
		StaffStamping staff, Notations notations, Context context) {
		
		LeadingSpacing leadingSpacing = measure.getLeadingSpacing();
		if (leadingSpacing == null)
			return emptyList;
		
		List<Stamping> ret = alist(leadingSpacing.elements.size());
		int staffIndex = measure.getMp().staff;
		for (ElementSpacing spacingElement : leadingSpacing.elements) {
			MusicElement element = spacingElement.getElement();
			if (element != null) {
				float xMm = measureOffsetXMm + spacingElement.offsetIs * measure.getInterlineSpace();
				Notation notation = notations.get(element, staffIndex);
				if (notation == null)
					throw new RuntimeException("No notation for element " + element + " at " +
						MP.getMP(element));
				ret.add(measureElementStamper.stamp(notation, staff, xMm, context));
			}
		}
		
		return ret;
	}
	
}
