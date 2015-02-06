package com.xenoage.zong.musiclayout.spacer.beat;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineMiddleBothRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineRegular;
import static com.xenoage.zong.core.music.barline.BarlineStyle.HeavyLight;
import static com.xenoage.zong.core.music.barline.BarlineStyle.LightHeavy;
import static com.xenoage.zong.core.music.barline.BarlineStyle.LightLight;
import static com.xenoage.zong.musiclayout.spacer.beat.BarlinesBeatOffsetter.barlinesBeatOffsetter;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.musiclayout.spacing.BeatOffset;

/**
 * Tests for {@link BarlinesBeatOffsetter}.
 * 
 * @author Andreas Wenger
 */
public class BarlinesBeatOffsetterTest {
	
	private BarlinesBeatOffsetter testee = barlinesBeatOffsetter;

	
	@Test public void computeBeatOffsetsTest() {
		//notes:      |    1/4    1/4   ||   1/4    |   1/4    |
		//barlines:   |:               :||:         |         :|
		float d = 2; //original distance between notes
		float is = 1.5f; //interline space
		//create original offsets
		List<BeatOffset> baseOffsets = alist(
			new BeatOffset(fr(0, 4), 0 * d), new BeatOffset(fr(1, 4), 1 * d),
			new BeatOffset(fr(2, 4), 2 * d), new BeatOffset(fr(3, 4), 3 * d),
			new BeatOffset(fr(4, 4), 4 * d));
		//create barlines
		ColumnHeader ch = new ColumnHeader(null, 0);
		ch.setStartBarline(barlineForwardRepeat(HeavyLight));
		ch.setMiddleBarline(barlineMiddleBothRepeat(LightLight, 1), fr(2, 4));
		ch.setMiddleBarline(barlineRegular(), fr(3, 4));
		ch.setEndBarline(barlineBackwardRepeat(LightHeavy, 1));
		//compute new offsets and check results
		BarlinesBeatOffsetter.Result result = testee.compute(baseOffsets, ch, is);
		float dRep = BarlinesBeatOffsetter.repeatSpace * is;
		float dMid = BarlinesBeatOffsetter.midBarlineSpace * is;
		//note offsets
		List<BeatOffset> vo = result.voiceElementOffsets;
		assertEquals(5, vo.size());
		assertEquals(new BeatOffset(fr(0, 4), 0 * d + 1 * dRep), vo.get(0));
		assertEquals(new BeatOffset(fr(1, 4), 1 * d + 1 * dRep), vo.get(1));
		assertEquals(new BeatOffset(fr(2, 4), 2 * d + 3 * dRep + 1 * dMid), vo.get(2));
		assertEquals(new BeatOffset(fr(3, 4), 3 * d + 3 * dRep + 2 * dMid), vo.get(3));
		assertEquals(new BeatOffset(fr(4, 4), 4 * d + 3 * dRep + 2 * dMid), vo.get(4));
		//barline offsets
		List<BeatOffset> bo = result.barlineOffsets;
		assertEquals(4, bo.size());
		assertEquals(new BeatOffset(fr(0, 4), 0 * d + 0 * dRep), bo.get(0));
		assertEquals(new BeatOffset(fr(2, 4), 2 * d + 2 * dRep), bo.get(1));
		assertEquals(new BeatOffset(fr(3, 4), 3 * d + 3 * dRep + 1 * dMid), bo.get(2));
		assertEquals(new BeatOffset(fr(4, 4), 4 * d + 4 * dRep + 2 * dMid), bo.get(3));
	}

}
