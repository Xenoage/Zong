package com.xenoage.zong.io.midi.out.repetitions;

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
import com.xenoage.zong.core.position.BP;
import lombok.AllArgsConstructor;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.collections.CList.ilist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction.*;
import static com.xenoage.zong.core.music.util.BeatE.beatE;
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
		Score score;
		Repetitions expectedRepetitions;
	}


	/**
	 * Test case with no repetitions at all:
	 *
	 * measures:   |     |      |      ||
	 * numbers:    |0    |1     |2     ||
	 */
	@Test public void testSimple() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 2).execute();

		val expectedRepetitions = new Repetitions(ilist(
				playRange(0, 3)));

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
			playRange(0, 1),
			playRange(0, 2),
			playRange(1, 2),
			playRange(1, 3),
			playRange(1, 2),
			playRange(1, 2),
			playRange(1, 3)));

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
		writeMiddleForwardRepeat(1, _1$2);
		writeBackwardRepeat(1, 1);
		writeMiddleBackwardRepeat(2, _1$2, 2);
		writeMiddleForwardRepeat(3, _1$4);
		writeMiddleBackwardRepeat(3, _3$4, 1);

		val expectedRepetitions = new Repetitions(ilist(
			new PlayRange(bp(0, _0), bp(2, _0)),
			new PlayRange(bp(1, _1$2), bp(2, _1$2)),
			new PlayRange(bp(1, _1$2), bp(2, _0)),
			new PlayRange(bp(1, _1$2), bp(2, _1$2)),
			new PlayRange(bp(1, _1$2), bp(2, _0)),
			new PlayRange(bp(1, _1$2), bp(3, _3$4)),
			new PlayRange(bp(3, _1$4), bp(4, _0))));

		runTest(new TestCase(score, expectedRepetitions));
	}

	/**
	 * Test case with coda/segna/dacapo, with the following repetitions:
	 *
	 *                                    senzarep                                conrep                    senzarep
	 *                    (tocoda)        (dacapo)   (coda)      (segno)          (dalsegno)     (segno2)   (dalsegno2)
	 * measures:   |    :|          |    |          |       |   |:       :|      |           |  |:        :|          ||
	 * numbers:    |0    |1         |2   |3         |4      |5  |6        |7     |8          |9 |10        |11        ||
	 */
	@Test public void testNavigationSigns() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 11).execute();
		writeBackwardRepeat(0, 1);
		writeSign(1, new Coda());
		writeSign(3, daCapo(false));
		writeSign(4, new Coda());
		writeForwardRepeat(6);
		writeSign(6, new Segno());
		writeBackwardRepeat(6, 1);
		writeSign(8, new Segno());
		writeForwardRepeat(10);
		writeSign(10, new Segno());
		writeBackwardRepeat(10, 1);
		writeSign(10, segno(false));

		val expectedRepetitions = new Repetitions(ilist(
			playRange(0, 1),
			playRange(0, 3),
			playRange(0, 1),
			playRange(4, 7),
			playRange(6, 8),
			playRange(6, 7),
			playRange(6, 11),
			playRange(10, 11),
			playRange(10, 12)));

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
		writeVolta(6, 1, range(1, 1));
		writeForwardRepeat(7);
		writeVolta(8, 1, range(1, 1));
		writeBackwardRepeat(8, 1);
		writeVolta(9, 1, range(2, 3));
		writeBackwardRepeat(9, 1); //implicitly repeats two times because of volta
		writeVolta(10, 1, null);

		val expectedRepetitions = new Repetitions(ilist(
			playRange(0, 2),
			playRange(0, 1),
			playRange(2, 6), //-> back to the very beginning, repeating the 1st volta group
			playRange(0, 2),
			playRange(0, 1),
			playRange(2, 5),
			playRange(6, 9),
			playRange(7, 8),
			playRange(9, 10),
			playRange(7, 8),
			playRange(9, 10),
			playRange(10, 11)));

		runTest(new TestCase(score, expectedRepetitions));
	}

	/**
	 * A more advanced test case with the following repetitions:
	 *                                                                          _____ ________ ________________              ___ ___
	 * voltas/repeats:                         2x   conrep                      1+2   3        4     senzarep                1   2
	 * measures:  |   |(segno)    |:  |   |   :|   |(d.c.)  |(tocoda)  |:      |    :|   |   :|   |  (dalsegno)|(coda) |:   |  :|   ||
	 * numbers:   |0  |1          |2  |3  |4   |5  |6       |7         |8      |9    |10 |11  |12 |13          |14     |15  |16 |17 ||
	 */
	@Test public void testAdvanced() {

		score = ScoreFactory.create1Staff();
		new MeasureAdd(score, 17).execute();
		writeSign(1, new Segno());
		writeForwardRepeat(2);
		writeBackwardRepeat(4, 2);
		writeSign(6, new DaCapo());
		writeSign(7, new Coda());
		writeForwardRepeat(8);
		writeVolta(9, 1, range(1, 2));
		writeBackwardRepeat(9, 1); //repeat times should be 2, but because of the "1.+2." volta the repeat is implicit - TODO: how does Sibelius/MuseScore handle this?
		writeVolta(10, 2, range(3, 3));
		writeBackwardRepeat(11, 1);
		writeVolta(12, 2, range(4, 4));
		writeSign(14, segno(false));
		writeSign(14, new Coda());
		writeForwardRepeat(15);
		writeVolta(16, 1, range(1));
		writeBackwardRepeat(16, 1);
		writeVolta(17, 1, range(2));

		val expectedRepetitions = new Repetitions(ilist(
			playRange(0, 2),
			playRange(2, 5),
			playRange(2, 5),
			playRange(2, 5),
			playRange(5, 6), //-> d.c. con.rep.
			playRange(0, 2),
			playRange(2, 5),
			playRange(2, 5),
			playRange(2, 5),
			playRange(5, 10),
			playRange(8, 10),
			playRange(8, 9),
			playRange(10, 12),
			playRange(8, 9),
			playRange(12, 14), //-> dal segno senza rep
			playRange(1, 7),
			playRange(14, 17),
			playRange(15, 16),
			playRange(17, 18)));

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
		score.getColumnHeader(measure).getMiddleBarlines().add(beatE(
				Barline.barlineForwardRepeat(BarlineStyle.Dashed), beat));
	}

	private void writeBackwardRepeat(int measure, int repeatTimes) {
		score.getColumnHeader(measure).setEndBarline(Barline.barlineBackwardRepeat(BarlineStyle.LightHeavy, repeatTimes));
	}

	private void writeMiddleBackwardRepeat(int measure, Fraction beat, int repeatTimes) {
		score.getColumnHeader(measure).getMiddleBarlines().add(beatE(
				Barline.barlineBackwardRepeat(BarlineStyle.Dashed, repeatTimes), beat));
	}

	private void writeSign(int measure, Direction sign) {
		score.getColumnHeader(measure).addOtherDirection(sign, _0);
	}

	private void writeVolta(int measure, int length, Range range) {
		score.getColumnHeader(measure).setVolta(new Volta(length, range, range.toString(), false));
	}

	private DaCapo daCapo(boolean conRep) {
		val ret = new DaCapo();
		ret.setConRepetizione(conRep);
		return ret;
	}

	private Segno segno(boolean conRep) {
		val ret = new Segno();
		ret.setConRepetizione(conRep);
		return ret;
	}

	private BP bp(int measure, Fraction beat) {
		return BP.bp(measure, beat);
	}

}
