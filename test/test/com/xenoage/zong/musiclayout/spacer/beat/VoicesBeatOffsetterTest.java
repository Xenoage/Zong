package com.xenoage.zong.musiclayout.spacer.beat;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Measure;
import com.xenoage.zong.core.music.MeasureElement;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.clef.Clef;
import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.core.music.key.TraditionalKey;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.io.musicxml.in.MusicXmlScoreFileInputTest;
import com.xenoage.zong.io.selection.Cursor;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.spacing.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.music.chord.ChordFactory.graceChord;
import static com.xenoage.zong.core.music.time.TimeType.time_4_4;
import static com.xenoage.zong.core.position.MP.*;
import static com.xenoage.zong.musiclayout.spacer.beat.VoicesBeatOffsetter.voicesBeatOffsetter;
import static com.xenoage.zong.musiclayout.spacing.TestSpacing.spacing;
import static org.junit.Assert.*;

/**
 * Tests for a {@link VoicesBeatOffsetter}.
 * 
 * @author Andreas Wenger
 */
public class VoicesBeatOffsetterTest {

	private VoicesBeatOffsetter testee = voicesBeatOffsetter;
	
	private final float width_grace = 1f;
	private final float width_1_8 = 1.5f;
	private final float width_1_6 = 1.7f;
	private final float width_1_4 = 2f;
	private final float width_3_8 = 2.5f;
	private final float width_1_2 = 3f;
	private final float width_1_1 = 5f;

	private final Fraction dur_1_8 = Companion.fr(1, 8);
	private final Fraction dur_1_6 = Companion.fr(1, 6);
	private final Fraction dur_1_4 = Companion.fr(1, 4);
	private final Fraction dur_3_8 = Companion.fr(3, 8);
	private final Fraction dur_1_2 = Companion.fr(1, 2);
	private final Fraction dur_1_1 = Companion.fr(1, 1);
	
	private final float minimalBeatsOffsetIs = 0.1f;


	@Test public void computeVoicesBeatsTest() {
		//must have 5 beats at 0, 2, 3, 4, 8.
		List<Fraction> beats = testee.computeVoicesBeats(
			createVoiceSpacings(createTestScore1Voice())).getLinkedList();
		assertEquals(5, beats.size());
		assertEquals(Companion.fr(0, 4), beats.get(0));
		assertEquals(Companion.fr(2, 8), beats.get(1));
		assertEquals(Companion.fr(3, 8), beats.get(2));
		assertEquals(Companion.fr(4, 8), beats.get(3));
		assertEquals(Companion.fr(8, 8), beats.get(4));
	}

	@Test public void computeDistanceTest() {
		Voice voice = createTestScore1Voice().getVoice(mp0);
		List<ElementSpacing> spacings = createTestElementSpacings1Voice();
		LinkedList<BeatOffset> emptyList = new LinkedList<>();
		//distance: the offsets of the notes and rests are interesting,
		//not the ones of the clefs, key signatures and time signatures,
		//so the method has to use the last occurrence of a beat.
		//distance between beat 0 and 4: must be 6
		float distance = testee.computeMinimalDistance(Companion.fr(0), Companion.fr(4, 4), false, voice, spacings,
			emptyList, 1);
		assertEquals(6, distance, Delta.DELTA_FLOAT);
		//distance between beat 0 and 5: must be 0 (beat 5 isn't used)
		distance = testee.computeMinimalDistance(Companion.fr(0), Companion.fr(5, 4), false, voice, spacings, emptyList,
			1);
		assertEquals(0, distance, Delta.DELTA_FLOAT);
		//distance between beat 0 and 2: must be 2
		distance = testee.computeMinimalDistance(Companion.fr(0), Companion.fr(2, 4), false, voice, spacings, emptyList,
			1);
		assertEquals(2, distance, Delta.DELTA_FLOAT);
		//distance between beat 5 and 8: must be 0 (beat 5 isn't used)
		distance = testee.computeMinimalDistance(Companion.fr(5), Companion.fr(8, 4), false, voice, spacings, emptyList,
			1);
		assertEquals(0, distance, Delta.DELTA_FLOAT);
	}

