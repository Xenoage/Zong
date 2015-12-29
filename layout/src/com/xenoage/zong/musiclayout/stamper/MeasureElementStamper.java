package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.musiclayout.layouter.scoreframelayout.MusicElementStamper.musicElementStamper;
import static com.xenoage.zong.musiclayout.stampings.Stamping.emptyList;

import java.util.List;

import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.Context;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.TimeNotation;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * Creates {@link Stamping}s for {@link MeasureElement}s.
 * 
 * @author Andreas Wenger
 */
public class MeasureElementStamper {
	
	public static final MeasureElementStamper measureElementStamper = new MeasureElementStamper();
	
	/**
	 * Stamps the {@link MeasureElement}s of the leading spacing.
	 * @param leadingXMm   the horizontal position on the staff in mm, where the measure
	 *                     starts (after leading spacing)
	 */
	public List<Stamping> stampLeading(MeasureSpacing measure, StaffStamping staff,
		float leadingXMm, Notations notations, Context context) {
		
		LeadingSpacing leadingSpacing = measure.getLeadingSpacing();
		if (leadingSpacing == null)
			return emptyList;
		
		List<Stamping> ret = alist(leadingSpacing.elements.size());
		int staffIndex = measure.getMp().staff;
		for (ElementSpacing spacingElement : leadingSpacing.elements) {
			MusicElement element = spacingElement.getElement();
			if (element != null) {
				float xMm = leadingXMm + spacingElement.offsetIs * measure.getInterlineSpace();
				Notation notation = notations.get(element, staffIndex);
				if (notation == null)
					throw new RuntimeException("No notation for element " + element + " at " +
						MP.getMP(element));
				ret.add(stamp(notation, staff, xMm, context));
			}
		}
		
		return ret;
	}
	
	/**
	 * Stamps all {@link MeasureElement}s of the given measure (but not the leading elements).
	 * @param measureXMm   the horizontal position on the staff in mm, where the measure
	 *                     starts (after leading spacing)
	 */
	public List<Stamping> stampMeasure(MeasureSpacing measure, StaffStamping staff,
		float measureXMm, Notations notations, Context context) {
		List<Stamping> ret = alist();
		for (ElementSpacing spacingElement : measure.getMeasureElementsSpacings()) {
			MusicElement element = spacingElement.getElement();
			if (element != null) {
				Notation notation = notations.get(element, context.mp.staff);
				float xMm = measureXMm + spacingElement.offsetIs * measure.getInterlineSpace();
				ret.add(stamp(notation, staff, xMm, context));
			}
		}
		return ret;
	}
	
	/**
	 * Stamps the element with the given {@link Notation} on the given staff and position.
	 */
	public Stamping stamp(Notation notation, StaffStamping staff, float xMm, Context context) {
		if (notation instanceof ClefNotation) {
			return musicElementStamper.createClefStamping((ClefNotation) notation, xMm,
				staff, context.symbols);
		}
		else if (notation instanceof TraditionalKeyNotation) {
			return musicElementStamper.createKeyStamping((TraditionalKeyNotation) notation,
				xMm, staff, context.symbols, context.settings);
		}
		else if (notation instanceof TimeNotation) {
			return musicElementStamper.createTimeStamping((TimeNotation) notation, xMm,
				staff, context.symbols);
		}
		else {
			throw new IllegalArgumentException("Notation not supported: " + notation);
		}
	}

}
