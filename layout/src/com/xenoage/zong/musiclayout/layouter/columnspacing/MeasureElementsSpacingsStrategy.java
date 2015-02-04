package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.iterators.It.it;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Tuple2.t;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atMeasure;

import java.util.List;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.iterators.It;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
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
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.horizontal.LeadingSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureElementsSpacings;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * This strategy creates {@link SpacingElement}s for the
 * {@link MeasureElement}s and {@link ColumnElement}s in
 * a {@link Measure}, like key signatures or clefs.
 * 
 * Currently clefs, key signatures at beat 0 over the
 * whole column and time signatures over the whole column are supported.
 * 
 * Because these elements need space, the {@link VoiceSpacing}s
 * of the voices of this measure must be given. Modified versions
 * that leave enough space for the measure elements are returned.
 * 
 * The strategy can either create spacings for the elements at
 * beat 0 or ignore them, if there is already a {@link LeadingSpacing}
 * for this column.
 * 
 * @author Andreas Wenger
 */
@SuppressWarnings("unused")
public class MeasureElementsSpacingsStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Creates and returns spacings for the measure elements of the given measure.
	 * The given precomputed voice spacings (without respect to measure elements) are
	 * updated, so that they leave enough space for the measure elements, and are returned.
	 * @param leadingSpacing  true, if a leading spacing is used for this measure. In this case,
	 *                        clefs at beat 0 are ignored (since they are already shown in the
	 *                        leading spacing)
	 * @param originalVoiceSpacings  the spacings of the voices without regard to measure elements
	 * @param notations       the precomputed notations for this staff
	 */
	public Tuple2<MeasureElementsSpacings, IList<VoiceSpacing>> computeMeasureElementsSpacings(
		Score score, int iStaff, int iMeasure, boolean leadingSpacing,
		IList<VoiceSpacing> originalVoiceSpacings, NotationsCache notations,
		LayoutSettings layoutSettings) {
		
		Measure measure = score.getMeasure(atMeasure(iStaff, iMeasure));
		ColumnHeader columnHeader = score.getHeader().getColumnHeader(iMeasure);
		return compute(measure.getClefs(), columnHeader.getKeys(), columnHeader.getTime(), leadingSpacing,
			originalVoiceSpacings, iStaff, notations, layoutSettings);
	}

	Tuple2<MeasureElementsSpacings, IList<VoiceSpacing>> compute(BeatEList<Clef> clefs,
		@MaybeEmpty BeatEList<Key> keys, @MaybeNull Time time, boolean leadingSpacing,
		IList<VoiceSpacing> originalVoiceSpacings, int staff, NotationsCache notations,
		LayoutSettings layoutSettings) {
		Key key0 = null;
		if (keys.size() > 0 && keys.getFirst().beat.equals(_0))
			key0 = keys.getFirst().element;
		if (key0 == null && time == null && (clefs == null || clefs.size() == 0)) {
			//nothing to do
			return t(MeasureElementsSpacings.empty, originalVoiceSpacings);
		}

		IList<VoiceSpacing> updatedVS = originalVoiceSpacings;
		List<SpacingElement> measureElementsSpacings = alist();
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
				measureElementsSpacings.add(new SpacingElement(key0, _0, startOffset));
				currentOffset += keyWidth.getUsedWidth();
			}

			//time
			//****
			if (time != null) {
				ElementWidth timeWidth = notations.get(time, staff).getWidth();
				measureElementsSpacings.add(new SpacingElement(time, _0, currentOffset +
					timeWidth.symbolWidth / 2));
				currentOffset += timeWidth.getUsedWidth();
			}

			//move voice elements, if not enough space before first voice element
			SpacingElement leftSE = getFirstSpacingElement(updatedVS, notations);
			if (leftSE != null) {
				float leftSEx = getLeftX(leftSE, notations);
				float ES = leftSEx; //existing space
				float AS = currentOffset - ES; //additional needed space
				if (AS > 0) {
					updatedVS = shift(updatedVS, AS);
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
				SpacingElement[] ses = getNearestSpacingElements(MEb, updatedVS, notations);
				SpacingElement VE1 = ses[0], VE2 = ses[1];
	
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
					updatedVS = shiftAfterBeat(updatedVS, AS, MEb);
				}
	
				//add measure element
				float MEx = VE2x - layoutSettings.spacings.widthDistanceMin - MEwidth / 2;
				measureElementsSpacings.add(new SpacingElement(ME.element, ME.beat, MEx));
	
			}
		}
		SpacingElement[] mes = new SpacingElement[measureElementsSpacings.size()];
		measureElementsSpacings.toArray(mes);
		return t(new MeasureElementsSpacings(mes), updatedVS);
	}

	/**
	 * Gets the leftmost {@link SpacingElement} in the given list of VoiceSpacings,
	 * or null there is none.
	 */
	private SpacingElement getFirstSpacingElement(List<VoiceSpacing> vss, NotationsCache notations) {
		SpacingElement ret = null;
		float retLeftX = Float.MAX_VALUE;
		for (VoiceSpacing vs : vss) {
			for (SpacingElement se : vs.spacingElements) {
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
	 * Gets the nearest two {@link SpacingElement}s at the
	 * given beat (left [0] and right [1]).
	 */
	private SpacingElement[] getNearestSpacingElements(Fraction beat, List<VoiceSpacing> vss,
		NotationsCache notations) {
		SpacingElement[] ret = { null, null };
		float retLeftX = Float.MIN_VALUE;
		float retRightX = Float.MAX_VALUE;
		for (VoiceSpacing vs : vss) {
			for (SpacingElement se : vs.spacingElements) {
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
	 * Gets the leftmost position of the given {@link SpacingElement} in the given staff.
	 * This is its offset minus the front gap of its {@link Notation}.
	 */
	private float getLeftX(SpacingElement se, NotationsCache notations) {
		//element and notation may be null, e.g. for last SE in measure
		Notation notation = notations.get(se.element);
		return se.offsetIs - (notation != null ? notation.getWidth().frontGap : 0);
	}

	/**
	 * Gets the rightmost position of the given {@link SpacingElement} in the given staff.
	 * This is its offset plus the width of its {@link Notation} (bot not plus its rear gap!).
	 */
	private float getRightX(SpacingElement se, NotationsCache notations) {
		//element and notation may be null, e.g. for last SE in measure
		Notation notation = notations.get(se.element);
		return se.offsetIs + (notation != null ? notation.getWidth().symbolWidth : 0);
	}

	/**
	 * Moves all given {@link SpacingElement}s by the given offset.
	 */
	public IList<VoiceSpacing> shift(List<VoiceSpacing> vss, float offset) {
		CList<VoiceSpacing> movedVS = clist();
		for (VoiceSpacing vs : vss) {
			SpacingElement[] movedSE = new SpacingElement[vs.spacingElements.length];
			for (int i : range(vs.spacingElements)) {
				float newOffset = vs.spacingElements[i].offsetIs + offset;
				movedSE[i] = vs.spacingElements[i].withOffset(newOffset);
			}
			movedVS.add(new VoiceSpacing(vs.voice, vs.interlineSpace, movedSE));
		}
		return movedVS.close();
	}

	/**
	 * Moves all given {@link SpacingElement}s by the given offset, if they are at
	 * or behind the given beat.
	 */
	public IList<VoiceSpacing> shiftAfterBeat(List<VoiceSpacing> vss, float offset, Fraction beat) {
		CList<VoiceSpacing> movedVS = clist();
		for (VoiceSpacing vs : vss) {
			SpacingElement[] movedSE = new SpacingElement[vs.spacingElements.length];
			for (int i : range(vs.spacingElements)) {
				float newOffset = vs.spacingElements[i].offsetIs +
					(vs.spacingElements[i].beat.compareTo(beat) >= 0 ? offset : 0);
				movedSE[i] = vs.spacingElements[i].withOffset(newOffset);
			}
			movedVS.add(new VoiceSpacing(vs.voice, vs.interlineSpace, movedSE));
		}
		return movedVS.close();
	}

}
