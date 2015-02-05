package com.xenoage.zong.musiclayout.spacer.measure;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction._0;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.time.Time;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.musiclayout.Context;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.horizontal.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureElementsSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Computes the {@link MeasureElementsSpacing}s for the
 * {@link MeasureElement}s and {@link ColumnElement}s in
 * a {@link Measure}, like key signatures or clefs.
 * 
 * Currently, clefs, key signatures at beat 0 over the
 * whole column and time signatures over the whole column are supported.
 * 
 * Because these elements need space, the {@link VoiceSpacing}s
 * of the voices of this measure must be given. These are modified
 * to leave enough space for the measure elements.
 * 
 * The strategy can either create spacings for the elements at
 * beat 0 or ignore them, if there is already a {@link LeadingSpacing}
 * for this column.
 * 
 * @author Andreas Wenger
 */
public class MeasureElementsSpacer {
	
	public static final MeasureElementsSpacer measureElementsSpacer = new MeasureElementsSpacer();
	

	/**
	 * Creates and returns spacings for the measure elements of the given measure.
	 * The given precomputed voice spacings (without respect to measure elements) are
	 * updated, so that they leave enough space for the measure elements.
	 * @param leadingSpacing  true, if a leading spacing is used for this measure. In this case,
	 *                        clefs at beat 0 are ignored (since they are already shown in the
	 *                        leading spacing)
	 * @param originalVoiceSpacings  the spacings of the voices without regard to measure elements
	 * @param notations       the precomputed notations for this staff
	 */
	public MeasureElementsSpacing compute(
		Context context, boolean leadingSpacing, List<VoiceSpacing> voiceSpacings) {
		
		Measure measure = context.score.getMeasure(context.mp);
		ColumnHeader columnHeader = context.score.getHeader().getColumnHeader(context.mp.measure);
		return compute(measure.getClefs(), columnHeader.getKeys(), columnHeader.getTime(), leadingSpacing,
			voiceSpacings, context.mp.staff, context.notationsCache, context.settings);
	}

	MeasureElementsSpacing compute(BeatEList<Clef> clefs,
		@MaybeEmpty BeatEList<Key> keys, @MaybeNull Time time, boolean leadingSpacing,
		List<VoiceSpacing> voiceSpacings, int staff, NotationsCache notations,
		LayoutSettings layoutSettings) {
		
		Key key0 = null;
		if (keys.size() > 0 && keys.getFirst().beat.equals(_0))
			key0 = keys.getFirst().element;
		if (key0 == null && time == null && (clefs == null || clefs.size() == 0)) {
			//nothing to do
			return MeasureElementsSpacing.empty;
		}

		ArrayList<ElementSpacing> ret = alist();
		float startOffset = layoutSettings.offsetMeasureStart;

		//key and time
		//************
		boolean isKey = !leadingSpacing && key0 instanceof TraditionalKey;
		boolean isTime = time != null;
		if (isKey || isTime) {
			float currentOffset = startOffset;

			//key
			//***
			if (isKey) {
				ElementWidth keyWidth = notations.get(key0, staff).getWidth();
				ret.add(new ElementSpacing(key0, _0, startOffset));
				currentOffset += keyWidth.getUsedWidth();
			}

			//time
			//****
			if (time != null) {
				ElementWidth timeWidth = notations.get(time, staff).getWidth();
				ret.add(new ElementSpacing(time, _0, currentOffset +
					timeWidth.symbolWidth / 2));
				currentOffset += timeWidth.getUsedWidth();
			}

			//move voice elements, if not enough space before first voice element
			ElementSpacing leftSE = getFirstElementSpacing(voiceSpacings, notations);
			if (leftSE != null) {
				float leftSEx = getLeftX(leftSE, notations);
				float ES = leftSEx; //existing space
				float AS = currentOffset - ES; //additional needed space
				if (AS > 0) {
					shift(voiceSpacings, AS);
					startOffset += AS;
				}
			}
		}

		//clefs
		//*****

		//for each measure element ME (with width ME.width) to insert:
		//find first voice element VE1 before the ME,
		//and last voice element VE2 at or after the ME (both in any voice!).
		//the distance between VE1 and VE2 can be used for the ME:
		//  existing space (ES) = (VE2.x - VE1.x) - 2*padding
		//if this distance is too small (ME.width > ES), additional space is required:
		//  additional space (AS) = ME.width - ES
		//then, all voice elements at or after VE2.beat are moved AS to the right.
		//the measure element is placed at ME.x = VE2.x - padding - ME.width/2
		//
		//example sketches: (*: padding, 1: VE1, 2: VE2)
		//
		//enough space: 
		//clef:         *[clef]*
		//voice 1:   o          2   
		//voice 2:    1              o
		//
		//conflict:
		//clef:         *[clef]*
		//voice 1:   o          2   
		//voice 2:       1           o
		//              !!
		//and its solution: move 2 spaces to the right
		//clef:           *[clef]*
		//voice 1:   o            2   
		//voice 2:       1             o
		if (clefs != null) {
			for (BeatE<Clef> ME : clefs) {
				Fraction MEb = ME.beat;
				float MEwidth = notations.get(ME.element).getWidth().getWidth();
	
				//if there is a leading spacing, ignore elements at beat 0
				if (leadingSpacing && !MEb.isGreater0())
					continue;
	
				//find VE1 and VE2 for the current element
				ElementSpacing[] ses = getNearestSpacingElements(MEb, voiceSpacings, notations);
				ElementSpacing VE1 = ses[0], VE2 = ses[1];
	
				//if VE1 is unknown, use startOffset. if VE2 is unknown, ignore this element
				float VE1x = (VE1 != null ? getRightX(VE1, notations) : startOffset);
				if (VE2 == null)
					continue;
				float VE2x = getLeftX(VE2, notations);
	
				//existing space
				float ES = VE2x - VE1x - 2 * layoutSettings.spacings.widthDistanceMin;
				if (ES < MEwidth) {
					//additional space needed
					float AS = MEwidth - ES;
					//move all elements at or after ME.beat
					VE2x += AS;
					shiftAfterBeat(voiceSpacings, AS, MEb);
				}
	
				//add measure element
				float MEx = VE2x - layoutSettings.spacings.widthDistanceMin - MEwidth / 2;
				ret.add(new ElementSpacing(ME.element, ME.beat, MEx));
	
			}
		}

		ret.trimToSize();
		return new MeasureElementsSpacing(ret);
	}