	/**
	 * Compute offsets of the common beats.
	 */
	@Test public void computeTest1() {
		Score score = createTestScore3Voices();

		BeatOffset[] beatOffsets = testee.compute(
			createVoiceSpacings(score), Companion.fr(4, 4), minimalBeatsOffsetIs).toArray(new BeatOffset[0]);
		float is = score.getFormat().getInterlineSpace();

		//2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet
		//^: dominating voice
		//voice 1:   | 4     4     4     4     |     
		//                               ^^^^^
		//voice 2:   | 8  8  8  8  2           |
		//             ^^^^^^^^^^^^
		//voice 3:   | 3   3   3   8  8  4     |
		//                         ^^^^^^
		//used:        *  ** * **  *  *  *     *
		//checked:     *     *     *     *     *
		assertEquals(10, beatOffsets.length);
		assertEquals(Companion.fr(0, 4), beatOffsets[0].getBeat());
		assertEquals((0) * is, beatOffsets[0].getOffsetMm(), df);
		assertEquals(Companion.fr(1, 4), beatOffsets[3].getBeat());
		assertEquals((2 * width_1_8) * is, beatOffsets[3].getOffsetMm(), df);
		assertEquals(Companion.fr(2, 4), beatOffsets[6].getBeat());
		assertEquals((4 * width_1_8) * is, beatOffsets[6].getOffsetMm(), df);
		assertEquals(Companion.fr(3, 4), beatOffsets[8].getBeat());
		assertEquals((6 * width_1_8) * is, beatOffsets[8].getOffsetMm(), df);
		assertEquals(Companion.fr(4, 4), beatOffsets[9].getBeat());
		assertEquals((6 * width_1_8 + width_1_4) * is, beatOffsets[9].getOffsetMm(), df);
	}

	/**
	 * Compute offsets of the common beats,
	 * this time for an incomplete measure.
	 */
	@Test public void computeTest2() {
		Score score = createTestScore3VoicesIncomplete();
		BeatOffset[] beatOffsets = testee.compute(
			createVoiceSpacings(score), Companion.fr(4, 4), minimalBeatsOffsetIs).toArray(new BeatOffset[0]);
		float is = score.getFormat().getInterlineSpace();

		//2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet, x: missing (empty)
		//^: dominating voice
		//voice 1:   | 4     4     4     8  xxx|
		//                         ^^^^^^^^^^^^
		//voice 2:   | 8  8  8  8  4     xxxxxx|
		//             ^^^^^^^^^^^^
		//voice 3:   | 3   3   3   8  xxxxxxxxx|
		//                         
		//used:        *  ** * **  *     *  °  *    (°: total final used beat)   
		//checked:     *     *     *     *  *  *

		assertEquals(10, beatOffsets.length);
		assertEquals(Companion.fr(0, 4), beatOffsets[0].getBeat());
		assertEquals((0) * is, beatOffsets[0].getOffsetMm(), df);
		assertEquals(Companion.fr(1, 4), beatOffsets[3].getBeat());
		assertEquals((2 * width_1_8) * is, beatOffsets[3].getOffsetMm(), df);
		assertEquals(Companion.fr(2, 4), beatOffsets[6].getBeat());
		assertEquals((4 * width_1_8) * is, beatOffsets[6].getOffsetMm(), df);
		assertEquals(Companion.fr(3, 4), beatOffsets[7].getBeat());
		assertEquals((4 * width_1_8 + width_1_4) * is, beatOffsets[7].getOffsetMm(), df);
		assertEquals(Companion.fr(7, 8), beatOffsets[8].getBeat());
		assertEquals((4 * width_1_8 + width_1_4 + width_1_8) * is, beatOffsets[8].getOffsetMm(), df);
		assertEquals(Companion.fr(4, 4), beatOffsets[9].getBeat());
		assertEquals((4 * width_1_8 + width_1_4 + width_1_8 + minimalBeatsOffsetIs) * is,
			beatOffsets[9].getOffsetMm(), df);
	}

