package com.xenoage.zong.musiclayout.layouter.horizontalsystemfilling;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.music.chord.ChordFactory.graceChord;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.xenoage.utils.collections.IList;
import com.xenoage.utils.math.Delta;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.SystemArrangement;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.LeadingSpacingMock;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureElementsSpacings;
import com.xenoage.zong.musiclayout.spacing.horizontal.MeasureSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Test cases for a {@link StretchHorizontalSystemFillingStrategy}.
 * 
 * @author Andreas Wenger
 */
public class StretchHorizontalSystemFillingStrategyTest {

	/**
	 * Checking if the elements are stretched correctly in a simple system.
	 */
	@Test public void computeSystemArrangementTest() {
		//create an easy system for testing
		float leadingWidth = 4;
		float offsetBeat1 = 3;
		float offsetBeat2 = 7;
		float offsetBeat3 = 12;
		SystemArrangement system = createSystemWith1Measure(leadingWidth, offsetBeat1, offsetBeat2,
			offsetBeat3);

		//stretch the system
		StretchHorizontalSystemFillingStrategy strategy = StretchHorizontalSystemFillingStrategy.instance;
		float newWidth = 20;
		system = strategy.computeSystemArrangement(system, newWidth);

		//compare the result
		//since the leading spacing (4 spaces) is not scaled, the
		//remaining 12 spaces of the voices width have to be scaled
		float stretch = (newWidth - leadingWidth) / offsetBeat3;
		ColumnSpacing newCol = system.getColumnSpacings().get(0);
		//beat offsets
		assertEquals(offsetBeat1 * stretch, newCol.getBeatOffsets()[0].getOffsetMm(),
			Delta.DELTA_FLOAT);
		assertEquals(offsetBeat2 * stretch, newCol.getBeatOffsets()[1].getOffsetMm(),
			Delta.DELTA_FLOAT);
		//element spacings
		VoiceSpacing newVoice = newCol.getMeasureSpacings()[0].getVoiceSpacings().get(0);
		assertEquals(offsetBeat1 * stretch, newVoice.spacingElements[0].offsetIs,
			Delta.DELTA_FLOAT);
		assertEquals(offsetBeat2 * stretch, newVoice.spacingElements[1].offsetIs,
			Delta.DELTA_FLOAT);

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
		SystemArrangement system = createSystemWith1MeasureGrace(offsetChord1, offsetChord2,
			offsetMeasureEnd, graceDistance);

		//stretch the system
		StretchHorizontalSystemFillingStrategy strategy = StretchHorizontalSystemFillingStrategy.instance;
		float newWidth = 28;
		system = strategy.computeSystemArrangement(system, newWidth);

		//compare the result
		ColumnSpacing newCol = system.getColumnSpacings().get(0);
		float stretch = (newWidth - +newCol.getLeadingWidth()) / offsetMeasureEnd;
		//beat offsets
		assertEquals(offsetChord1 * stretch, newCol.getBeatOffsets()[0].getOffsetMm(),
			Delta.DELTA_FLOAT);
		assertEquals(offsetChord2 * stretch, newCol.getBeatOffsets()[1].getOffsetMm(),
			Delta.DELTA_FLOAT);
		//element spacings
		VoiceSpacing newVoice = newCol.getMeasureSpacings()[0].getVoiceSpacings().get(0);
		assertEquals(offsetChord1 * stretch, newVoice.spacingElements[0].offsetIs,
			Delta.DELTA_FLOAT);
		assertEquals(offsetChord2 * stretch - 2 * graceDistance,
			newVoice.spacingElements[1].offsetIs, Delta.DELTA_FLOAT);
		assertEquals(offsetChord2 * stretch - 1 * graceDistance,
			newVoice.spacingElements[2].offsetIs, Delta.DELTA_FLOAT);
		assertEquals(offsetChord2 * stretch, newVoice.spacingElements[3].offsetIs,
			Delta.DELTA_FLOAT);
	}