	/**
	 * Gets the leftmost {@link ElementSpacing} in the given list of {@link VoiceSpacing}s,
	 * or null there is none.
	 */
	private ElementSpacing getFirstElementSpacing(List<VoiceSpacing> vss, NotationsCache notations) {
		ElementSpacing ret = null;
		float retLeftX = Float.MAX_VALUE;
		for (VoiceSpacing vs : vss) {
			for (ElementSpacing se : vs.spacingElements) {
				float leftX = getLeftX(se, notations);
				if (leftX < retLeftX) {
					retLeftX = leftX;
					ret = se;
					break; //no other element after this one in the same voice will be more left
				}
			}
		}
		return ret;
	}

	/**
	 * Gets the nearest two {@link ElementSpacing}s at the
	 * given beat (left [0] and right [1]).
	 */
	private ElementSpacing[] getNearestSpacingElements(Fraction beat, List<VoiceSpacing> vss,
		NotationsCache notations) {
		ElementSpacing[] ret = { null, null };
		float retLeftX = Float.MIN_VALUE;
		float retRightX = Float.MAX_VALUE;
		for (VoiceSpacing vs : vss) {
			for (ElementSpacing se : vs.spacingElements) {
				int compare = se.beat.compareTo(beat);
				if (compare < 0) {
					float leftX = getLeftX(se, notations);
					if (leftX > retLeftX) {
						//found nearer left element
						retLeftX = leftX;
						ret[0] = se;
					}
				}
				else if (compare >= 0) {
					float rightX = getRightX(se, notations);
					if (rightX < retRightX) {
						//found nearer right element
						retRightX = rightX;
						ret[1] = se;
					}
					break; //first candidate always matches here
				}
			}
		}
		return ret;
	}

	/**
	 * Gets the leftmost position of the given {@link ElementSpacing} in the given staff.
	 * This is its offset minus the front gap of its {@link Notation}.
	 */
	private float getLeftX(ElementSpacing se, NotationsCache notations) {
		//element and notation may be null, e.g. for last SE in measure
		Notation notation = notations.get(se.element);
		return se.offsetIs - (notation != null ? notation.getWidth().frontGap : 0);
	}

	/**
	 * Gets the rightmost position of the given {@link ElementSpacing} in the given staff.
	 * This is its offset plus the width of its {@link Notation} (bot not plus its rear gap!).
	 */
	private float getRightX(ElementSpacing se, NotationsCache notations) {
		//element and notation may be null, e.g. for last SE in measure
		Notation notation = notations.get(se.element);
		return se.offsetIs + (notation != null ? notation.getWidth().symbolWidth : 0);
	}

	/**
	 * Moves all given {@link ElementSpacing}s by the given offset.
	 */
	public void shift(List<VoiceSpacing> vss, float offsetIs) {
		for (VoiceSpacing vs : vss)
			for (ElementSpacing se : vs.spacingElements)
				se.offsetIs += offsetIs;
	}

	/**
	 * Moves all given {@link ElementSpacing}s by the given offset, if they are at
	 * or behind the given beat.
	 */
	public void shiftAfterBeat(List<VoiceSpacing> vss, float offsetIs, Fraction beat) {
		for (VoiceSpacing vs : vss)
			for (ElementSpacing se : vs.spacingElements)
				if (se.beat.compareTo(beat) >= 0)
					se.offsetIs += offsetIs;
	}

}
