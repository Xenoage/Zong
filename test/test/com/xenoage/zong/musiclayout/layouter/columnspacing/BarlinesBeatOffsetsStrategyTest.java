package com.xenoage.zong.musiclayout.layouter.columnspacing;

import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineMiddleBothRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineRegular;
import static com.xenoage.zong.core.music.barline.BarlineStyle.HeavyLight;
import static com.xenoage.zong.core.music.barline.BarlineStyle.LightHeavy;
import static com.xenoage.zong.core.music.barline.BarlineStyle.LightLight;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
		BeatOffset[] baseOffsets = {
			new BeatOffset(fr(0, 4), 0 * d), new BeatOffset(fr(1, 4), 1 * d),
			new BeatOffset(fr(2, 4), 2 * d), new BeatOffset(fr(3, 4), 3 * d),
			new BeatOffset(fr(4, 4), 4 * d) };
		//create barlines
		ColumnHeader ch = new ColumnHeader(null, 0);
		ch.setStartBarline(barlineForwardRepeat(HeavyLight));
		ch.setMiddleBarline(barlineMiddleBothRepeat(LightLight, 1), fr(2, 4));
		ch.setMiddleBarline(barlineRegular(), fr(3, 4));
		ch.setEndBarline(barlineBackwardRepeat(LightHeavy, 1));
		//compute new offsets and check results
		Tuple2<BeatOffset[], BeatOffset[]> result = new BarlinesBeatOffsetsStrategy()
			.computeBeatOffsets(baseOffsets, ch, is);
		float dRep = BarlinesBeatOffsetsStrategy.REPEAT_SPACE * is;
		float dMid = BarlinesBeatOffsetsStrategy.MID_BARLINE_SPACE * is;
		//note offsets
		assertEquals(5, result.get1().length);
		assertEquals(new BeatOffset(fr(0, 4), 0 * d + 1 * dRep), result.get1()[0]);
		assertEquals(new BeatOffset(fr(1, 4), 1 * d + 1 * dRep), result.get1()[1]);
		assertEquals(new BeatOffset(fr(2, 4), 2 * d + 3 * dRep + 1 * dMid), result.get1()[2]);
		assertEquals(new BeatOffset(fr(3, 4), 3 * d + 3 * dRep + 2 * dMid), result.get1()[3]);
		assertEquals(new BeatOffset(fr(4, 4), 4 * d + 3 * dRep + 2 * dMid), result.get1()[4]);
		//barline offsets
		assertEquals(4, result.get2().length);
		assertEquals(new BeatOffset(fr(0, 4), 0 * d + 0 * dRep), result.get2()[0]);
		assertEquals(new BeatOffset(fr(2, 4), 2 * d + 2 * dRep), result.get2()[1]);
		assertEquals(new BeatOffset(fr(3, 4), 3 * d + 3 * dRep + 1 * dMid), result.get2()[2]);
		assertEquals(new BeatOffset(fr(4, 4), 4 * d + 4 * dRep + 2 * dMid), result.get2()[3]);
	}

}
