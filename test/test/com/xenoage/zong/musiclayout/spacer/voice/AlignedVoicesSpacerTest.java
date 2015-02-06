package com.xenoage.zong.musiclayout.spacer.voice;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.Voice.voice;
import static com.xenoage.zong.musiclayout.spacer.voice.AlignedVoicesSpacer.alignedVoicesSpacer;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.musiclayout.BeatOffset;
import com.xenoage.zong.musiclayout.notations.RestNotation;
import com.xenoage.zong.musiclayout.spacing.horizontal.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.horizontal.VoiceSpacing;

/**
 * Tests for {@link AlignedVoicesSpacer}.
 *
 * @author Andreas Wenger
 */
public class AlignedVoicesSpacerTest {
	
	private AlignedVoicesSpacer testee = alignedVoicesSpacer;
	

	@Test public void testComputeSharedBeats() {
		//list 1 beats: 0  3   789
		//list 2 beats: 0    5 7 9
		//shared beats: 0      7 9
		List<ElementSpacing> list1 = alist(new ElementSpacing(null, beat(0), 0f), new ElementSpacing(
			null, beat(3), 0f), new ElementSpacing(null, beat(7), 0f), new ElementSpacing(null, beat(8),
			0f), new ElementSpacing(null, beat(9), 0f));
		List<BeatOffset> list2 = alist(new BeatOffset(beat(0), 0f), new BeatOffset(beat(5), 0f),
			new BeatOffset(beat(7), 0f), new BeatOffset(beat(9), 0f));
		List<BeatOffset> res = testee.computeSharedBeats(list1, list2);
		assertEquals(3, res.size());
		assertEquals(beat(0), res.get(0).getBeat());
		assertEquals(beat(7), res.get(1).getBeat());
		assertEquals(beat(9), res.get(2).getBeat());
		//list 1 beats: 01 3
		//list 2 beats:   2 4  
		//shared beats: (none)
		list1 = alist(new ElementSpacing(null, beat(0), 0f), new ElementSpacing(null, beat(1), 0f),
			new ElementSpacing(null, beat(3), 0f));
		list2 = alist(new BeatOffset(beat(2), 0f), new BeatOffset(beat(4), 0f));
		res = testee.computeSharedBeats(list1, list2);
		assertEquals(0, res.size());
		//list 1 beats: 000033
		//list 2 beats: 0123
		//shared beats: 0 and 3 (no duplicate values!)
		list1 = alist(new ElementSpacing(null, beat(0), 0f), new ElementSpacing(null, beat(0), 0f),
			new ElementSpacing(null, beat(0), 0f), new ElementSpacing(null, beat(0), 0f),
			new ElementSpacing(null, beat(3), 0f), new ElementSpacing(null, beat(3), 0f));
		list2 = alist(new BeatOffset(beat(0), 0f), new BeatOffset(beat(1), 0f), new BeatOffset(beat(2),
			0f), new BeatOffset(beat(3), 0f));
		res = testee.computeSharedBeats(list1, list2);
		assertEquals(2, res.size());
		assertEquals(beat(0), res.get(0).getBeat());
		assertEquals(beat(3), res.get(1).getBeat());
	}

