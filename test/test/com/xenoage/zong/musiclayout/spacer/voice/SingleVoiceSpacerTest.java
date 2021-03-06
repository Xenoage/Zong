package com.xenoage.zong.musiclayout.spacer.voice;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.DELTA_FLOAT;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.musiclayout.spacer.voice.SingleVoiceSpacer.singleVoiceSpacer;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.VoiceTest;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.notation.RestNotation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.settings.LayoutSettingsTest;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.VoiceSpacing;

/**
 * Tests for {@link SingleVoiceSpacer}.
 * 
 * @author Andreas Wenger
 */
public class SingleVoiceSpacerTest {
	
	private SingleVoiceSpacer testee = singleVoiceSpacer;

	private Rest r1, r2, r3, r4;
	private Chord g1, g2;
	private LayoutSettings layoutSettings;


	@Before public void setUp()
		throws IOException {
		r1 = new Rest(Companion.fr(1, 4));
		r2 = new Rest(Companion.fr(1, 8));
		r3 = new Rest(Companion.fr(1, 8));
		r4 = new Rest(Companion.fr(1, 2));
		g1 = VoiceTest.grace(1);
		g2 = VoiceTest.grace(1);
		layoutSettings = LayoutSettingsTest.loadTestSettings();
	}

	/**
	 * Computes a simple voice spacing.
	 * <pre>
	 * Single elements: [-r1---][-r2-][-r3--][----r4--]
	 * Combined:        --r1--~~r2_~-r3_~~---r4---
	 * </pre> (~: area used by two elements, _: minimal distance between elements)
	 */
	@Test public void testSimple() {
		//create voice and notations
		Voice voice = new Voice(alist((VoiceElement) r1, r2, r3, r4));
		Notations notations = new Notations();
		notations.add(new RestNotation(r1, new ElementWidth(2, 2, 4), null));
		notations.add(new RestNotation(r2, new ElementWidth(2, 2, 2), null));
		notations.add(new RestNotation(r3, new ElementWidth(2, 2, 3), null));
		notations.add(new RestNotation(r4, new ElementWidth(5, 2, 3), null));
		//compute spacing
		VoiceSpacing vs = testee.compute(voice, 200f, Companion.fr(4, 4), 5, notations, layoutSettings);
		//check spacing
		ElementSpacing[] ses = vs.elements.toArray(new ElementSpacing[0]);
		float s = layoutSettings.offsetMeasureStart;
		float d = layoutSettings.spacings.widthDistanceMin;
		assertEquals(5, ses.length);
		assertEquals(s + 2, ses[0].xIs, DELTA_FLOAT);
		assertEquals(s + 8, ses[1].xIs, DELTA_FLOAT);
		assertEquals(s + 12 + d, ses[2].xIs, DELTA_FLOAT);
		assertEquals(s + 19 + 2 * d, ses[3].xIs, DELTA_FLOAT);
		assertEquals(s + 24 + 2 * d, ses[4].xIs, DELTA_FLOAT);
		//check beats
		assertEquals(Companion.fr(0, 8), ses[0].beat);
		assertEquals(Companion.fr(2, 8), ses[1].beat);
		assertEquals(Companion.fr(3, 8), ses[2].beat);
		assertEquals(Companion.fr(4, 8), ses[3].beat);
		assertEquals(Companion.fr(8, 8), ses[4].beat);
	}

	/**
	 * Computes a voice spacing with grace notes,
	 * where the element before the grace notes has enough empty rear space
	 * to take all the grace notes.
	 * <pre>
	 * Single elements: [-r1------------][g1][g2][--r4--]
	 * Combined:        --r1----~g1~g2~~~r4---
	 * </pre> (~: area used by two elements)
	 */
	@Test public void testGrace1() {
		//create voice and notations
		Voice voice = new Voice(alist((VoiceElement) r1, g1, g2, r4));
		Notations notations = new Notations();
		notations.add(new RestNotation(r1, new ElementWidth(2, 2, 13), null));
		notations.add(new ChordNotation(g1, new ElementWidth(1, 2, 1)));
		notations.add(new ChordNotation(g2, new ElementWidth(1, 2, 1)));
		notations.add(new RestNotation(r4, new ElementWidth(3, 2, 3), null));
		//compute spacing
		VoiceSpacing vs = testee.compute(voice, 300f, Companion.fr(4, 4), 5, notations, layoutSettings);
		//check spacing
		ElementSpacing[] ses = vs.elements.toArray(new ElementSpacing[0]);;
		float s = layoutSettings.offsetMeasureStart;
		assertEquals(5, ses.length);
		assertEquals(s + 2, ses[0].xIs, DELTA_FLOAT);
		assertEquals(s + 9, ses[1].xIs, DELTA_FLOAT);
		assertEquals(s + 12, ses[2].xIs, DELTA_FLOAT);
		assertEquals(s + 17, ses[3].xIs, DELTA_FLOAT);
		assertEquals(s + 22, ses[4].xIs, DELTA_FLOAT);
		//check beats
		assertEquals(Companion.fr(0, 8), ses[0].beat);
		assertEquals(Companion.fr(2, 8), ses[1].beat);
		assertEquals(Companion.fr(2, 8), ses[2].beat);
		assertEquals(Companion.fr(2, 8), ses[3].beat);
		assertEquals(Companion.fr(6, 8), ses[4].beat);
	}

