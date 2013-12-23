package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.llist;
import static com.xenoage.utils.iterators.ReverseIterator.reverseIt;

import java.util.LinkedList;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.Notation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Strategy to compute the horizontal spacing for a
 * single voice regardless of the spacing of other
 * voices or staves. Lyrics are ignored
 * (use the {@link LyricsVoiceSpacingStrategy} later).
 * 
 * This is needed for precomputations needed for the final
 * spacing, which can be done when the {@link BeatOffset}s
 * are known by using the {@link BeatOffsetBasedVoiceSpacingStrategy}.
 * 
 * Since this strategy only handles {@link VoiceElement}s, the barlines
 * at the beginning and the end of the measure, implicit initial clefs,
 * time signatures and so on are ignored.
 * 
 * @author Andreas Wenger
 */
public class SeparateVoiceSpacingStrategy
	implements ScoreLayouterStrategy {

	/**
	 * Computes the {@link VoiceSpacing} for the given voice,
	 * which is computed separately, regardless of the spacing of other
	 * voices or staves. If a parent measure is given, its clefs and
	 * key signatures are considered for the computation, too.
	 * @param voice           the voice to compute
	 * @param interlineSpace  interline space of the voice
	 * @param notations       the already computed notations (all elements within the
	 *                        given voice must be included)
	 * @param measureBeats    the time signature of this voice
	 * @param layoutSettings  general layout preferences
	 */
	public VoiceSpacing computeVoiceSpacing(Voice voice, float interlineSpace,
		NotationsCache notations, Fraction measureBeats, LayoutSettings layoutSettings) {
		LinkedList<SpacingElement> acc = llist();

		//special case: no elements in the measure.
		if (voice.getElements().size() == 0) {
			return new VoiceSpacing(voice, interlineSpace, ilist(
				new SpacingElement(null, Fraction._0, 0), new SpacingElement(null, measureBeats,
					layoutSettings.spacings.widthMeasureEmpty)));
		}

		//we compute the spacings in reverse order. this is easier, since grace chords
		//use shared space when possible, but are aligned to the right. so we begin
		//at position 0, and create the spacings in reverse direction (thus we find negative positions).
		//at the end, we shift the voice spacing to the right to be aligned at the left measure border,
		//which is position 0.

		//last symbol offset:
		//real offset where the last element's symbol started
		float lastSymbolOffset = 0; //since we do not know the right border yet, we start at 0

		//front gap offset:
		//offset where the last element (including front gap) started.
		float lastFrontGapOffset = lastSymbolOffset;

		//last full element offset:
		//lastSymbolOffset of the last full (non-grace) element
		float lastFullSymbolOffset = lastSymbolOffset;

		//at last beat
		Fraction curBeat = voice.getFilledBeats();
		acc.addFirst(new SpacingElement(null, curBeat, lastFrontGapOffset));

		//iterate through the elements in reverse order
		for (VoiceElement element : reverseIt(voice.getElements())) {
			//get the notation
			Notation notation = notations.get(element);
			if (notation == null)
				throw new IllegalStateException("No notation for element " + element);

			//get the width of the element (front gap, symbol's width, rear gap, lyric's width)
			ElementWidth elementWidth = notation.getWidth();

			//add spacing for element
			float symbolOffset;
			boolean grace = !element.getDuration().isGreater0();
			if (!grace) {
				//full element
				//share this rear gap and the front gap of the following
				//element + the space of following grace elements, when possible
				//(but use at least minimal distance)
				symbolOffset = Math.min(lastFrontGapOffset - layoutSettings.spacings.widthDistanceMin,
					lastFullSymbolOffset - elementWidth.getRearGap()) - elementWidth.getSymbolWidth();
				lastFullSymbolOffset = symbolOffset;
				//update beat cursor
				curBeat = curBeat.sub(element.getDuration());
			}
			else {
				//grace element
				//share this rear gap and the front gap of the following element, when possible
				symbolOffset = Math.min(lastFrontGapOffset, lastSymbolOffset - elementWidth.getRearGap()) -
					elementWidth.getSymbolWidth();
			}
			acc.addFirst(new SpacingElement(element, curBeat, symbolOffset, grace));
			lastFrontGapOffset = symbolOffset - elementWidth.getFrontGap();
			lastSymbolOffset = symbolOffset;
		}

		//shift spacings to the right
		float shift = (-lastFrontGapOffset) + layoutSettings.offsetMeasureStart;
		CList<SpacingElement> ret = clist(acc.size());
		for (SpacingElement se : acc)
			ret.add(se.withOffset(se.offset + shift));
		ret.close();

		return new VoiceSpacing(voice, interlineSpace, ret);
	}

}
