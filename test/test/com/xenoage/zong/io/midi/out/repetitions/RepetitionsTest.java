package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.zong.io.midi.out.repetitions.Repetitions.PlayRange;
import org.junit.Test;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atBeat;
import static org.junit.Assert.*;

/**
 * Tests for {@link Repetitions}.
 *
 * @author Andreas Wenger
 */
public class RepetitionsTest {

	@Test public void testMerge() {
		Repetitions reps = new Repetitions(
				alist(range(0, 4), range(3, 5), range(5, 7), range(3, 4), range(4, 8), range(8, 9)));
		Repetitions merged = new Repetitions(
				alist(range(0, 4), range(3, 7), range(3, 9)));
		assertEquals(merged, reps);
	}

	public static PlayRange range(int startMeasure, int endMeasure) {
		return new PlayRange(atBeat(0, startMeasure, 0, _0), atBeat(0, endMeasure, 0, _0));
	}

}