	/**
	 * Computes a voice spacing with grace notes,
	 * where the element before the grace notes has enough empty rear space
	 * to take at least one of the grace notes.
	 * <pre>
	 * Single elements: [-r1------][g1][g2][--r4--]
	 * Combined:        --r1_~g1~g2~~~r4---
	 * </pre> (~: area used by two elements, _: minimal distance between elements)
	 */
	@Test public void testGrace2() {
		//create voice and notations
		Voice voice = new Voice(alist((VoiceElement) r1, g1, g2, r4));
		Notations notations = new Notations();
		notations.add(new RestNotation(r1, new ElementWidth(2, 2, 7), null));
		notations.add(new ChordNotation(g1, new ElementWidth(1, 2, 1)));
		notations.add(new ChordNotation(g2, new ElementWidth(1, 2, 1)));
		notations.add(new RestNotation(r4, new ElementWidth(3, 2, 3), null));
		//compute spacing
		VoiceSpacing vs = testee.compute(voice, 400f, Companion.fr(4, 4), 5, notations, layoutSettings);
		//check spacing
		ElementSpacing[] ses = vs.elements.toArray(new ElementSpacing[0]);;
		float s = layoutSettings.offsetMeasureStart;
		float d = layoutSettings.spacings.widthDistanceMin;
		assertEquals(5, ses.length);
		assertEquals(s + 2, ses[0].xIs, DELTA_FLOAT);
		assertEquals(s + 5 + d, ses[1].xIs, DELTA_FLOAT);
		assertEquals(s + 8 + d, ses[2].xIs, DELTA_FLOAT);
		assertEquals(s + 13 + d, ses[3].xIs, DELTA_FLOAT);
		assertEquals(s + 18 + d, ses[4].xIs, DELTA_FLOAT);
		//check beats
		assertEquals(Companion.fr(0, 8), ses[0].beat);
		assertEquals(Companion.fr(2, 8), ses[1].beat);
		assertEquals(Companion.fr(2, 8), ses[2].beat);
		assertEquals(Companion.fr(2, 8), ses[3].beat);
		assertEquals(Companion.fr(6, 8), ses[4].beat);
	}

	/**
	 * Computes a voice spacing with grace notes,
	 * where the element before the grace notes has not enough empty rear space
	 * to take even a single grace notes.
	 * <pre>
	 * Single elements: [-r1--][g1][g2][--r4--]
	 * Combined:        --r1_~g1~g2~~~r4---
	 * </pre> (~: area used by two elements, _: minimal distance between elements)
	 */
	@Test public void testGrace3() {
		//create voice and notations
		Voice voice = new Voice(alist((VoiceElement) r1, g1, g2, r4));
		Notations notations = new Notations();
		notations.add(new RestNotation(r1, new ElementWidth(2, 2, 3), null));
		notations.add(new ChordNotation(g1, new ElementWidth(1, 2, 1)));
		notations.add(new ChordNotation(g2, new ElementWidth(1, 2, 1)));
		notations.add(new RestNotation(r4, new ElementWidth(3, 2, 3), null));
		//compute spacing
		VoiceSpacing vs = testee.compute(voice, 400f, Companion.fr(4, 4), 5, notations, layoutSettings);
		//check spacing
		ElementSpacing[] ses = vs.elements.toArray(new ElementSpacing[0]);;
		float s = layoutSettings.offsetMeasureStart;
		float d = layoutSettings.spacings.widthDistanceMin;
		assertEquals(5, ses.length);
		assertEquals(s + 2, ses[0].xIs, DELTA_FLOAT);
		assertEquals(s + 5 + d, ses[1].xIs, DELTA_FLOAT);
		assertEquals(s + 8 + d, ses[2].xIs, DELTA_FLOAT);
		assertEquals(s + 13 + d, ses[3].xIs, DELTA_FLOAT);
		assertEquals(s + 18 + d, ses[4].xIs, DELTA_FLOAT);
		//check beats
		assertEquals(Companion.fr(0, 8), ses[0].beat);
		assertEquals(Companion.fr(2, 8), ses[1].beat);
		assertEquals(Companion.fr(2, 8), ses[2].beat);
		assertEquals(Companion.fr(2, 8), ses[3].beat);
		assertEquals(Companion.fr(6, 8), ses[4].beat);
	}

}