	@Test public void computeTest1() {
		float is = 2;
		//voice spacing:
		//beats:   ..2.4..78
		//offsets:   | |  || 
		//           | |  |⌎- 6
		//           | |  ⌎-- 4
		//           | ⌎----- 2
		//           ⌎------- 1
		VoiceSpacing voiceSpacing = new VoiceSpacing(voice(), is, alist(new ElementSpacing(null,
			beat(2), 1f), new ElementSpacing(null, beat(4), 2f), new ElementSpacing(null, beat(7), 4f),
			new ElementSpacing(null, beat(8), 6f)));
		//given beat offsets:
		//beats:   0...4...8
		//offsets: |   |   | 
		//         |   |   ⌎- 20
		//         |   ⌎----- 8
		//         ⌎--------- 0
		List<BeatOffset> beatOffsets = alist(new BeatOffset(beat(0), 0f),
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
		testee.compute(voiceSpacing, beatOffsets);
		List<ElementSpacing> finalSpacing = voiceSpacing.spacingElements;
		assertEquals(4, finalSpacing.size());
		assertEquals(beat(2), finalSpacing.get(0).beat);
		assertEquals(4f / is, finalSpacing.get(0).offsetIs, df);
		assertEquals(beat(4), finalSpacing.get(1).beat);
		assertEquals(8f / is, finalSpacing.get(1).offsetIs, df);
		assertEquals(beat(7), finalSpacing.get(2).beat);
		assertEquals(14f / is, finalSpacing.get(2).offsetIs, df);
		assertEquals(beat(8), finalSpacing.get(3).beat);
		assertEquals(20f / is, finalSpacing.get(3).offsetIs, df);
	}

	@Test public void computeTest2() {
		float is = 3;
		//voice spacing:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 2
		//         ⌎--- 0
		VoiceSpacing voiceSpacing = new VoiceSpacing(voice(), is, alist(new ElementSpacing(null,
			beat(0), 0f), new ElementSpacing(null, beat(2), 2f)));
		//given beat offsets:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 2
		//         ⌎--- 0
		List<BeatOffset> beatOffsets = alist(new BeatOffset(beat(0), 0f), new BeatOffset(beat(2), 2f));
		//shared beats: 0, 2.
		//
		//resulting spacing:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 2
		//         ⌎--- 0
		testee.compute(voiceSpacing, beatOffsets);
		List<ElementSpacing> finalSpacing = voiceSpacing.spacingElements;
		assertEquals(2, finalSpacing.size());
		assertEquals(beat(0), finalSpacing.get(0).beat);
		assertEquals(0f / is, finalSpacing.get(0).offsetIs, df);
		assertEquals(beat(2), finalSpacing.get(1).beat);
		assertEquals(2f / is, finalSpacing.get(1).offsetIs, df);
	}

	@Test public void computeTest3() {
		float is = 4;
		//voice spacing:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 2
		//         ⌎--- 0
		VoiceSpacing voiceSpacing = new VoiceSpacing(voice(), is, alist(new ElementSpacing(null,
			beat(0), 0f), new ElementSpacing(null, beat(2), 2f)));
		//given beat offsets:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 6
		//         ⌎--- 2
		List<BeatOffset> beatOffsets = alist(new BeatOffset(beat(0), 2f), new BeatOffset(beat(2), 6f));
		//shared beats: 0, 2.
		//
		//resulting spacing:
		//beats:   0.2
		//offsets: | | 
		//         | ⌎- 6
		//         ⌎--- 2
		testee.compute(voiceSpacing, beatOffsets);
		List<ElementSpacing> finalSpacing = voiceSpacing.spacingElements;
		assertEquals(2, finalSpacing.size());
		assertEquals(beat(0), finalSpacing.get(0).beat);
		assertEquals(2f / is, finalSpacing.get(0).offsetIs, df);
		assertEquals(beat(2), finalSpacing.get(1).beat);
		assertEquals(6f / is, finalSpacing.get(1).offsetIs, df);
	}

	/**
	 * Tests the strategy with a voice that uses grace notes.
	 * The distance of grace notes to their main notes should not be stretched,
	 * but should stay the same.
	 */
	@Test public void computeTestGrace() {
		float is = 2;
		//voice spacing:
		//beats:   0.2...gg8
		//offsets: | |   ||| 
		//         | |   ||⌎- 60
		//         | |   |⌎-- 59 (grace note)
		//         | |   ⌎--- 58 (grace note)
		//         | ⌎------- 51
		//         ⌎--------- 50
		VoiceSpacing voiceSpacing = new VoiceSpacing(voice(), is, alist(new ElementSpacing(null,
			beat(0), 0f), new ElementSpacing(null, beat(2), 1f), graceSpacing(beat(8), 8f),
			graceSpacing(beat(8), 9f), new ElementSpacing(null, beat(8), 10f)));
		//given beat offsets:
		//beats:   0.......8
		//offsets: |       | 
		//         |       ⌎- 30
		//         ⌎--------- 10
		List<BeatOffset> beatOffsets = alist(new BeatOffset(beat(0), 10f), new BeatOffset(beat(8), 30f));
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
		testee.compute(voiceSpacing, beatOffsets);
		List<ElementSpacing> finalSpacing = voiceSpacing.spacingElements;
		assertEquals(5, finalSpacing.size());
		assertEquals(beat(0), finalSpacing.get(0).beat);
		assertEquals(10f / is, finalSpacing.get(0).offsetIs, df);
		assertEquals(beat(2), finalSpacing.get(1).beat);
		assertEquals(12f / is, finalSpacing.get(1).offsetIs, df);
		assertEquals(beat(8), finalSpacing.get(2).beat);
		assertEquals(26f / is, finalSpacing.get(2).offsetIs, df);
		assertEquals(beat(8), finalSpacing.get(3).beat);
		assertEquals(28f / is, finalSpacing.get(3).offsetIs, df);
		assertEquals(beat(8), finalSpacing.get(4).beat);
		assertEquals(30f / is, finalSpacing.get(4).offsetIs, df);
	}

	private Fraction beat(int quarters) {
		return fr(quarters, 4);
	}

	private ElementSpacing graceSpacing(Fraction beat, float offsetIs) {
		return new ElementSpacing(new RestNotation(new Rest(_0), null), beat, offsetIs);
	}
}
