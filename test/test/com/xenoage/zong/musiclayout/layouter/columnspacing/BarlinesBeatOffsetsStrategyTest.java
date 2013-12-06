package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineMiddleBothRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineRegular;
import static com.xenoage.zong.core.music.barline.BarlineStyle.HeavyLight;
import static com.xenoage.zong.core.music.barline.BarlineStyle.LightHeavy;
import static com.xenoage.zong.core.music.barline.BarlineStyle.LightLight;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import com.xenoage.utils.collections.IList;
import com.xenoage.utils.kernel.Tuple2;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.musiclayout.BeatOffset;

/**
 * Tests for {@link BarlinesBeatOffsetsStrategy}.
 * 
 * @author Andreas Wenger
 */
public class BarlinesBeatOffsetsStrategyTest {

	@Test public void computeBeatOffsetsTest() {
		//notes:      |    1/4    1/4   ||   1/4    |   1/4    |
		//barlines:   |:               :||:         |         :|
		float d = 2; //original distance between notes
		float is = 1.5f; //interline space
		//create original offsets
		ArrayList<BeatOffset> baseOffsets = alist();
		baseOffsets.addAll(Arrays.asList(new BeatOffset(fr(0, 4), 0 * d),
			new BeatOffset(fr(1, 4), 1 * d), new BeatOffset(fr(2, 4), 2 * d), new BeatOffset(fr(3, 4),
				3 * d), new BeatOffset(fr(4, 4), 4 * d)));
		//create barlines
		ColumnHeader ch = new ColumnHeader(null, 0);
		ch.setStartBarline(barlineForwardRepeat(HeavyLight));
		ch.setMiddleBarline(barlineMiddleBothRepeat(LightLight, 1), fr(2, 4));
		ch.setMiddleBarline(barlineRegular(), fr(3, 4));
		ch.setEndBarline(barlineBackwardRepeat(LightHeavy, 1));
		//compute new offsets and check results
		Tuple2<IList<BeatOffset>, IList<BeatOffset>> result = new BarlinesBeatOffsetsStrategy()
			.computeBeatOffsets(baseOffsets, ch, is);
		float dRep = BarlinesBeatOffsetsStrategy.REPEAT_SPACE * is;
		float dMid = BarlinesBeatOffsetsStrategy.MID_BARLINE_SPACE * is;
		//note offsets
		assertEquals(5, result.get1().size());
		assertEquals(new BeatOffset(fr(0, 4), 0 * d + 1 * dRep), result.get1().get(0));
		assertEquals(new BeatOffset(fr(1, 4), 1 * d + 1 * dRep), result.get1().get(1));
		assertEquals(new BeatOffset(fr(2, 4), 2 * d + 3 * dRep + 1 * dMid), result.get1().get(2));
		assertEquals(new BeatOffset(fr(3, 4), 3 * d + 3 * dRep + 2 * dMid), result.get1().get(3));
		assertEquals(new BeatOffset(fr(4, 4), 4 * d + 3 * dRep + 2 * dMid), result.get1().get(4));
		//barline offsets
		assertEquals(4, result.get2().size());
		assertEquals(new BeatOffset(fr(0, 4), 0 * d + 0 * dRep), result.get2().get(0));
		assertEquals(new BeatOffset(fr(2, 4), 2 * d + 2 * dRep), result.get2().get(1));
		assertEquals(new BeatOffset(fr(3, 4), 3 * d + 3 * dRep + 1 * dMid), result.get2().get(2));
		assertEquals(new BeatOffset(fr(4, 4), 4 * d + 4 * dRep + 2 * dMid), result.get2().get(3));
	}

}
