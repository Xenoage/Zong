package com.xenoage.zong.musiclayout.spacer.system.fill;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.music.chord.ChordFactory.graceChord;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.musiclayout.spacer.system.fill.StretchMeasures.stretchMeasures;
import static com.xenoage.zong.musiclayout.spacing.ElementSpacing.empty;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.spacing.BeatOffset;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.LeadingSpacingMock;
import com.xenoage.zong.musiclayout.spacing.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import com.xenoage.zong.musiclayout.spacing.VoiceSpacing;

/**
 * Tests for {@link StretchMeasures}.
 * 
 * @author Andreas Wenger
 */
public class StretchMeasuresTest {
	
	private StretchMeasures testee = stretchMeasures;

	
	@Test public void computeTest() {
		//create an easy system for testing
		float leadingWidth = 4;
		float offsetBeat1 = 3;
		float offsetBeat2 = 7;
		float offsetBeat3 = 12;
		SystemSpacing system = createSystemWith1Measure(leadingWidth, offsetBeat1, offsetBeat2,
			offsetBeat3);

		//stretch the system
		float newWidth = 20;
		testee.compute(system, newWidth);

		//compare the result
		//since the leading spacing (4 spaces) is not scaled, the
		//remaining 12 spaces of the voices width have to be scaled
		float stretch = (newWidth - leadingWidth) / offsetBeat3;
		ColumnSpacing newCol = system.columns.get(0);
		//beat offsets
		assertEquals(offsetBeat1 * stretch, newCol.getBeatOffsets().get(0).getOffsetMm(), df);
		assertEquals(offsetBeat2 * stretch, newCol.getBeatOffsets().get(1).getOffsetMm(), df);
		//element spacings
		VoiceSpacing newVoice = newCol.measures.get(0).getVoiceSpacings().get(0);
		assertEquals(offsetBeat1 * stretch, newVoice.elements.get(0).offsetIs, df);
		assertEquals(offsetBeat2 * stretch, newVoice.elements.get(1).offsetIs, df);
	}

	/**
	 * Checking if the elements are stretched correctly in a simple system
	 * where there are grace notes. The space between grace notes and their following
	 * full chord may not be stretched.
	 */
	@Test public void computeSystemArrangementTestGrace() {
		//create an easy system for testing
		float offsetChord1 = 3;
		float offsetChord2 = 12;
		float offsetMeasureEnd = 16;
		float graceDistance = 2;
		SystemSpacing system = createSystemWith1MeasureGrace(offsetChord1, offsetChord2,
			offsetMeasureEnd, graceDistance);

		//stretch the system
		float newWidth = 28;
		testee.compute(system, newWidth);

		//compare the result
		ColumnSpacing newCol = system.columns.get(0);
		float stretch = (newWidth - +newCol.getLeadingWidthMm()) / offsetMeasureEnd;
		//beat offsets
		assertEquals(offsetChord1 * stretch, newCol.getBeatOffsets().get(0).getOffsetMm(),
			df);
		assertEquals(offsetChord2 * stretch, newCol.getBeatOffsets().get(1).getOffsetMm(),
			df);
		//element spacings
		VoiceSpacing newVoice = newCol.measures.get(0).getVoiceSpacings().get(0);
		assertEquals(offsetChord1 * stretch, newVoice.elements.get(0).offsetIs,
			df);
		assertEquals(offsetChord2 * stretch - 2 * graceDistance,
			newVoice.elements.get(1).offsetIs, df);
		assertEquals(offsetChord2 * stretch - 1 * graceDistance,
			newVoice.elements.get(2).offsetIs, df);
		assertEquals(offsetChord2 * stretch, newVoice.elements.get(3).offsetIs,
			df);
	}

