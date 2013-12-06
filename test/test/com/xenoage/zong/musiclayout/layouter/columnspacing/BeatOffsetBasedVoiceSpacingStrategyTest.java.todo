package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.utils.pdlib.PVector.pvec;
import static com.xenoage.zong.core.position.BMP.bmp0;
import static com.xenoage.zong.io.score.ScoreController.getMeasureBeats;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.xenoage.utils.io.TestIO;
import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.pdlib.PVector;
import com.xenoage.utils.pdlib.Vector;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Voice;
import com.xenoage.zong.io.musicxml.in.MusicXMLScoreFileInputTest;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterTest;
import com.xenoage.zong.musiclayout.layouter.cache.NotationsCache;
import com.xenoage.zong.musiclayout.layouter.notation.NotationStrategy;
import com.xenoage.zong.musiclayout.settings.LayoutSettings;
import com.xenoage.zong.musiclayout.spacing.horizontal.SpacingElement;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Test cases for the {@link BeatOffsetBasedVoiceSpacingStrategy}.
 * 
 * TODO:Task #36: Update test because clefs/keys are not handled any more in voices.
 *
 * @author Andreas Wenger
 */
public class BeatOffsetBasedVoiceSpacingStrategyTest {

	private LayoutSettings layoutSettings;


	@Before public void setUp() {
		TestIO.initWithSharedDir();
		layoutSettings = LayoutSettings.loadDefault();
	}