	/**
	 * Compute offsets of the common beats, when also grace notes are used.
	 */
	@Test public void computeTestGrace() {
		Score score = createTestScore3VoicesGrace();

		BeatOffset[] beatOffsets = testee.compute(
			createVoiceSpacings(score), Companion.fr(4, 4), minimalBeatsOffsetIs).toArray(new BeatOffset[0]);
		float is = score.getFormat().getInterlineSpace();

		//2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet
		//^: dominating voice

		//voice 1: | 4     ...4       4     4     |
		//           ^^^^^^^^^              ^^^^^
		//voice 2: | 8  8     8  8  ..2           |
		//                    ^^^^^^^^
		//voice 3: | 3   3      3    .8  8  4     |
		//                            ^^^^^^
		//used:      *  **    * **    *  *  *     *
		//checked:   *        *       *     *     *
		assertEquals(10, beatOffsets.length);
		assertEquals(Companion.fr(0, 4), beatOffsets[0].getBeat());
		assertEquals((0) * is, beatOffsets[0].getOffsetMm(), df);
		assertEquals(Companion.fr(1, 4), beatOffsets[3].getBeat());
		float offset1 = width_1_4 + 3 * width_grace;
		assertEquals(offset1 * is, beatOffsets[3].getOffsetMm(), df);
		assertEquals(Companion.fr(2, 4), beatOffsets[6].getBeat());
		float offset2 = offset1 + 2 * width_1_8 + 2 * width_grace;
		assertEquals(offset2 * is, beatOffsets[6].getOffsetMm(), df);
		assertEquals(Companion.fr(3, 4), beatOffsets[8].getBeat());
		float offset3 = offset2 + 2 * width_1_8;
		assertEquals(offset3 * is, beatOffsets[8].getOffsetMm(), df);
		assertEquals(Companion.fr(4, 4), beatOffsets[9].getBeat());
		float offset4 = offset3 + width_1_4;
		assertEquals(offset4 * is, beatOffsets[9].getOffsetMm(), df);
	}

	/**
	 * Test file "BeatOffsetsStrategyTest-1.xml".
	 */
	@Test public void computeBeatOffsets_File1() {
		Score score = MusicXmlScoreFileInputTest.loadXMLTestScore("VoicesBeatOffsetterTest-1.xml");

		LinkedList<VoiceSpacing> voiceSpacings = createVoiceSpacings(score);
		BeatOffset[] beatOffsets = testee.compute(voiceSpacings,
			Companion.fr(3, 4), minimalBeatsOffsetIs).toArray(new BeatOffset[0]);

		//file must have 5 beat offsets with increasing mm offsets
		assertEquals(5, beatOffsets.length);
		assertEquals(Companion.fr(0, 4), beatOffsets[0].getBeat());
		assertEquals(Companion.fr(1, 4), beatOffsets[1].getBeat());
		assertEquals(Companion.fr(2, 4), beatOffsets[2].getBeat());
		assertEquals(Companion.fr(5, 8), beatOffsets[3].getBeat());
		assertEquals(Companion.fr(3, 4), beatOffsets[4].getBeat());
		for (int i = 0; i < beatOffsets.length - 1; i++) {
			assertTrue(beatOffsets[i].getOffsetMm() < beatOffsets[i + 1].getOffsetMm());
		}

		//distance between beat 1/4 and 2/4 must be width_1_4
		float is = score.getFormat().getInterlineSpace();
		assertEquals(width_1_4 * is, beatOffsets[2].getOffsetMm() - beatOffsets[1].getOffsetMm(), df);
	}

	/**
	 * Test file "BeatOffsetsStrategyTest-2.xml".
	 */
	@Test public void computeBeatOffsets_File2() {
		Score score = MusicXmlScoreFileInputTest.loadXMLTestScore("VoicesBeatOffsetterTest-2.xml");

		LinkedList<VoiceSpacing> voiceSpacings = createVoiceSpacings(score);
		BeatOffset[] beatOffsets = testee.compute(voiceSpacings,
			Companion.fr(3, 4), minimalBeatsOffsetIs).toArray(new BeatOffset[0]);

		//file must have 6 beat offsets with increasing mm offsets
		assertEquals(6, beatOffsets.length);
		assertEquals(Companion.fr(0, 4), beatOffsets[0].getBeat());
		assertEquals(Companion.fr(1, 8), beatOffsets[1].getBeat());
		assertEquals(Companion.fr(1, 4), beatOffsets[2].getBeat());
		assertEquals(Companion.fr(2, 4), beatOffsets[3].getBeat());
		assertEquals(Companion.fr(5, 8), beatOffsets[4].getBeat());
		assertEquals(Companion.fr(3, 4), beatOffsets[5].getBeat());
		for (int i = 0; i < beatOffsets.length - 1; i++) {
			assertTrue(beatOffsets[i].getOffsetMm() < beatOffsets[i + 1].getOffsetMm());
		}

		//distance between beat 1/4 and 2/4 must be width_1_4
		float is = score.getFormat().getInterlineSpace();
		assertEquals(width_1_4 * is, beatOffsets[3].getOffsetMm() - beatOffsets[2].getOffsetMm(),
			df);
	}

