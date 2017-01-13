package com.xenoage.zong.musiclayout.spacing;

import com.xenoage.zong.core.position.MP;
import org.junit.Test;

import java.util.List;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.*;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Delta.df;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.position.MP.unknown;
import static com.xenoage.zong.core.position.Time.time;
import static com.xenoage.zong.musiclayout.spacing.BeatOffset.bo;
import static org.junit.Assert.*;

/**
 * Tests for {@link SystemSpacing}.
 *
 * @author Andreas Wenger
 */
public class SystemSpacingTest {

	private SystemSpacing system = createSystemSpacing(
		alist(
			//measure 0 barline beats
			alist(bo(fr(0, 4), 0), bo(fr(4, 4), 25)),
			//measure 1 barline beats
			alist(bo(fr(0, 4), 0), bo(fr(4, 4), 20)),
			//measure 2 barline beats
			alist(bo(fr(0, 4), 0), bo(fr(4, 4), 20))),
		alist(
			//measure 0 internal beats
			ilist(bo(fr(0, 4), 5), bo(fr(1, 4), 20)),
			//measure 1 internal beats
			ilist(bo(fr(0, 4), 5), bo(fr(1, 4), 15)),
			//measure 2 internal beats
			ilist(bo(fr(0, 4), 5), bo(fr(3, 4), 15))));


	@Test public void getMpAtTest() {
		MP mp;
		ColumnSpacing column;
		float xMm;
		int lastMeasure = system.getEndMeasureIndex();
		//coordinate before first measure must return unknown measure
		mp = system.getMpAt(system.getMeasureStartMm(0) - 0.1f, unknown);
		assertEquals(unknown, mp.measure);
		//coordinate before first beat in measure 0 must return first beat
		column = system.getColumn(0);
		xMm = system.getMeasureStartMm(0) + getFirst(column.getBeatOffsets()).offsetMm * 0.1f;
		mp = system.getMpAt(xMm, unknown);
		assertEquals(0, mp.measure);
		assertEquals(getFirst(column.getBeatOffsets()).beat, mp.beat);
		//coordinate after last measure must return unknown measure
		mp = system.getMpAt(system.getMeasureEndMm(lastMeasure) + 0.1f, unknown);
		assertEquals(mp.measure, unknown);
		//coordinate after last beat in last measure must return last beat
		column = system.getColumn(system.getEndMeasureIndex());
		xMm = system.getMeasureStartMm(lastMeasure) + getLast(column.getBeatOffsets()).offsetMm + 0.1f;
		mp = system.getMpAt(xMm, unknown);
		assertEquals(lastMeasure, mp.measure);
		assertEquals(getLast(column.getBeatOffsets()).beat, mp.beat);
		//coordinate at i-th x-position must return i-th beat
		for (int iMeasure : range(lastMeasure + 1)) {
			List<BeatOffset> bm = system.getColumn(iMeasure).getBeatOffsets();
			for (int iBeat : range(bm)) {
				xMm = system.getMeasureStartMm(iMeasure) + bm.get(iBeat).offsetMm;
				mp = system.getMpAt(xMm, unknown);
				assertEquals(iMeasure, mp.measure);
				assertEquals(bm.get(iBeat).beat, mp.beat);
			}
		}
		//coordinate between beat 0 and 3 (but nearer to beat 0) in measure 2 must return beat 0
		column = system.getColumn(2);
		BeatOffset m2bo0 = column.getBeatOffset(fr(0, 4));
		BeatOffset m2bo3 = column.getBeatOffset(fr(3, 4));
		xMm = system.getMeasureStartMm(2) + m2bo0.offsetMm + (m2bo3.offsetMm - m2bo0.offsetMm) * 0.4f;
		mp = system.getMpAt(xMm, unknown);
		assertEquals(2, mp.measure);
		assertEquals(m2bo0.beat, mp.beat);
		//coordinate between beat 0 and 3 (but nearer to beat 3) in measure 2 must return beat 3
		xMm = system.getMeasureStartMm(2) + m2bo0.offsetMm + (m2bo3.offsetMm - m2bo0.offsetMm) * 0.6f;
		mp = system.getMpAt(xMm, unknown);
		assertEquals(2, mp.measure);
		assertEquals(m2bo3.beat, mp.beat);
	}

	@Test public void getXMmAt() {
		BeatOffset bo;
		float xMm;
		float expectedXMm;
		int lastMeasure = system.getEndMeasureIndex();
		//beat before first beat in measure 0 must return first beat
		bo = getFirst(system.getColumn(0).beatOffsets);
		xMm = system.getXMmAt(time(0, bo.beat.sub(fr(1, 4))));
		expectedXMm = system.getMeasureStartMm(0) + bo.offsetMm;
		assertEquals(expectedXMm, xMm, df);
		//beat after last beat in last measure must return last beat
		bo = getLast(system.getColumn(lastMeasure).beatOffsets);
		xMm = system.getXMmAt(time(lastMeasure, bo.beat.add(fr(1, 4))));
		expectedXMm = system.getMeasureStartMm(lastMeasure) + bo.offsetMm;
		assertEquals(expectedXMm, xMm, df);
		//i-th beat must return coordinate at i-th x-position
		for (int iMeasure : range(lastMeasure + 1)) {
			for (BeatOffset bo2 : system.getColumn(iMeasure).beatOffsets) {
				xMm = system.getXMmAt(time(iMeasure, bo2.beat));
				expectedXMm = system.getMeasureStartMm(iMeasure) + bo2.offsetMm;
				assertEquals(expectedXMm, xMm, df);
			}
		}
	}
	
	private SystemSpacing createSystemSpacing(List<List<BeatOffset>> barlineBeats,
		List<List<BeatOffset>> columnsBeats) {
		List<ColumnSpacing> columns = alist();
		for (int iMeasure : range(columnsBeats))
			columns.add(new ColumnSpacing(iMeasure, alist(),
				columnsBeats.get(iMeasure), barlineBeats.get(iMeasure)));
		return new SystemSpacing(columns, 0, 0, 0, null, 0);
	}

}
