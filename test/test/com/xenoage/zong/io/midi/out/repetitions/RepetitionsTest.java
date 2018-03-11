package com.xenoage.zong.io.midi.out.repetitions;

import org.junit.Test;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.Time.time;
import static com.xenoage.zong.io.midi.out.repetitions.Repetitions.mergeRepetitions;
import static org.junit.Assert.*;

/**
 * Tests for {@link Repetitions}.
 *
 * @author Andreas Wenger
 */
public class RepetitionsTest {

	@Test public void mergeRepetitionsTest() {
		//test two connected ranges
		List<Repetition> merged = mergeRepetitions(alist(repetition(0, 4), repetition(4, 8)));
		List<Repetition> expected = alist(repetition(0, 8));
		assertEquals(expected, merged);
		//test two unconnected ranges
		merged = mergeRepetitions(alist(repetition(0, 4), repetition(5, 8)));
		expected = alist(repetition(0, 4), repetition(5, 8));
		assertEquals(expected, merged);
		//test longer score
		merged = mergeRepetitions(
				alist(repetition(0, 4), repetition(3, 5), repetition(5, 7), repetition(3, 4), repetition(4, 8),
						repetition(8, 9), repetition(12, 15), repetition(15, 16)));
		expected = alist(repetition(0, 4), repetition(3, 7), repetition(3, 9), repetition(12, 16));
		assertEquals(expected, merged);
	}

	public static Repetition repetition(int startMeasure, int endMeasure) {
		return new Repetition(time(startMeasure, Companion.get_0()), time(endMeasure, Companion.get_0()));
	}

}
