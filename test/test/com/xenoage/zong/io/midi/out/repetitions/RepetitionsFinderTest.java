package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.collections.CList;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.io.midi.out.repetitions.Repetitions.PlayRange;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.zong.io.midi.out.repetitions.RepetitionsTest.range;
import static org.junit.Assert.*;

/**
 * Tests for {@link RepetitionsFinder}.
 *
 * @author Andreas Wenger
 */
public class RepetitionsFinderTest {

	@Test public void findRepetitionsTest() {
		val reps = RepetitionsFinder.createPlaylist(getTestScore());
		assertEquals(getExpectedRepetitions(), reps);
	}

	/**
	 * Creates a simple test score with the following repetitions:
	 *
	 * voltas:                                                           1__ 2_______ 3_______________
	 * measures:  |   |(segno)    |:  |   |   :|   (tocoda)|    |:      |  :|   |   :|   |  (dalsegno)|(coda) |   ||
	 * numbers:   |0  |1          |2  |3  |4   |5          |6   |7      |8  |9  |10  |11 |12          |13     |14 ||
	 */
	private Score getTestScore() {
		Score score = ScoreFactory.create1Staff();
		return score;
	}

	/**
	 * See {@link #getTestScore()}.
	 */
	private Repetitions getExpectedRepetitions() {
		CList<PlayRange> ranges = clist();
		ranges.add(range(0, 2));
		ranges.add(range(2, 5));
		ranges.add(range(2, 5));
		ranges.add(range(5, 7));
		ranges.add(range(7, 9));
		ranges.add(range(7, 8));
		ranges.add(range(9, 10));
		ranges.add(range(7, 8));
		ranges.add(range(11, 13));
		ranges.add(range(1, 2));
		ranges.add(range(2, 5));
		ranges.add(range(2, 5));
		ranges.add(range(5, 6));
		ranges.add(range(13, 15));
		return new Repetitions(ranges.close());
	}



}
