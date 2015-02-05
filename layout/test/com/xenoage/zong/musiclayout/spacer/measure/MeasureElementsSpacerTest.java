package com.xenoage.zong.musiclayout.spacer.measure;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.util.BeatE.beatE;
import static com.xenoage.zong.musiclayout.spacer.measure.MeasureElementsSpacer.measureElementsSpacer;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.Key;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ClefNotation;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.settings.ChordSpacings;
import com.xenoage.zong.musiclayout.settings.ChordWidths;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.settings.Spacings;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureElementsSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Tests for the {@link MeasureElementsSpacer}.
 * 
 * In these tests, we assume a width for the padding of
 * 1 IS and for the clef of 6 IS.
 * 
 * @author Andreas Wenger
 */
public class MeasureElementsSpacerTest {

	private MeasureElementsSpacer testee = measureElementsSpacer;
	
	private float paddingWidth = 1;
	private float clefWidth = 6;
	private LayoutSettings ls = ls();


	/**
	 * If there is enough space left for the measure elements,
	 * the voice spacings do not have to be changed.
	 * To understand the following sketch, have a look at the comments
	 * in {@link MeasureElementsSpacer}.
	 * <pre>
	 * enough space: 
	 * beat:     0   2   4   6   8
	 * offset:   3 5 7 9  13  17  21
	 *           . . . . . . . . . .
	 * clef:         *[clef]*          (on beat 6)
	 * voice 1:   o          2   
	 * voice 2:    1              o
	 * </pre>
	 */
	@Test public void testEnoughExistingSpace() {
		Rest[] ve = ve();
		List<VoiceSpacing> vs = alist(
			new VoiceSpacing(null, 1, alist(se(ve[0], fr(1, 2), 4), se(ve[1], fr(6), 15))),
			new VoiceSpacing(null, 1, alist(se(ve[2], fr(1), 5), se(ve[3], fr(17, 2), 20))));
		Clef innerClef = new Clef(ClefType.clefTreble);
		BeatEList<Clef> innerClefs = new BeatEList<Clef>();
		innerClefs.add(beatE(innerClef, fr(6)));
		MeasureElementsSpacing res = testee.compute(
			innerClefs, new BeatEList<Key>(), null, false, vs, 0, sne(ve, innerClef), ls);
		//clef must be at offset 15 - padding - clefwidth/2
		ElementSpacing[] mes = res.elements.toArray(new ElementSpacing[0]);
		assertEquals(1, mes.length);
		assertEquals(fr(6), mes[0].beat);
		assertEquals(15 - paddingWidth - clefWidth / 2, mes[0].offsetIs, Delta.DELTA_FLOAT);
		//voice spacings must be unchanged - GOON - clone original
		assertEquals(vs, vs);
	}

	/**
	 * If there is not enough space left for the measure elements,
	 * the voice spacings have to be moved to create enough space.
	 * To understand the following sketch, have a look at the comments
	 * in {@link MeasureElementsSpacer}.
	 * <pre>
	 * enough space: 
	 * beat:     0   2   4   6   8
	 * offset:   3 5 7 9  13  17  21
	 *           . . . . . . . . . .
	 * clef:         *[clef]*          (on beat 4)
	 * voice 1:   o      2
	 * voice 2:    1          o
	 * </pre>
	 * Between VE1 and VE2, there are 6 spaces.
	 * Assuming a padding width of 1 and a clef width of 6,
	 * the clef can be moved two spaces to the left, but this is not
	 * enough. All elements at or after beat 3 have to be moved 2 spaces
	 * to the right.
	 */
	@Test public void testNeedAdditionalSpace() {
		Rest[] ve = ve();
		List<VoiceSpacing> vs = alist(
			new VoiceSpacing(null, 1, alist(se(ve[0], fr(1, 2), 4), se(ve[1], fr(4), 11))),
			new VoiceSpacing(null, 1, alist(se(ve[2], fr(1), 5), se(ve[3], fr(13, 2), 16))));
		Clef innerClef = new Clef(ClefType.clefTreble);
		BeatEList<Clef> innerClefs = new BeatEList<Clef>();
		innerClefs.add(beatE(innerClef, fr(4)));
		MeasureElementsSpacing mes = testee.compute(
			innerClefs, new BeatEList<Key>(), null, false, vs, 0, sne(ve, innerClef), ls);
		//voice spacings
		assertEquals(2, vs.size());
		assertEquals(ilist(se(ve[0], fr(1, 2), 4), se(ve[1], fr(4), 13)), vs.get(0)
			.spacingElements);
		assertEquals(ilist(se(ve[2], fr(1), 5), se(ve[3], fr(13, 2), 18)), vs.get(1)
			.spacingElements);
		//clef must be at offset 13 - padding - clefwidth/2
		ElementSpacing[] se = mes.elements.toArray(new ElementSpacing[0]);
		assertEquals(1, se.length);
		assertEquals(fr(4), se[0].beat);
		assertEquals(13 - paddingWidth - clefWidth / 2, se[0].offsetIs, Delta.DELTA_FLOAT);
	}

	private Rest[] ve() {
		return new Rest[] { new Rest(fr(1)), new Rest(fr(1)), new Rest(fr(1)), new Rest(fr(1)) };
	}

	private ElementSpacing se(VoiceElement ve, Fraction beat, float offset) {
		return new ElementSpacing(ve, beat, offset);
	}

	private NotationsCache sne(Rest[] rests, Clef clef) {
		NotationsCache ret = new NotationsCache();
		for (Rest rest : rests)
			ret.add(new RestNotation(rest, new ElementWidth(0)));
		ret.add(new ClefNotation(clef, new ElementWidth(clefWidth), 0, 1));
		return ret;
	}

	private LayoutSettings ls() {
		HashMap<Fraction, Float> dw = map();
		dw.put(fr(1), 1f);
		ChordSpacings spacings = new ChordSpacings(dw);
		return new LayoutSettings(ChordWidths.defaultValue, ChordWidths.defaultValue, new Spacings(
			spacings, spacings, 1, 1, clefWidth, 0, paddingWidth), 1, 1, 0, 0);
	}

}