	/**
	 * Creates and returns a simple {@link SystemSpacing} with only one
	 * measure with a clef and two notes, using the given parameters.
	 * @param leadingWidth  width of the leading spacing in mm
	 * @param offsetBeat0   offset of beat 1/4 in mm
	 * @param offsetBeat1   offset of beat 3/4 in mm
	 * @param offsetBeat2   width of the voice spacing in mm
	 */
	public static SystemSpacing createSystemWith1Measure(float leadingWidth, float offsetBeat0,
		float offsetBeat1, float offsetBeat2) {
		Chord chord1 = chord(pi(0, 0, 4), fr(2, 4));
		Chord chord2 = chord(pi(1, 0, 4), fr(2, 4));
		Voice voice = new Voice(alist(chord1, chord2));
		List<BeatOffset> beatOffsets = alist(new BeatOffset(fr(1, 4), offsetBeat0), new BeatOffset(
			fr(3, 4), offsetBeat1), new BeatOffset(fr(5, 4), offsetBeat2));
		float is = 1;
		List<VoiceSpacing> voiceSpacings = alist(new VoiceSpacing(voice, is, alist(
			new ElementSpacing(new ChordNotation(chord1), beatOffsets.get(0).getBeat(), beatOffsets.get(0).getOffsetMm()),
			new ElementSpacing(new ChordNotation(chord2), beatOffsets.get(1).getBeat(), beatOffsets.get(1).getOffsetMm()))));
		MeasureSpacing measureSpacing = new MeasureSpacing(atMeasure(0, 0), is, voiceSpacings,
			empty, LeadingSpacingMock.createGClefSpacing(leadingWidth));
		List<MeasureSpacing> measureSpacings = alist(measureSpacing);
		ColumnSpacing mcs = new ColumnSpacing(-1, measureSpacings, beatOffsets,
			alist(new BeatOffset(fr(0, 4), 0), new BeatOffset(fr(6, 4), offsetBeat2)));
		SystemSpacing system = new SystemSpacing(ilist(mcs), 0, 0, leadingWidth +
			offsetBeat2, new float[]{0f}, new float[0], 0);
		return system;
	}

	/**
	 * Creates and returns a simple {@link SystemSpacing} with only one
	 * measure and three notes: two main notes and two grace notes between them.
	 */
	public static SystemSpacing createSystemWith1MeasureGrace(float offsetChord1,
		float offsetChord2, float offsetMeasureEnd, float graceDistance) {
		Chord chord1 = chord(pi(0, 0, 4), fr(2, 4));
		Chord chord2grace = graceChord(pi(1, 0, 4));
		Chord chord3grace = graceChord(pi(2, 0, 4));
		Chord chord4 = chord(pi(3, 0, 4), fr(2, 4));
		Voice voice = new Voice(alist(chord1, chord2grace, chord3grace, chord4));
		List<BeatOffset> beatOffsets = alist(new BeatOffset(fr(0, 4), offsetChord1), new BeatOffset(
			fr(2, 4), offsetChord2), new BeatOffset(fr(4, 4), offsetMeasureEnd));
		float is = 1;
		List<VoiceSpacing> voiceSpacings = alist(new VoiceSpacing(voice, is, alist(
			new ElementSpacing(new ChordNotation(chord1), beatOffsets.get(0).getBeat(),
				beatOffsets.get(0).getOffsetMm()),
			new ElementSpacing(new ChordNotation(chord2grace), beatOffsets.get(1).getBeat(),
				beatOffsets.get(1).getOffsetMm() - 2 * graceDistance),
			new ElementSpacing(new ChordNotation(chord3grace),
				beatOffsets.get(1).getBeat(), beatOffsets.get(1).getOffsetMm() - 1 * graceDistance),
			new ElementSpacing(new ChordNotation(chord4), beatOffsets.get(1).getBeat(),
				beatOffsets.get(1).getOffsetMm()))));
		MeasureSpacing measureSpacing = new MeasureSpacing(atMeasure(0, 0), is, voiceSpacings, empty, null);
		ColumnSpacing mcs = new ColumnSpacing(-1, alist(measureSpacing),
			beatOffsets, alist(new BeatOffset(fr(0, 4), 0), new BeatOffset(fr(4, 4), offsetMeasureEnd)));
		SystemSpacing system = new SystemSpacing(alist(mcs), 0, 0, offsetMeasureEnd,
			new float[]{0f}, new float[0], 0);
		return system;
	}

}
