package com.xenoage.zong.io.midi.out.repetitions;

import org.junit.Test;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.BP.bp;
import static org.junit.Assert.*;

/**
 * Tests for {@link Repetitions}.
 *
 * @author Andreas Wenger
 */
public class RepetitionsTest {

	@Test public void mergeRangesTest() {
		//test two connected ranges
		List<PlayRange> merged = Repetitions.mergeRanges(alist(playRange(0, 4), playRange(4, 8)));
		List<PlayRange> expected = alist(playRange(0, 8));
		assertEquals(expected, merged);
		//test two unconnected ranges
		merged = Repetitions.mergeRanges(alist(playRange(0, 4), playRange(5, 8)));
		expected = alist(playRange(0, 4), playRange(5, 8));
		assertEquals(expected, merged);
		//test longer score
		merged = Repetitions.mergeRanges(
				alist(playRange(0, 4), playRange(3, 5), playRange(5, 7), playRange(3, 4), playRange(4, 8), playRange(8, 9),
						playRange(12, 15), playRange(15, 16)));
		expected = alist(playRange(0, 4), playRange(3, 7), playRange(3, 9), playRange(12, 16));
		assertEquals(expected, merged);
	}

	public static PlayRange playRange(int startMeasure, int endMeasure) {
		return new PlayRange(bp(startMeasure, _0), bp(endMeasure, _0));
	}

}
