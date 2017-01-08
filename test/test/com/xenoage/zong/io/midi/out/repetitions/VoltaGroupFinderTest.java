package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.volta.Volta;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.collections.ArrayUtils.getFirst;
import static com.xenoage.utils.collections.ArrayUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;
import static org.junit.Assert.*;

/**
 * Tests for {@link VoltaGroupFinder}.
 *
 * @author Andreas Wenger
 */
public class VoltaGroupFinderTest {

	int[][] voltaGroups = {
			{5, 7, 10, 13}, //1st from measure 5-7, 2nd 7-10, 3rd 10-13
			{14, 15},
			{19, 21, 23}
	};

	@Test public void findAllVoltaGroupsTest() {
		Score score = createScore();
		val found = new VoltaGroupFinder(score).findAllVoltaGroups();
		assertEquals(voltaGroups.length, found.voltaGroups.size());
		for (val expectedGroups : voltaGroups) {
			val voltaGroup = found.getVoltaGroupAt(expectedGroups[0]);
			assertNotNull(voltaGroup);
			assertEquals(getLast(expectedGroups) - getFirst(expectedGroups), voltaGroup.getMeasuresCount());
			assertEquals(expectedGroups.length - 1, voltaGroup.voltasStartMeasures.size());
			for (int iVolta : range(0, expectedGroups.length - 2)) //-2: last measure is just for marking the ending
				assertEquals(expectedGroups[iVolta], voltaGroup.voltasStartMeasures.get(iVolta).startMeasure);
		}
	}

	private Score createScore() {
		Score score = ScoreFactory.create1Staff();
		new MeasureAdd(score, getLast(getLast(voltaGroups)) + 4).execute();
		for (val voltaGroup : voltaGroups) {
			for (int iVolta = 0; iVolta < voltaGroup.length - 1; iVolta++) {
				int startMeasure = voltaGroup[iVolta];
				int length = voltaGroup[iVolta + 1] - voltaGroup[iVolta];
				val numbers = range(iVolta + 1, iVolta + 1);
				score.getHeader().getColumnHeader(startMeasure).setVolta(new Volta(length, numbers, numbers.toString(), false));
			}
		}
		return score;
	}

}