	/**
	 * Creates and returns a simple {@link SystemArrangement} with only one
	 * measure with a clef and two notes, using the given parameters.
	 * @param leadingWidth  width of the leading spacing in mm
	 * @param offsetBeat0   offset of beat 1/4 in mm
	 * @param offsetBeat1   offset of beat 3/4 in mm
	 * @param offsetBeat2   width of the voice spacing in mm
	 */
	public static SystemArrangement createSystemWith1Measure(float leadingWidth, float offsetBeat0,
		float offsetBeat1, float offsetBeat2) {
		Voice voice = new Voice(alist((VoiceElement) chord(pi(0, 0, 4), fr(2, 4)), chord(
			pi(1, 0, 4), fr(2, 4))));
		BeatOffset[] beatOffsets = { new BeatOffset(fr(1, 4), offsetBeat0), new BeatOffset(
			fr(3, 4), offsetBeat1), new BeatOffset(fr(5, 4), offsetBeat2) };
		IList<VoiceSpacing> voiceSpacings = ilist(new VoiceSpacing(voice, 1, new SpacingElement[]{
			new SpacingElement(voice.getElement(0), beatOffsets[0].getBeat(), beatOffsets[0].getOffsetMm()),
			new SpacingElement(voice.getElement(1), beatOffsets[1].getBeat(), beatOffsets[1]
				.getOffsetMm())}));
		MeasureSpacing measureSpacing = new MeasureSpacing(atMeasure(0, 0), voiceSpacings,
			MeasureElementsSpacings.empty, LeadingSpacingMock.createGClefSpacing(leadingWidth));
		MeasureSpacing[] measureSpacings = { measureSpacing };
		ColumnSpacing mcs = new ColumnSpacing(new Score(), measureSpacings, beatOffsets,
			new BeatOffset[] { new BeatOffset(fr(0, 4), 0), new BeatOffset(fr(6, 4), offsetBeat2) });
		SystemArrangement system = new SystemArrangement(10, 10, ilist(mcs), 0, 0, leadingWidth +
			offsetBeat2, new float[]{0f}, new float[0], 0);
		return system;
	}

	/**
	 * Creates and returns a simple {@link SystemArrangement} with only one
	 * measure and three notes: two main notes and two grace notes between them.
	 */
	public static SystemArrangement createSystemWith1MeasureGrace(float offsetChord1,
		float offsetChord2, float offsetMeasureEnd, float graceDistance) {
		Voice voice = new Voice(alist((VoiceElement) chord(pi(0, 0, 4), fr(2, 4)), graceChord(pi(
			1, 0, 4)), graceChord(pi(2, 0, 4)), chord(pi(3, 0, 4), fr(2, 4))));
		BeatOffset[] beatOffsets = { new BeatOffset(fr(0, 4), offsetChord1), new BeatOffset(
			fr(2, 4), offsetChord2), new BeatOffset(fr(4, 4), offsetMeasureEnd) };
		IList<VoiceSpacing> voiceSpacings = ilist(new VoiceSpacing(voice, 1, new SpacingElement[]{new SpacingElement(
			voice.getElement(0), beatOffsets[0].getBeat(), beatOffsets[0].getOffsetMm()),
			new SpacingElement(voice.getElement(1), beatOffsets[1].getBeat(), beatOffsets[1]
				.getOffsetMm() - 2 * graceDistance, true), new SpacingElement(voice.getElement(2),
				beatOffsets[1].getBeat(), beatOffsets[1].getOffsetMm() - 1 * graceDistance, true),
			new SpacingElement(voice.getElement(3), beatOffsets[1].getBeat(), beatOffsets[1]
				.getOffsetMm())}));
		MeasureSpacing measureSpacing = new MeasureSpacing(atMeasure(0, 0), voiceSpacings,
			MeasureElementsSpacings.empty, null);
		ColumnSpacing mcs = new ColumnSpacing(new Score(), new MeasureSpacing[] { measureSpacing },
			beatOffsets, new BeatOffset[] { new BeatOffset(fr(0, 4), 0), new BeatOffset(fr(4, 4), offsetMeasureEnd) });
		SystemArrangement system = new SystemArrangement(10, 10, ilist(mcs), 0, 0, offsetMeasureEnd,
			new float[]{0f}, new float[0], 0);
		return system;
	}

}
