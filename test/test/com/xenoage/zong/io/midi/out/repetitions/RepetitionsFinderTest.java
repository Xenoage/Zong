package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineStyle;
import com.xenoage.zong.core.music.direction.*;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.position.Time;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.*;
import static com.xenoage.zong.core.music.util.BeatE.beatE;
import static com.xenoage.zong.io.midi.out.repetitions.RepetitionsTest.repetition;
import static org.junit.Assert.*;

/**
 * Tests for {@link RepetitionsFinder}.
 *
 * @author Andreas Wenger
 */
public class RepetitionsFinderTest {

	@AllArgsConstructor
	class TestCase {
		Score score;
		Repetitions expectedRepetitions;
	}


	/**
	 * Test case with no repeats at all:
	 *
	 * measures:   |     |      |      ||
	 * numbers:    |0    |1     |2     ||
	 */
	@Test public void testSimple() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 2).execute();

		val expectedRepetitions = new Repetitions(ilist(
				repetition(0, 3)));

		runTest(new TestCase(score, expectedRepetitions));
	}


	/**
	 * Test case with barline repeats, with the following repetitions:
	 *
	 * repeats:                 2x
	 * measures:   |    :|:    :|     :|
	 * numbers:    |0    |1     |2     |
	 */
	@Test public void testBarlines() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 2).execute();
		writeBackwardRepeat(0, 1);
		writeForwardRepeat(1);
		writeBackwardRepeat(1, 2);
		writeBackwardRepeat(2, 1);

		val expectedRepetitions = new Repetitions(ilist(
			repetition(0, 1),
			repetition(0, 2),
			repetition(1, 2),
			repetition(1, 3),
			repetition(1, 2),
			repetition(1, 2),
			repetition(1, 3)));

		runTest(new TestCase(score, expectedRepetitions));
	}

	/**
	 * Test case with barline repeats, also within measures, with the following repetitions:
	 *
	 * repeats:                             2x
	 * measures:   |     |     /:    :|    :\  |  /:  :\ |
	 * numbers:    |0    |1           |2       |3        |
	 */
	@Test public void testMiddleBarlines() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 3).execute();
		writeMiddleForwardRepeat(1, Companion.get_1$2());
		writeBackwardRepeat(1, 1);
		writeMiddleBackwardRepeat(2, Companion.get_1$2(), 2);
		writeMiddleForwardRepeat(3, Companion.get_1$4());
		writeMiddleBackwardRepeat(3, Companion.get_3$4(), 1);

		val expectedRepetitions = new Repetitions(ilist(
			new Repetition(bp(0, Companion.get_0()), bp(2, Companion.get_0())),
			new Repetition(bp(1, Companion.get_1$2()), bp(2, Companion.get_1$2())),
			new Repetition(bp(1, Companion.get_1$2()), bp(2, Companion.get_0())),
			new Repetition(bp(1, Companion.get_1$2()), bp(2, Companion.get_1$2())),
			new Repetition(bp(1, Companion.get_1$2()), bp(2, Companion.get_0())),
			new Repetition(bp(1, Companion.get_1$2()), bp(3, Companion.get_3$4())),
			new Repetition(bp(3, Companion.get_1$4()), bp(4, Companion.get_0()))));

		runTest(new TestCase(score, expectedRepetitions));
	}

	/**
	 * Test case with coda/segna/dacapo, with the following repetitions:
	 *
	 *                                senzarep                                 conrep                        senzarep
	 *                (tocoda)        (dacapo)      (coda)      (segno)      (dalsegno)     (segno2)       (dalsegno2)
	 * measures:   |    :|          |    |          |       |   |:       :|      |          |        |:        :|      ||
	 * numbers:    |0    |1         |2   |3         |4      |5  |6        |7     |8         |9       |10        |11    ||
	 */
	@Test public void testNavigationSigns() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 11).execute();
		writeBackwardRepeat(0, 1);
		writeNavigationOrigin(0, new Coda());
		writeNavigationOrigin(2, new DaCapo(false));
		writeNavigationTarget(4, new Coda());
		writeForwardRepeat(6);
		writeNavigationTarget(6, new Segno());
		writeBackwardRepeat(6, 1);
		writeNavigationOrigin(7, new Segno());
		writeForwardRepeat(10);
		writeNavigationTarget(9, new Segno());
		writeBackwardRepeat(10, 1);
		writeNavigationOrigin(10, segno(false));

		val expectedRepetitions = new Repetitions(ilist(
			repetition(0, 1),
			repetition(0, 3),
			repetition(0, 1),
			repetition(4, 7),
			repetition(6, 8),
			repetition(6, 7),
			repetition(6, 11),
			repetition(10, 11),
			repetition(9, 12)));

		runTest(new TestCase(score, expectedRepetitions));
	}

	/**
	 * Test case with voltas, with the following repetitions:
	 *                   ___ ___         ___ ____         ___ _____ _____
	 * voltas:           1   2           1   2            1   2+3   def
	 * measures:   |    |  :|   |   |   |  :|    |:      |  :|    :|     ||
	 * numbers:    |0   |1  |2  |3  |4  |5  |6   |7      |8  |9    |10   ||
	 */
	@Test public void testVoltas() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 10).execute();
		writeVolta(1, 1, range(1, 1));
		writeBackwardRepeat(1, 1);
		writeVolta(2, 1, range(2, 2));
		writeVolta(5, 1, range(1, 1));
		writeBackwardRepeat(5, 1);
		writeVolta(6, 1, range(2, 2));
		writeForwardRepeat(7);
		writeVolta(8, 1, range(1, 1));
		writeBackwardRepeat(8, 1);
		writeVolta(9, 1, range(2, 3));
		writeBackwardRepeat(9, 1); //should be 2, but 1 is also fine since within a volta we use this repeat sign implicitly
		writeVolta(10, 1, null);

		val expectedRepetitions = new Repetitions(ilist(
			repetition(0, 2),
			repetition(0, 1),
			repetition(2, 6), //-> back to the very beginning (because of missing forward repeat)
			repetition(0, 2),
			repetition(0, 1),
			repetition(2, 5),
			repetition(6, 9),
			repetition(7, 8),
			repetition(9, 10),
			repetition(7, 8),
			repetition(9, 10),
			repetition(7, 8),
			repetition(10, 11)));

		runTest(new TestCase(score, expectedRepetitions));
	}

	/**
	 * Test case with voltas and repeats, but with a senza repetizione da capo:
	 *                   ___ ___             ___ ____
	 * voltas:           1   2               1   2  d.c. senza rep
	 * measures:   |    |  :|   |:  |  :|:  |  :|   ||
	 * numbers:    |0   |1  |2  |3  |4  |5  |6  |7  ||
	 */
	@Test public void testSenzaRep() {
		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 7).execute();
		writeVolta(1, 1, range(1, 1));
		writeBackwardRepeat(1, 1);
		writeVolta(2, 1, range(2, 2));
		writeForwardRepeat(3);
		writeBackwardRepeat(4, 1);
		writeForwardRepeat(5);
		writeVolta(6, 1, range(1, 1));
		writeBackwardRepeat(6, 1);
		writeVolta(7, 1, range(2, 2));
		writeNavigationOrigin(7, new DaCapo(false));

		val expectedRepetitions = new Repetitions(ilist(
			repetition(0, 2),
			repetition(0, 1),
			repetition(2, 5),
			repetition(3, 7),
			repetition(5, 6),
			repetition(7, 8),
			repetition(0, 1),
			repetition(2, 6),
			repetition(7, 8)));

		runTest(new TestCase(score, expectedRepetitions));
	}

	/**
	 * Test case with repeats within voltas:
	 *                   _______________ ___
	 * voltas:           1               2
	 * measures:   |    |   |:  |  :|  :|   ||
	 * numbers:    |0   |1  |2  |3  |4  |5  ||
	 */
	@Test public void testRepeatsWithinVoltas() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 5).execute();
		writeVolta(1, 4, range(1, 1));
		writeForwardRepeat(2);
		writeBackwardRepeat(3, 1);
		writeBackwardRepeat(4, 1);
		writeVolta(5, 1, range(2, 2));

		val expectedRepetitions = new Repetitions(ilist(
				repetition(0, 4),
				repetition(2, 5),
				repetition(0, 1),
				repetition(5, 6)));

		runTest(new TestCase(score, expectedRepetitions));
	}

	/**
	 * A more advanced test case with the following repetitions:
	 *                                                               ____ ________ ________________              ___ ___
	 * voltas/repeats:                         2x                    1+2  3        4     senzarep                1   2
	 * measures:  |   |(segno)  |:  |   |   :|   |   |(tocoda)  |:  |   :|   |   :|   |  (dalsegno)|(coda) |:   |  :|   ||
	 * numbers:   |0  |1        |2  |3  |4   |5  |6  |7         |8  |9   |10 |11  |12 |13          |14     |15  |16 |17 ||
	 */
	@Test public void testAdvanced() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 17).execute();
		writeNavigationTarget(1, new Segno());
		writeForwardRepeat(2);
		writeBackwardRepeat(4, 2);
		writeNavigationOrigin(6, new Coda());
		writeForwardRepeat(8);
		writeVolta(9, 1, range(1, 2));
		writeBackwardRepeat(9, 2);
		writeVolta(10, 2, range(3, 3));
		writeBackwardRepeat(11, 1);
		writeVolta(12, 2, range(4, 4));
		writeNavigationOrigin(13, segno(false));
		writeNavigationTarget(14, new Coda());
		writeForwardRepeat(15);
		writeVolta(16, 1, range(1, 1));
		writeBackwardRepeat(16, 1);
		writeVolta(17, 1, range(2, 2));

		val expectedRepetitions = new Repetitions(ilist(
			repetition(0, 5),
			repetition(2, 5),
			repetition(2, 10),
			repetition(8, 10),
			repetition(8, 9),
			repetition(10, 12),
			repetition(8, 9),
			repetition(12, 14), //-> dal segno senza rep
			repetition(1, 7),
			repetition(14, 17),
			repetition(15, 16),
			repetition(17, 18)));

		runTest(new TestCase(score, expectedRepetitions));
	}

	private void runTest(TestCase testCase) {
		val reps = RepetitionsFinder.findRepetitions(testCase.score);
		assertEquals(testCase.expectedRepetitions, reps);
	}

	Score score;

	private void writeForwardRepeat(int measure) {
		score.getColumnHeader(measure).setStartBarline(Barline.barlineForwardRepeat(BarlineStyle.HeavyLight));
	}

	private void writeMiddleForwardRepeat(int measure, Fraction beat) {
		score.getColumnHeader(measure).getMiddleBarlines().add(Companion.beatE(
				Barline.barlineForwardRepeat(BarlineStyle.Dashed), beat));
	}

	private void writeBackwardRepeat(int measure, int repeatTimes) {
		score.getColumnHeader(measure).setEndBarline(Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, repeatTimes));
	}

	private void writeMiddleBackwardRepeat(int measure, Fraction beat, int repeatTimes) {
		score.getColumnHeader(measure).getMiddleBarlines().add(Companion.beatE(
				Barline.barlineBackwardRepeat(BarlineStyle.Dashed, repeatTimes), beat));
	}

	private void writeNavigationTarget(int measure, NavigationSign sign) {
		score.getColumnHeader(measure).setNavigationTarget(sign);
	}

	private void writeNavigationOrigin(int measure, NavigationSign sign) {
		score.getColumnHeader(measure).setNavigationOrigin(sign);
	}

	private void writeVolta(int measure, int length, Range range) {
		score.getColumnHeader(measure).setVolta(new Volta(length, range, ""+range, false));
	}

	private Segno segno(boolean conRep) {
		val ret = new Segno();
		ret.setWithRepeats(conRep);
		return ret;
	}

	private Time bp(int measure, Fraction beat) {
		return Time.Companion.time(measure, beat);
	}

}