	/**
	 * Test file "BeatOffsetsStrategyTest-3.xml".
	 */
	@Test public void computeBeatOffsets_File3() {
		Score score = MusicXmlScoreFileInputTest.loadXMLTestScore("VoicesBeatOffsetterTest-3.xml");

		LinkedList<VoiceSpacing> voiceSpacings = createVoiceSpacings(score);
		BeatOffset[] beatOffsets = testee.compute(voiceSpacings,
			Companion.fr(5, 4), minimalBeatsOffsetIs).toArray(new BeatOffset[0]);

		//file must have 8 beat offsets with increasing mm offsets
		//special difficulty: last eighth note must be further to the right as preceding quarter
		//in other voice, even the distance between the whole note and the last eighth would be big enough
		assertEquals(8, beatOffsets.length);
		assertEquals(Companion.fr(0, 4), beatOffsets[0].getBeat());
		assertEquals(Companion.fr(1, 8), beatOffsets[1].getBeat());
		assertEquals(Companion.fr(1, 4), beatOffsets[2].getBeat());
		assertEquals(Companion.fr(2, 4), beatOffsets[3].getBeat());
		assertEquals(Companion.fr(3, 4), beatOffsets[4].getBeat());
		assertEquals(Companion.fr(4, 4), beatOffsets[5].getBeat());
		assertEquals(Companion.fr(9, 8), beatOffsets[6].getBeat());
		assertEquals(Companion.fr(5, 4), beatOffsets[7].getBeat());
		for (int i = 0; i < beatOffsets.length - 1; i++) {
			assertTrue("beat " + (i + 1) + " wrong",
				beatOffsets[i].getOffsetMm() < beatOffsets[i + 1].getOffsetMm());
		}

		//distance between beat 1/4 and 2/4 must be width_1_4
		float is = score.getFormat().getInterlineSpace();
		assertEquals(width_1_4 * is, beatOffsets[3].getOffsetMm() - beatOffsets[2].getOffsetMm(),
			df);
	}

	/**
	 * Test file "BeatOffsetsStrategyTest-4.xml".
	 */
	@Test public void computeBeatOffsets_File4() {
		Score score = MusicXmlScoreFileInputTest.loadXMLTestScore("VoicesBeatOffsetterTest-4.xml");

		LinkedList<VoiceSpacing> voiceSpacings = createVoiceSpacings(score);
		BeatOffset[] beatOffsets = testee.compute(voiceSpacings,
			Companion.fr(3, 4), minimalBeatsOffsetIs).toArray(new BeatOffset[0]);

		//distance between beat 1/4 and 2/4 must be width_1_4
		float is = score.getFormat().getInterlineSpace();
		assertEquals(width_1_4 * is, beatOffsets[3].getOffsetMm() - beatOffsets[2].getOffsetMm(), df);
	}

