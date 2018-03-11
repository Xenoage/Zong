package com.xenoage.zong.musiclayout.spacer.measure;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.musiclayout.notation.ClefNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.RestNotation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.SimpleSpacing;
import com.xenoage.zong.musiclayout.spacing.VoiceSpacing;
import org.junit.Test;

import java.util.List;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.util.BeatE.beatE;
import static com.xenoage.zong.core.music.util.BeatEList.beatEList;
import static com.xenoage.zong.musiclayout.settings.LayoutSettings.defaultLayoutSettings;
import static com.xenoage.zong.musiclayout.spacer.measure.MeasureElementsSpacer.measureElementsSpacer;
import static org.junit.Assert.*;

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
	private LayoutSettings ls = defaultLayoutSettings;


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
		List<VoiceSpacing> originalVs = alist(
			new VoiceSpacing(null, 1, alist(spacing(ve[0], Companion.fr(1, 2), 4), spacing(ve[1], Companion.fr(6), 15))),
			new VoiceSpacing(null, 1, alist(spacing(ve[2], Companion.fr(1), 5), spacing(ve[3], Companion.fr(17, 2), 20))));
		List<VoiceSpacing> vs = alist(originalVs);
		Clef innerClef = new Clef(ClefType.clefTreble);
		BeatEList<Clef> innerClefs = Companion.beatEList();
		innerClefs.add(Companion.beatE(innerClef, Companion.fr(6)));
		List<ElementSpacing> res = testee.compute(
			innerClefs, Companion.beatEList(), null, false, vs, 0, notations(ve, innerClef), ls);
		//clef must be at offset 15 - padding - clefwidth/2
		ElementSpacing[] mes = res.toArray(new ElementSpacing[0]);
		assertEquals(1, mes.length);
		assertEquals(Companion.fr(6), mes[0].beat);
		assertEquals(15 - paddingWidth - clefWidth / 2, mes[0].xIs, Delta.DELTA_FLOAT);
		//voice spacings must be unchanged
		assertEquals(originalVs, vs);
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
			new VoiceSpacing(null, 1, alist(spacing(ve[0], Companion.fr(1, 2), 4), spacing(ve[1], Companion.fr(4), 11))),
			new VoiceSpacing(null, 1, alist(spacing(ve[2], Companion.fr(1), 5), spacing(ve[3], Companion.fr(13, 2), 16))));
		Clef innerClef = new Clef(ClefType.clefTreble);
		BeatEList<Clef> innerClefs = Companion.beatEList();
		innerClefs.add(Companion.beatE(innerClef, Companion.fr(4)));
		List<ElementSpacing> mes = testee.compute(
			innerClefs, Companion.beatEList(), null, false, vs, 0, notations(ve, innerClef), ls);
		//voice spacings
		assertEquals(2, vs.size());
		assertEqualsSpacings(ilist(spacing(ve[0], Companion.fr(1, 2), 4), spacing(ve[1], Companion.fr(4), 13)),
			vs.get(0).elements);
		assertEqualsSpacings(ilist(spacing(ve[2], Companion.fr(1), 5), spacing(ve[3], Companion.fr(13, 2), 18)),
			vs.get(1).elements);
		//clef must be at offset 13 - padding - clefwidth/2
		ElementSpacing[] se = mes.toArray(new ElementSpacing[0]);
		assertEquals(1, se.length);
		assertEquals(Companion.fr(4), se[0].beat);
		assertEquals(13 - paddingWidth - clefWidth / 2, se[0].xIs, Delta.DELTA_FLOAT);
	}
	
	private void assertEqualsSpacings(List<ElementSpacing> expected, List<ElementSpacing> actual) {
		assertEquals(expected.size(), actual.size());
		for (int i : range(expected)) {
			assertEquals(expected.get(i).getElement(), expected.get(i).getElement());
			assertEquals(expected.get(i).beat, expected.get(i).beat);
			assertEquals(expected.get(i).xIs, expected.get(i).xIs, df);
		}
	}

	private Rest[] ve() {
		return new Rest[] { new Rest(Companion.fr(1)), new Rest(Companion.fr(1)), new Rest(Companion.fr(1)), new Rest(Companion.fr(1)) };
	}

	private ElementSpacing spacing(Rest rest, Fraction beat, float offset) {
		return new SimpleSpacing(new RestNotation(rest, new ElementWidth(0), null), beat, offset);
	}

	private Notations notations(Rest[] rests, Clef clef) {
		Notations ret = new Notations();
		ret.add(new ClefNotation(clef, new ElementWidth(clefWidth), 0, 1));
		return ret;
	}

}
