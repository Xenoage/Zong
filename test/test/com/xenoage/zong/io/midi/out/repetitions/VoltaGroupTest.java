package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.io.midi.out.repetitions.VoltaGroup.VoltaStartMeasure;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.Test;

import java.util.List;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.*;

/**
 * Tests for {@link VoltaGroup}.
 *
 * @author Andreas Wenger
 */
public class VoltaGroupTest {

	List<TestCase> tests = alist(

		// __ __
		// 1  2
		new TestCase("group_1_2", new VoltaGroup(ilist(
			volta(5, 2, range(1, 1)),
			volta(7, 2, range(2, 2)))),
			expectedVoltaIndices(0, 1),
			expectedMeasuresCount(4)),

		// __ __ __
		// 1  2  d   default must be played, too
		new TestCase("group_1_2_d", new VoltaGroup(ilist(
			volta(5, 2, range(1, 1)),
			volta(7, 2, range(2, 2)),
			defaultVolta(9, 2))),
			expectedVoltaIndices(0, 1, 2),
			expectedMeasuresCount(6)),

		// ___ __ ___
		// 1-3 4  5-6
		new TestCase("group_ranges", new VoltaGroup(ilist(
			volta(5, 2, range(1, 3)),
			volta(7, 2, range(4, 4)),
			volta(9, 2, range(5, 6)))),
			expectedVoltaIndices(0, 0, 0, 1, 2, 2),
			expectedMeasuresCount(6)),

		// __ __
		// 1  4     1st is played 3 times
		new TestCase("group_1_gap_4", new VoltaGroup(ilist(
			volta(4, 1, range(1, 1)),
			volta(5, 1, range(4, 4)))),
			expectedVoltaIndices(0, 0, 0, 1),
			expectedMeasuresCount(2)),

		// __ __ __ __ ___  __ __
		// 1  d  4  d  5-8  d  10    1st default is played 2 times, 2nd default is not played at all,
		//                        3rd default is played 1 time
		new TestCase(" group_defaults", new VoltaGroup(ilist(
			volta(2, 3, range(1, 1)),
			defaultVolta(5, 1),
			volta(6, 4, range(4, 4)),
			defaultVolta(10, 2),
			volta(12, 1, range(5, 8)),
			defaultVolta(13, 2),
			volta(15, 2, range(10, 10)))),
			expectedVoltaIndices(0, 1, 1, 2, 4, 4, 4, 4, 5, 6),
			expectedMeasuresCount(15))

	);

	@AllArgsConstructor
	class TestCase {
		String name;
		VoltaGroup voltaGroup;
		int[] expectedVoltaIndices;
		int expectedMeasuresCount;
	}


	@Test public void getRepeatCountTest() {
		for (val test : tests)
			assertEquals(test.name, test.expectedVoltaIndices.length, test.voltaGroup.getRepeatCount());
	}

	@Test public void findVoltaTest() {
		for (val test : tests) {
			for (int repeatTime : range(1, test.voltaGroup.getRepeatCount()))
				assertEquals(test.name + ", repeat " + repeatTime, test.expectedVoltaIndices[repeatTime - 1],
						test.voltaGroup.voltasStartMeasures.indexOf(test.voltaGroup.findVolta(repeatTime)));
			//invalid repeat time: null expectedVoltaIndices
			for (int repeatTime : new int[]{0, test.voltaGroup.getRepeatCount() + 1})
				assertNull(test.name + ", repeat " + repeatTime, test.voltaGroup.findVolta(repeatTime));
		}
	}

	@Test public void getMeasuresCountTest() {
		for (val test : tests)
			assertEquals(test.name, test.expectedMeasuresCount, test.voltaGroup.getMeasuresCount());
	}

	private VoltaStartMeasure volta(int startMeasure, int length, Range range) {
		return new VoltaStartMeasure(new Volta(length, range, range.toString(), false), startMeasure);
	}

	private VoltaStartMeasure defaultVolta(int startMeasure, int length) {
		return new VoltaStartMeasure(new Volta(length, null, "default", false), startMeasure);
	}

	private int[] expectedVoltaIndices(int... voltaIndices) {
		return voltaIndices;
	}

	private int expectedMeasuresCount(int measuresCount) {
		return measuresCount;
	}

}