	private Score createTestScore1Voice() {
		Score score = new Score();
		score.getFormat().setInterlineSpace(1);
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new Clef(ClefType.Companion.getClefTreble()));
		cursor.write((MeasureElement) new TraditionalKey(-3));
		cursor.write(new TimeSignature(TimeType.timeType(6, 4)));
		//beats: 0, 2, 3, 4, 8.
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 2)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 2)));
		return score;
	}

	private List<ElementSpacing> createTestElementSpacings1Voice() {
		return alist(
			spacing(Companion.fr(0, 4), 1), //clef. width: 3
			spacing(Companion.fr(0, 4), 4), //key. width: 2
			spacing(Companion.fr(0, 4), 6), //time. width: 3
			spacing(Companion.fr(0, 4), 9), //note. width: 2
			spacing(Companion.fr(2, 4), 11), //note. width: 2
			spacing(Companion.fr(3, 4), 13), //note. width: 2
			spacing(Companion.fr(4, 4), 15), //note. width: 2
			spacing(Companion.fr(8, 4), 17) //note. width: 2
		);
	}

	private Score createTestScore3Voices() {
		Score score = new Score();
		score.getFormat().setInterlineSpace(10);
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new TimeSignature(time_4_4));

		//2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet
		//voice 1: | 4     4     4     4     |   (staff 1)
		//voice 2: | 8  8  8  8  2           |   (staff 1)
		//voice 3: | 3   3   3   8  8  4     |   (staff 2)

		//voice 1 (staff 1)
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));

		//voice 2 (staff 1)
		cursor.setMp(atElement(0, 0, 1, 0));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 2)));

		//voice 3 (staff 2)
		cursor.setMp(atElement(1, 0, 0, 0));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));

		return score;
	}

	private Score createTestScore3VoicesGrace() {
		Score score = new Score();
		score.getFormat().setInterlineSpace(10);
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new TimeSignature(time_4_4));

		//2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet, .: grace note
		//voice 1: | 4     ...4       4     4     |   (staff 1)
		//voice 2: | 8  8     8  8  ..2           |   (staff 1)
		//voice 3: | 3   3      3    .8  8  4     |   (staff 2)

		//voice 1 (staff 1)
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(graceChord(Companion.pi(0, 0, 0)));
		cursor.write(graceChord(Companion.pi(0, 0, 0)));
		cursor.write(graceChord(Companion.pi(0, 0, 0)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));

		//voice 2 (staff 1)
		cursor.setMp(atElement(0, 0, 1, 0));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(graceChord(Companion.pi(0, 0, 0)));
		cursor.write(graceChord(Companion.pi(0, 0, 0)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 2)));

		//voice 3 (staff 2)
		cursor.setMp(atElement(1, 0, 0, 0));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(graceChord(Companion.pi(0, 0, 0)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));

		return score;
	}

	private Score createTestScore3VoicesIncomplete() {
		Score score = new Score();
		score.getFormat().setInterlineSpace(2);
		Cursor cursor = new Cursor(score, mp0, true);
		cursor.write(new TimeSignature(time_4_4));

		//2: half note, 4: quarter note, 8: eighth note, 3: quarter triplet, x: missing (empty)
		//voice 1: | 4     4     4     8  xxx|   (staff 1)
		//voice 2: | 8  8  8  8  4     xxxxxx|   (staff 1)
		//voice 3: | 3   3   3   8  xxxxxxxxx|   (staff 2)

		//voice 1
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));

		//voice 2
		cursor.setMp(atElement(0, 0, 1, 0));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 4)));

		//voice 3
		cursor.setMp(atElement(0, 0, 2, 0));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 6)));
		cursor.write(chord(Companion.pi(0, 0, 0), Companion.fr(1, 8)));

		return score;
	}

	/**
	 * Create {@link VoiceSpacing}s for the first measure column
	 * of the given {@link Score}.
	 */
	private LinkedList<VoiceSpacing> createVoiceSpacings(Score score) {
		LinkedList<VoiceSpacing> ret = new LinkedList<>();
		for (int iStaff : range(0, score.getStavesCount() - 1)) {
			Measure measure = score.getMeasure(atMeasure(iStaff, 0));
			for (Voice voice : measure.getVoices()) {
				Fraction beat = Companion.fr(0);
				ArrayList<ElementSpacing> se = alist();
				float offset = 0;
				for (VoiceElement e : voice.getElements()) {
					//compute width
					float width = 0;
					if (e.getDuration().equals(Companion.get_0()))
						width = width_grace;
					else if (e.getDuration().equals(dur_1_8))
						width = width_1_8;
					else if (e.getDuration().equals(dur_1_6))
						width = width_1_6;
					else if (e.getDuration().equals(dur_1_4))
						width = width_1_4;
					else if (e.getDuration().equals(dur_3_8))
						width = width_3_8;
					else if (e.getDuration().equals(dur_1_2))
						width = width_1_2;
					else if (e.getDuration().equals(dur_1_1))
						width = width_1_1;
					//create spacing element with offset
					se.add(new ChordSpacing(new ChordNotation((Chord) e), beat, offset));
					beat = beat.add(e.getDuration());
					offset += width;
				}
				se.add(new BorderSpacing(beat, offset));
				ret.add(new VoiceSpacing(voice, score.getFormat().getInterlineSpace(), se));
			}
		}
		return ret;
	}
	
}