	@Test public void testComputeSharedBeats() {
		BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
		//list 1 beats: 0  3   789
		//list 2 beats: 0    5 7 9
		//shared beats: 0      7 9
		PVector<SpacingElement> list1 = pvec(new SpacingElement(null, beat(0), 0f), new SpacingElement(
			null, beat(3), 0f), new SpacingElement(null, beat(7), 0f), new SpacingElement(null, beat(8),
			0f), new SpacingElement(null, beat(9), 0f));
		PVector<BeatOffset> list2 = pvec(new BeatOffset(beat(0), 0f), new BeatOffset(beat(5), 0f),
			new BeatOffset(beat(7), 0f), new BeatOffset(beat(9), 0f));
		List<BeatOffset> res = strategy.computeSharedBeats(list1, list2);
		assertEquals(3, res.size());
		assertEquals(beat(0), res.get(0).getBeat());
		assertEquals(beat(7), res.get(1).getBeat());
		assertEquals(beat(9), res.get(2).getBeat());
		//list 1 beats: 01 3
		//list 2 beats:   2 4  
		//shared beats: (none)
		list1 = pvec(new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(1), 0f),
			new SpacingElement(null, beat(3), 0f));
		list2 = pvec(new BeatOffset(beat(2), 0f), new BeatOffset(beat(4), 0f));
		res = strategy.computeSharedBeats(list1, list2);
		assertEquals(0, res.size());
		//list 1 beats: 000033
		//list 2 beats: 0123
		//shared beats: 0 and 3 (no duplicate values!)
		list1 = pvec(new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(0), 0f),
			new SpacingElement(null, beat(0), 0f), new SpacingElement(null, beat(0), 0f),
			new SpacingElement(null, beat(3), 0f), new SpacingElement(null, beat(3), 0f));
		list2 = pvec(new BeatOffset(beat(0), 0f), new BeatOffset(beat(1), 0f), new BeatOffset(beat(2),
			0f), new BeatOffset(beat(3), 0f));
		res = strategy.computeSharedBeats(list1, list2);
		assertEquals(2, res.size());
		assertEquals(beat(0), res.get(0).getBeat());
		assertEquals(beat(3), res.get(1).getBeat());
	}

	@Test public void computeVoiceSpacingTest1() {
		float is = 2;
		//voice spacing:
		//beats:   ..2.4..78
		//offsets:   | |  || 
		//           | |  |⌎- 6
		//           | |  ⌎-- 4
		//           | ⌎----- 2
		//           ⌎------- 1
		VoiceSpacing voiceSpacing = new VoiceSpacing(Voice.empty, is, pvec(new SpacingElement(null,
			beat(2), 1f), new SpacingElement(null, beat(4), 2f), new SpacingElement(null, beat(7), 4f),
			new SpacingElement(null, beat(8), 6f)));
		//given beat offsets:
		//beats:   0...4...8
		//offsets: |   |   | 
		//         |   |   ⌎- 20
		//         |   ⌎----- 8
		//         ⌎--------- 0
		PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 0f),
			new BeatOffset(beat(4), 8f), new BeatOffset(beat(8), 20f));
		//shared beats: 4, 8.
		//
		//resulting spacing:
		//beats:   ..2.4..78
		//offsets:   | |  || 
		//           | |  |⌎- (20 - 8) / (6 - 2) * (6 - 2) + 8 = 20 } (shared beats 4 and 8)
		//           | |  ⌎-- (20 - 8) / (6 - 2) * (4 - 2) + 8 = 14 } (shared beats 4 and 8)
		//           | ⌎-----  (8 - 0) / (2 - 0) * (2 - 0) + 0 =  8 } (shared beats 0 and 4)
		//           ⌎-------  (8 - 0) / (2 - 0) * (1 - 0) + 0 =  4 } (shared beats 0 and 4)
		BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
		Vector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets)
			.getSpacingElements();
		assertEquals(4, finalSpacing.size());
		assertEquals(beat(2), finalSpacing.get(0).beat);
		assertEquals(4f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT_ROUGH);
		assertEquals(beat(4), finalSpacing.get(1).beat);
		assertEquals(8f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT_ROUGH);
		assertEquals(beat(7), finalSpacing.get(2).beat);
		assertEquals(14f / is, finalSpacing.get(2).offset, Delta.DELTA_FLOAT_ROUGH);
		assertEquals(beat(8), finalSpacing.get(3).beat);
		assertEquals(20f / is, finalSpacing.get(3).offset, Delta.DELTA_FLOAT_ROUGH);
	}

	@Test public void computeVoiceSpacingTest2() {
		float is = 3;
		//voice spacing:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 2
		//         ⌎--- 0
		VoiceSpacing voiceSpacing = new VoiceSpacing(Voice.empty, is, pvec(new SpacingElement(null,
			beat(0), 0f), new SpacingElement(null, beat(2), 2f)));
		//given beat offsets:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 2
		//         ⌎--- 0
		PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 0f), new BeatOffset(beat(2), 2f));
		//shared beats: 0, 2.
		//
		//resulting spacing:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 2
		//         ⌎--- 0
		BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
		Vector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets)
			.getSpacingElements();
		assertEquals(2, finalSpacing.size());
		assertEquals(beat(0), finalSpacing.get(0).beat);
		assertEquals(0f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT);
		assertEquals(beat(2), finalSpacing.get(1).beat);
		assertEquals(2f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT);
	}

	@Test public void computeVoiceSpacingTest3() {
		float is = 4;
		//voice spacing:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 2
		//         ⌎--- 0
		VoiceSpacing voiceSpacing = new VoiceSpacing(Voice.empty, is, pvec(new SpacingElement(null,
			beat(0), 0f), new SpacingElement(null, beat(2), 2f)));
		//given beat offsets:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 6
		//         ⌎--- 2
		PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 2f), new BeatOffset(beat(2), 6f));
		//shared beats: 0, 2.
		//
		//resulting spacing:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 6
		//         ⌎--- 2
		BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
		Vector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets)
			.getSpacingElements();
		assertEquals(2, finalSpacing.size());
		assertEquals(beat(0), finalSpacing.get(0).beat);
		assertEquals(2f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT);
		assertEquals(beat(2), finalSpacing.get(1).beat);
		assertEquals(6f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT);
	}

	/**
	 * Tests the strategy with a voice that uses grace notes.
	 * The distance of grace notes to their main notes should not be stretched,
	 * but should stay the same.
	 */
	@Test public void computeVoiceSpacingTestGrace() {
		float is = 2;
		//voice spacing:
		//beats:   0.2...gg8
		//offsets: | |   ||| 
		//         | |   ||⌎- 60
		//         | |   |⌎-- 59 (grace note)
		//         | |   ⌎--- 58 (grace note)
		//         | ⌎------- 51
		//         ⌎--------- 50
		VoiceSpacing voiceSpacing = new VoiceSpacing(Voice.empty, is, pvec(new SpacingElement(null,
			beat(0), 0f), new SpacingElement(null, beat(2), 1f), new SpacingElement(null, beat(8), 8f,
			true), new SpacingElement(null, beat(8), 9f, true), new SpacingElement(null, beat(8), 10f)));
		//given beat offsets:
		//beats:   0.......8
		//offsets: |       | 
		//         |       ⌎- 30
		//         ⌎--------- 10
		PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 10f), new BeatOffset(beat(8),
			30f));
		//shared beats: 4, 8.
		//
		//resulting spacing:
		//beats:   0.2...gg8
		//offsets: | |   ||| 
		//         | |   ||⌎- (30 - 10) / (60 - 50) * (60 - 50) + 10 = 30 = x } (shared beats 0 and 8)
		//         | |   |⌎-- x - (10 - 9) = 30 - (1 * IS) = 28 (because it is a grace note in front of x)
		//         | |   ⌎--- x - (10 - 8) = 30 - (2 * IS) = 26 (because it is a grace note in front of x)
		//         | ⌎------- (30 - 10) / (60 - 50) * (51 - 50) + 10 = 12 } (shared beats 0 and 8)
		//         ⌎--------- (30 - 10) / (60 - 50) * (50 - 50) + 10 = 10 } (shared beats 0 and 8)
		BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
		Vector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets)
			.getSpacingElements();
		assertEquals(5, finalSpacing.size());
		assertEquals(beat(0), finalSpacing.get(0).beat);
		assertEquals(10f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT_ROUGH);
		assertEquals(beat(2), finalSpacing.get(1).beat);
		assertEquals(12f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT_ROUGH);
		assertEquals(beat(8), finalSpacing.get(2).beat);
		assertEquals(26f / is, finalSpacing.get(2).offset, Delta.DELTA_FLOAT_ROUGH);
		assertEquals(beat(8), finalSpacing.get(3).beat);
		assertEquals(28f / is, finalSpacing.get(3).offset, Delta.DELTA_FLOAT_ROUGH);
		assertEquals(beat(8), finalSpacing.get(4).beat);
		assertEquals(30f / is, finalSpacing.get(4).offset, Delta.DELTA_FLOAT_ROUGH);
	}

	/**
	 * Test file "BeatOffsetBasedVoiceSpacingStrategyTest-1.xml".
	 */
	@Test public void computeVoiceSpacing_File1() {
		Score score = MusicXMLScoreFileInputTest
			.loadXMLTestScore("BeatOffsetBasedVoiceSpacingStrategyTest-1.xml");

		//use 2 mm interline space
		float is = 2;
		score = score.withFormat(score.format.withInterlineSpace(is));

		//compute voice spacing for voice 0
		NotationStrategy notationStrategy = ScoreLayouterTest.getNotationStrategy();
		NotationsCache notations = notationStrategy.computeNotations(score, null, layoutSettings);
		VoiceSpacing voiceSpacing = new SeparateVoiceSpacingStrategy().computeVoiceSpacing(
			score.getVoice(bmp0), is, notations, getMeasureBeats(score, 0), layoutSettings);

		//use the following beat offsets
		PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 5f),
			new BeatOffset(beat(1), 10f), new BeatOffset(beat(2), 15f));

		//create voice spacing based on beat offsets
		BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
		Vector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets)
			.getSpacingElements();

		//check for right offsets
		assertEquals(4, finalSpacing.size());
		assertEquals(beat(0), finalSpacing.get(0).beat);
		assertEquals(5f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT);
		assertEquals(beat(1), finalSpacing.get(1).beat);
		assertEquals(10f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT);
		assertEquals(beat(2), finalSpacing.get(2).beat);
		assertEquals(15f / is, finalSpacing.get(2).offset, Delta.DELTA_FLOAT);
	}

	/**
	 * Test file "BeatOffsetBasedVoiceSpacingStrategyTest-2.xml".
	 */
	@Test public void computeVoiceSpacing_File2() {
		Score score = MusicXMLScoreFileInputTest
			.loadXMLTestScore("BeatOffsetBasedVoiceSpacingStrategyTest-2.xml");

		//use 3 mm interline space
		float is = 3;
		score = score.withFormat(score.format.withInterlineSpace(is));

		//compute voice spacing for voices 0 and 1
		NotationStrategy notationStrategy = ScoreLayouterTest.getNotationStrategy();
		NotationsCache notations = notationStrategy.computeNotations(score, null, layoutSettings);
		for (int voice = 0; voice <= 1; voice++) {
			VoiceSpacing voiceSpacing = new SeparateVoiceSpacingStrategy().computeVoiceSpacing(
				score.getVoice(bmp0), is, notations, getMeasureBeats(score, 0), layoutSettings);

			//use the following beat offsets
			PVector<BeatOffset> beatOffsets = pvec(new BeatOffset(beat(0), 5f), new BeatOffset(beat(1),
				10f), new BeatOffset(beat(2), 15f));

			//create voice spacing based on beat offsets
			BeatOffsetBasedVoiceSpacingStrategy strategy = new BeatOffsetBasedVoiceSpacingStrategy();
			Vector<SpacingElement> finalSpacing = strategy.computeVoiceSpacing(voiceSpacing, beatOffsets)
				.getSpacingElements();

			//check for right offsets
			assertEquals(4, finalSpacing.size());
			assertEquals(beat(0), finalSpacing.get(0).beat);
			assertEquals(5f / is, finalSpacing.get(0).offset, Delta.DELTA_FLOAT);
			assertEquals(beat(1), finalSpacing.get(1).beat);
			assertEquals(10f / is, finalSpacing.get(1).offset, Delta.DELTA_FLOAT);
			assertEquals(beat(2), finalSpacing.get(2).beat);
			assertEquals(15f / is, finalSpacing.get(2).offset, Delta.DELTA_FLOAT);
		}
	}

	private Fraction beat(int quarters) {
		return fr(quarters, 4);
	}

}
