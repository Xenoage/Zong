package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.math.Delta.DELTA_FLOAT;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.VoiceTest;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.settings.LayoutSettingsTest;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementWidth;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;


/**
 * Test cases for the {@link SeparateVoiceSpacingStrategy}.
 * 
 * @author Andreas Wenger
 */
public class SeparateVoiceSpacingStrategyTest
{
	
	private Rest r1, r2, r3, r4;
	private Chord g1, g2;
	private LayoutSettings layoutSettings;
	
	
	@Before public void setUp()
	{
		r1 = new Rest(fr(1, 4));
		r2 = new Rest(fr(1, 8));
		r3 = new Rest(fr(1, 8));
		r4 = new Rest(fr(1, 2));
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
	@Test public void computeVoiceSpacingTestSimple()
	{
		//create voice and notations
		Voice voice = new Voice(pvec((VoiceElement) r1, r2, r3, r4));
		NotationsCache notations = NotationsCache.create();
		notations.add(new RestNotation(r1, new ElementWidth(2, 2, 4)));
		notations.add(new RestNotation(r2, new ElementWidth(2, 2, 2)));
		notations.add(new RestNotation(r3, new ElementWidth(2, 2, 3)));
		notations.add(new RestNotation(r4, new ElementWidth(5, 2, 3)));
		//compute spacing
		VoiceSpacing vs = new SeparateVoiceSpacingStrategy().computeVoiceSpacing(voice,
			200f, notations, fr(4, 4), layoutSettings);
		//check spacing
		Vector<SpacingElement> ses = vs.getSpacingElements();
		float s = layoutSettings.offsetMeasureStart;
		float d = layoutSettings.spacings.widthDistanceMin;
		assertEquals(5, ses.size());
		assertEquals(s + 2, ses.get(0).offset, DELTA_FLOAT);
		assertEquals(s + 8, ses.get(1).offset, DELTA_FLOAT);
		assertEquals(s + 12 + d, ses.get(2).offset, DELTA_FLOAT);
		assertEquals(s + 19 + 2*d, ses.get(3).offset, DELTA_FLOAT);
		assertEquals(s + 24 + 2*d, ses.get(4).offset, DELTA_FLOAT);
		//check beats
		assertEquals(fr(0, 8), ses.get(0).beat);
		assertEquals(fr(2, 8), ses.get(1).beat);
		assertEquals(fr(3, 8), ses.get(2).beat);
		assertEquals(fr(4, 8), ses.get(3).beat);
		assertEquals(fr(8, 8), ses.get(4).beat);
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
	@Test public void computeVoiceSpacingTestGrace1()
	{
		//create voice and notations
		Voice voice = new Voice(pvec((VoiceElement) r1, g1, g2, r4));
		NotationsCache notations = NotationsCache.create();
		notations.add(new RestNotation(r1, new ElementWidth(2, 2, 13)));
		notations.add(new ChordNotation(g1, new ElementWidth(1, 2, 1), null, null, null, null, null));
		notations.add(new ChordNotation(g2, new ElementWidth(1, 2, 1), null, null, null, null, null));
		notations.add(new RestNotation(r4, new ElementWidth(3, 2, 3)));
		//compute spacing
		VoiceSpacing vs = new SeparateVoiceSpacingStrategy().computeVoiceSpacing(voice,
			300f, notations, fr(4, 4), layoutSettings);
		//check spacing
		Vector<SpacingElement> ses = vs.getSpacingElements();
		float s = layoutSettings.offsetMeasureStart;
		assertEquals(5, ses.size());
		assertEquals(s + 2, ses.get(0).offset, DELTA_FLOAT);
		assertEquals(s + 9, ses.get(1).offset, DELTA_FLOAT);
		assertEquals(s + 12, ses.get(2).offset, DELTA_FLOAT);
		assertEquals(s + 17, ses.get(3).offset, DELTA_FLOAT);
		assertEquals(s + 22, ses.get(4).offset, DELTA_FLOAT);
		//check beats
		assertEquals(fr(0, 8), ses.get(0).beat);
		assertEquals(fr(2, 8), ses.get(1).beat);
		assertEquals(fr(2, 8), ses.get(2).beat);
		assertEquals(fr(2, 8), ses.get(3).beat);
		assertEquals(fr(6, 8), ses.get(4).beat);
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
	@Test public void computeVoiceSpacingTestGrace2()
	{
		//create voice and notations
		Voice voice = new Voice(pvec((VoiceElement) r1, g1, g2, r4));
		NotationsCache notations = NotationsCache.create();
		notations.add(new RestNotation(r1, new ElementWidth(2, 2, 7)));
		notations.add(new ChordNotation(g1, new ElementWidth(1, 2, 1), null, null, null, null, null));
		notations.add(new ChordNotation(g2, new ElementWidth(1, 2, 1), null, null, null, null, null));
		notations.add(new RestNotation(r4, new ElementWidth(3, 2, 3)));
		//compute spacing
		VoiceSpacing vs = new SeparateVoiceSpacingStrategy().computeVoiceSpacing(voice,
			400f, notations, fr(4, 4), layoutSettings);
		//check spacing
		Vector<SpacingElement> ses = vs.getSpacingElements();
		float s = layoutSettings.offsetMeasureStart;
		float d = layoutSettings.spacings.widthDistanceMin;
		assertEquals(5, ses.size());
		assertEquals(s + 2, ses.get(0).offset, DELTA_FLOAT);
		assertEquals(s + 5 + d, ses.get(1).offset, DELTA_FLOAT);
		assertEquals(s + 8 + d, ses.get(2).offset, DELTA_FLOAT);
		assertEquals(s + 13 + d, ses.get(3).offset, DELTA_FLOAT);
		assertEquals(s + 18 + d, ses.get(4).offset, DELTA_FLOAT);
		//check beats
		assertEquals(fr(0, 8), ses.get(0).beat);
		assertEquals(fr(2, 8), ses.get(1).beat);
		assertEquals(fr(2, 8), ses.get(2).beat);
		assertEquals(fr(2, 8), ses.get(3).beat);
		assertEquals(fr(6, 8), ses.get(4).beat);
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
	@Test public void computeVoiceSpacingTestGrace3()
	{
		//create voice and notations
		Voice voice = new Voice(pvec((VoiceElement) r1, g1, g2, r4));
		NotationsCache notations = NotationsCache.create();
		notations.add(new RestNotation(r1, new ElementWidth(2, 2, 3)));
		notations.add(new ChordNotation(g1, new ElementWidth(1, 2, 1), null, null, null, null, null));
		notations.add(new ChordNotation(g2, new ElementWidth(1, 2, 1), null, null, null, null, null));
		notations.add(new RestNotation(r4, new ElementWidth(3, 2, 3)));
		//compute spacing
		VoiceSpacing vs = new SeparateVoiceSpacingStrategy().computeVoiceSpacing(voice,
			400f, notations, fr(4, 4), layoutSettings);
		//check spacing
		Vector<SpacingElement> ses = vs.getSpacingElements();
		float s = layoutSettings.offsetMeasureStart;
		float d = layoutSettings.spacings.widthDistanceMin;
		assertEquals(5, ses.size());
		assertEquals(s + 2, ses.get(0).offset, DELTA_FLOAT);
		assertEquals(s + 5 + d, ses.get(1).offset, DELTA_FLOAT);
		assertEquals(s + 8 + d, ses.get(2).offset, DELTA_FLOAT);
		assertEquals(s + 13 + d, ses.get(3).offset, DELTA_FLOAT);
		assertEquals(s + 18 + d, ses.get(4).offset, DELTA_FLOAT);
		//check beats
		assertEquals(fr(0, 8), ses.get(0).beat);
		assertEquals(fr(2, 8), ses.get(1).beat);
		assertEquals(fr(2, 8), ses.get(2).beat);
		assertEquals(fr(2, 8), ses.get(3).beat);
		assertEquals(fr(6, 8), ses.get(4).beat);
	}

}
