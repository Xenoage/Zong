package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.musiclayout.stamper.ElementStamper.elementStamper;
import static com.xenoage.zong.musiclayout.stampings.Stamping.emptyList;

import java.util.List;

import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.Notation;
import com.xenoage.zong.musiclayout.notation.TimeNotation;
import com.xenoage.zong.musiclayout.notation.TraditionalKeyNotation;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * Creates {@link Stamping}s for {@link MeasureElement}s.
 * 
 * @author Andreas Wenger
 */
public class MeasureStamper {
	
	public static final MeasureStamper measureStamper = new MeasureStamper();
	
	/**
	 * Stamps the {@link MeasureElement}s of the leading spacing.
	 * @param leadingXMm  the horizontal position on the staff in mm, where
	 *                    the leading spacing of the measure starts.
	 */
	public List<Stamping> stampLeading(MeasureSpacing measure, float leadingXMm,
		StamperContext context) {
		
		LeadingSpacing leading = measure.leading;
		if (leading == null)
			return emptyList;
		
		List<Stamping> ret = alist(leading.elements.size());
		for (ElementSpacing element : leading.elements) {
			MusicElement me = element.getElement();
			if (me != null) {
				float xMm = leadingXMm + element.xIs * measure.interlineSpace;
				Notation notation = context.getNotation(me);
				if (notation == null)
					throw new RuntimeException("No notation for element " + me + " at " + MP.getMP(me));
				ret.add(stamp(notation, xMm, context));
			}
		}

		return ret;
	}
	
	/**
	 * Stamps all {@link MeasureElement}s of the given measure (but not the leading elements).
	 * @param measureXMm  the horizontal position on the staff in mm, where the measure
	 *                    starts (after leading spacing).
	 */
	public List<Stamping> stampMeasure(MeasureSpacing measure, float measureXMm,
		StamperContext context) {
		List<Stamping> ret = alist();
		for (ElementSpacing element : measure.elements) {
			MusicElement me = element.getElement();
			if (me != null) {
				Notation notation = context.getNotation(me);
				float xMm = measureXMm + element.xIs * measure.interlineSpace;
				ret.add(stamp(notation, xMm, context));
			}
		}
		return ret;
	}
	
	/**
	 * Stamps the element with the given {@link Notation} on the given staff and position.
	 */
	public Stamping stamp(Notation notation, float xMm, StamperContext context) {
		if (notation instanceof ClefNotation) {
			return elementStamper.createClefStamping((ClefNotation) notation, xMm, context);
		}
		else if (notation instanceof TraditionalKeyNotation) {
			return elementStamper.createKeyStamping((TraditionalKeyNotation) notation,
				xMm, context);
		}
		else if (notation instanceof TimeNotation) {
			return elementStamper.createTimeStamping((TimeNotation) notation, xMm, context);
		}
		else {
			throw new IllegalArgumentException("Notation not supported: " + notation);
		}
	}

}
