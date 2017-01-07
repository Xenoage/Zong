package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.direction.Coda;
import com.xenoage.zong.core.music.direction.DaCapo;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.position.MP;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.collections.CList.clist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction._1$2;
import static com.xenoage.zong.core.music.util.BeatE.beatE;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.unknown;
import static com.xenoage.zong.io.midi.out.repetitions.RepetitionsTest.playRange;
import static org.junit.Assert.*;

/**
 * Tests for {@link RepetitionsFinder}.
 *
 * @author Andreas Wenger
 */
public class RepetitionsFinderTest {

	@AllArgsConstructor
	class TestCase {
		String name;
		Score score;
		Repetitions expectedRepetitions;
	}

	TestCase[] testCases = new TestCase[] {
		getBarlinesTest(),
		getMiddleBarlinesTest(),
		//GOON getNavigationSignsTest(),
		//GOON getVoltasTest(),
		getAdvancedTest()
	};


	@Test public void findRepetitionsTest() {
		for (val testCase : testCases) {
			val reps = RepetitionsFinder.findRepetitions(testCase.score);
			assertEquals("Test: " + testCase.name, testCase.expectedRepetitions, reps);
		}
	}

	/**
	 * Creates a test case with barline repeats, with the following repetitions:
	 *
	 * repeats:                 2x
	 * measures:   |    :|:    :|     :|
	 * numbers:    |0    |1     |2     |
	 */
	private TestCase getBarlinesTest() {
		val name = "barlines test";

		val score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 17).execute();
		writeBackwardRepeat(score, 0, 1);
		writeForwardRepeat(score, 1);
		writeBackwardRepeat(score, 1, 2);
		writeBackwardRepeat(score, 2, 1);

		CList<PlayRange> ranges = clist();
		ranges.add(playRange(0, 1));
		ranges.add(playRange(0, 2));
		ranges.add(playRange(1, 2));
		ranges.add(playRange(1, 2));
		ranges.add(playRange(1, 3));
		ranges.add(playRange(1, 2));
		ranges.add(playRange(1, 2));
		ranges.add(playRange(1, 3));
		val expectedRepetitions = new Repetitions(ranges.close());

		return new TestCase(name, score, expectedRepetitions);
	}

	/**
	 * Creates a test score with barline repeats, also within measures, with the following repetitions:
	 *
	 * repeats:                             2x
	 * measures:   |     |     /:    :|    :\  |
	 * numbers:    |0    |1           |2       |
	 */
	private TestCase getMiddleBarlinesTest() {
		val name = "barlines test";

		val score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 17).execute();
		writeMiddleForwardRepeat(score, 1, _1$2);
		writeBackwardRepeat(score, 1, 1);
		writeMiddleBackwardRepeat(score, 2, _1$2, 2);

		CList<PlayRange> ranges = clist();
		ranges.add(new PlayRange(mp(0, _0), mp(2, _0)));
		ranges.add(new PlayRange(mp(1, _1$2), mp(2, _1$2)));
		ranges.add(new PlayRange(mp(1, _1$2), mp(2, _0)));
		ranges.add(new PlayRange(mp(1, _1$2), mp(2, _1$2)));
		ranges.add(new PlayRange(mp(1, _1$2), mp(2, _0)));
		ranges.add(new PlayRange(mp(1, _1$2), mp(3, _0)));
		val expectedRepetitions = new Repetitions(ranges.close());

		return new TestCase(name, score, expectedRepetitions);
	}

	/**
	 * Creates a more advanced test score with the following repetitions:
	 *                                                                          _____ ________ ________________              ___ ___
	 * voltas/repeats:                         2x   conrep                      1+2   3        4     senzarep                1   2
	 * measures:  |   |(segno)    |:  |   |   :|   |(d.c.)  |(tocoda)  |:      |    :|   |   :|   |  (dalsegno)|(coda) |:   |  :|   ||
	 * numbers:   |0  |1          |2  |3  |4   |5  |6       |7         |8      |9    |10 |11  |12 |13          |14     |15  |16 |17 ||
	 */
	private TestCase getAdvancedTest() {
		val name = "advanced test";

		val score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 17).execute();
		writeSign(score, 1, new Segno());
		writeForwardRepeat(score,2);
		writeBackwardRepeat(score,4, 2);
		writeSign(score,6, new DaCapo());
		writeSign(score,7, new Coda());
		writeForwardRepeat(score,8);
		writeVolta(score,9, 1, range(1, 2));
		writeBackwardRepeat(score,9, 1); //repeat times should be 2, but because of the "1.+2." volta the repeat is implicit - TODO: how does Sibelius/MuseScore handle this?
		writeVolta(score,10, 2, range(3, 3));
		writeBackwardRepeat(score,11, 1);
		writeVolta(score,12, 2, range(4, 4));
		writeSign(score,14, new Segno());
		writeSign(score,14, new Coda());
		writeForwardRepeat(score,15);
		writeVolta(score,16, 1, range(1));
		writeBackwardRepeat(score,16, 1);
		writeVolta(score,17, 1, range(2));

		CList<PlayRange> ranges = clist();
		ranges.add(playRange(0, 2));
		ranges.add(playRange(2, 5));
		ranges.add(playRange(2, 5));
		ranges.add(playRange(2, 5));
		ranges.add(playRange(5, 6)); //-> d.c. con.rep.
		ranges.add(playRange(0, 2));
		ranges.add(playRange(2, 5));
		ranges.add(playRange(2, 5));
		ranges.add(playRange(2, 5));
		ranges.add(playRange(5, 10));
		ranges.add(playRange(8, 10));
		ranges.add(playRange(8, 9));
		ranges.add(playRange(10, 12));
		ranges.add(playRange(8, 9));
		ranges.add(playRange(12, 14)); //-> dal segno senza rep
		ranges.add(playRange(1, 7));
		ranges.add(playRange(14, 17));
		ranges.add(playRange(15, 16));
		ranges.add(playRange(17, 18));
		val expectedRepetitions = new Repetitions(ranges.close());

		return new TestCase(name, score, expectedRepetitions);
	}

	private void writeForwardRepeat(Score score, int measure) {
		score.getColumnHeader(measure).setStartBarline(Barline.barlineForwardRepeat(BarlineStyle.HeavyLight));
	}

	private void writeMiddleForwardRepeat(Score score, int measure, Fraction beat) {
		score.getColumnHeader(measure).getMiddleBarlines().add(beatE(
				Barline.barlineForwardRepeat(BarlineStyle.Dashed), beat));
	}

	private void writeBackwardRepeat(Score score, int measure, int repeatTimes) {
		score.getColumnHeader(measure).setEndBarline(Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, repeatTimes));
	}

	private void writeMiddleBackwardRepeat(Score score, int measure, Fraction beat, int repeatTimes) {
		score.getColumnHeader(measure).getMiddleBarlines().add(beatE(
				Barline.barlineBackwardRepeat(BarlineStyle.Dashed, repeatTimes), beat));
	}

	private void writeSign(Score score, int measure, Direction sign) {
		score.getColumnHeader(measure).addOtherDirection(sign, _0);
	}

	private void writeVolta(Score score, int measure, int length, Range range) {
		score.getColumnHeader(measure).setVolta(new Volta(length, range, range.toString(), false));
	}

	private MP mp(int measure, Fraction beat) {
		return atBeat(unknown, measure, unknown, beat);
	}

}
