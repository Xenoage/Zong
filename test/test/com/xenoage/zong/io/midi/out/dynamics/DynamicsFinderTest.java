package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.commands.core.music.MeasureElementWrite;
import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.direction.Dynamics;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.io.midi.out.dynamics.type.FixedDynamics;
import com.xenoage.zong.io.midi.out.dynamics.type.GradientDynamics;
import com.xenoage.zong.io.midi.out.repetitions.RepetitionsFinder;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.math.Fraction.*;
import static com.xenoage.zong.core.music.direction.DynamicsType.*;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.Time.time;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DynamicsFinder}.
 *
 * @author Andreas Wenger
 */
public class DynamicsFinderTest {

	/**
	 * Tests the following part:
	 *
	 *                    |0    |1    |2    |3    |4    ||
	 *                    |     |:    |    :|     |     ||
	 * staff 0: measures:  mp          pp      f <<< pp    //measure 3+4: f at 2/4, cresc at 3/4 to 1/4 (after that implicitly one level louder = ff), ff at 2/4
	 *           voice 0: ------ ----- ----- ----
	 *           voice 1:     mf------        --           //second part uses part volume, since there is an empty measure without voice 1 inbetween
	 * staff 1: measures:  p<<<<<<<<<<<f     ff
	 */
	@Test public void testAdvanced() {
		val score = getAdvancedScore();
		val expected = getAdvancedExpectedPeriods();
		val repetitions = RepetitionsFinder.findRepetitions(score);
		val actual = DynamicsFinder.findDynamics(score, new DynamicsInterpretation(), repetitions).getPeriods();
		assertEquals(expected, actual);
	}

	/**
	 * See {@link #testAdvanced()}.
	 */
	private Score getAdvancedScore() {
		Score score = new Score();
		new PartAdd(score, new Part("", null, 2, null), 0, null).execute();
		new MeasureAdd(score, 4).execute();
		new ColumnElementWrite(new TimeSignature(TimeType.time_4_4),
				score.getColumnHeader(0), null, null ).execute();
		//staff 0
		new MeasureElementWrite(new Dynamics(mp), score.getMeasure(atMeasure(0, 0)), _0).execute();
		new MeasureElementWrite(new Dynamics(mp), score.getMeasure(atMeasure(0, 0)), _0).execute();

		return score;
	}

	/**
	 * See {@link #testAdvanced()}.
	 */
	private DynamicsPeriods getAdvancedExpectedPeriods() {
		val d = new DynamicsPeriodsBuilder();
		//staff 0
		d.addPeriodToStaff(new DynamicsPeriod(
				time(0, _0), time(2, _0), new FixedDynamics(mp)), 0, 0);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(2, _0), time(3, _0), new FixedDynamics(pp)), 0, 0);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(1, _0), time(3, _1$2), new FixedDynamics(pp)), 0, 1);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(3, _1$2), time(3, _3$4), new FixedDynamics(f)), 0, 1);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(3, _3$4), time(4, _1$4), new GradientDynamics(f, ff)), 1, 0); //ff: implicit target after f-cresc
		d.addPeriodToStaff(new DynamicsPeriod(
				time(4, _1$4), time(4, _1$2), new FixedDynamics(ff)), 0, 1);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(4, _1$2), time(5, _0), new FixedDynamics(pp)), 0, 1);
		//voice 1 in staff 0
		d.addPeriodToVoice(new DynamicsPeriod(
				time(0, _1$2), time(2, _0), new FixedDynamics(mf)), 0, 1, 0); //only 1st time; we do not see the mf again
		//staff 1
		d.addPeriodToStaff(new DynamicsPeriod(
				time(0, _0), time(2, _0), new GradientDynamics(p, f)), 1, 0);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(2, _0), time(3, _0), new FixedDynamics(f)), 1, 0);
		d.addPeriodToStaff(new DynamicsPeriod( // f remains, we do not jump in the middle of a cresc
				time(1, _0), time(3, _0), new FixedDynamics(f)), 1, 1);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(3, _0), time(5, _0), new FixedDynamics(ff)), 1, 1);
		return d.build();
	}



}
