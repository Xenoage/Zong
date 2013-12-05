package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.math.Fraction.fr;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;

import com.xenoage.utils.math.Delta;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.position.MP;

/**
 * Test cases for a {@link StaffMarks} class.
 *
 * @author Andreas Wenger
 */
public class StaffMarksTest {

	StaffMarks sm = new StaffMarks(0, 0, 0, ilist(
		new MeasureMarks(0, 0, 0, 25, ilist(new BeatOffset(fr(0, 4), 5), new BeatOffset(fr(1, 4), 20))),
		new MeasureMarks(1, 35, 35, 55,
			ilist(new BeatOffset(fr(0, 4), 40), new BeatOffset(fr(1, 4), 50))), new MeasureMarks(2, 55,
			55, 75, ilist(new BeatOffset(fr(0, 4), 60), new BeatOffset(fr(3, 4), 70)))));


	/**
	 * Tests the getScorePositionAt method.
	 */
	@Test public void getScorePositionAt() {
		MP mp;
		MeasureMarks[] mm = sm.getMeasureMarks().toArray(new MeasureMarks[0]);
		MeasureMarks lastMm = mm[mm.length - 1];
		//coordinate before first measure must return null
		assertNull(sm.getMPAt(mm[0].getStartMm() - 1));
		//coordinate before first beat in measure 0 must return first beat
		mp = sm.getMPAt((mm[0].getStartMm() + mm[0].getBeatOffsets().getFirst().getOffsetMm()) / 2);
		assertEquals(0, mp.measure);
		assertEquals(mm[0].getBeatOffsets().getFirst().getBeat(), mp.beat);
		//coordinate after last measure must return null
		assertNull(sm.getMPAt(lastMm.getEndMm() + 1));
		//coordinate after last beat in last measure must return last beat
		mp = sm.getMPAt((lastMm.getBeatOffsets().getLast().getOffsetMm() + lastMm.getStartMm()) / 2);
		assertEquals(mm.length - 1, mp.measure);
		assertEquals(lastMm.getBeatOffsets().getLast().getBeat(), mp.beat);
		//coordinate at i-th x-position must return i-th beat
		for (int iMeasure = 0; iMeasure < mm.length; iMeasure++) {
			List<BeatOffset> bm = mm[iMeasure].getBeatOffsets();
			for (int iBeat = 0; iBeat < bm.size(); iBeat++) {
				mp = sm.getMPAt(bm.get(iBeat).getOffsetMm());
				assertEquals(iMeasure, mp.measure);
				assertEquals(bm.get(iBeat).getBeat(), mp.beat);
			}
		}
		//coordinate between beat 0 and 3 in measure 2 must return beat 3
		mp = sm.getMPAt((mm[2].getBeatOffsets().get(0).getOffsetMm() + mm[2].getBeatOffsets().get(1)
			.getOffsetMm()) / 2);
		assertEquals(2, mp.measure);
		assertEquals(mm[2].getBeatOffsets().get(1).getBeat(), mp.beat);
	}

	/**
	 * Tests the getXMmAt method.
	 */
	@Test public void getXMmAt() {
		MeasureMarks[] mm = sm.getMeasureMarks().toArray(new MeasureMarks[0]);
		MeasureMarks lastMm = mm[mm.length - 1];
		//beat before first measure and after last measure must return null
		assertNull(sm.getXMmAt(0 - 1, Fraction._0));
		assertNull(sm.getXMmAt(mm.length, Fraction._0));
		//beat before first beat in measure 0 must return first beat
		assertEquals(mm[0].getBeatOffsets().getFirst().getOffsetMm(),
			sm.getXMmAt(0, mm[0].getBeatOffsets().getFirst().getBeat().sub(fr(1, 4))), Delta.DELTA_FLOAT);
		//beat after last beat in last measure must return last beat
		assertEquals(lastMm.getBeatOffsets().getLast().getOffsetMm(),
			sm.getXMmAt(mm.length - 1, lastMm.getBeatOffsets().getLast().getBeat().add(fr(1, 4))),
			Delta.DELTA_FLOAT);
		//i-th beat must return coordinate at i-th x-position
		for (int iMeasure = 0; iMeasure < mm.length; iMeasure++) {
			List<BeatOffset> bm = mm[iMeasure].getBeatOffsets();
			for (int iBeat = 0; iBeat < bm.size(); iBeat++) {
				assertEquals(bm.get(iBeat).getOffsetMm(), sm.getXMmAt(iMeasure, bm.get(iBeat).getBeat()),
					Delta.DELTA_FLOAT);
			}
		}
	}

}
