package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.ScoreFactory;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.dynamics.type.FixedDynamics;
import com.xenoage.zong.io.midi.out.dynamics.type.GradientDynamics;
import lombok.val;

import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.utils.math.Fraction._1$2;
import static com.xenoage.zong.core.music.direction.DynamicsType.*;
import static com.xenoage.zong.core.position.Time.time;

/**
 * Tests for {@link DynamicsFinder}.
 *
 * @author Andreas Wenger
 */
public class DynamicsFinderTest {

	/**
	 * Tests the following part:
	 *
	 *                    |0    |1    |2    |3    ||
	 *                    |     |:    |    :|     ||
	 * staff 0: measures:  mp          pp
	 *           voice 0:
	 *           voice 1:     mf-------
	 * staff 1: measures:  p<<<<<<<<<<<f     ff
	 *
	 * The last found dynamic is valid for the whole part.
	 * If there is more than one dynamic, the dynamic on the current
	 * staff is more important. Even more important is a dynamic
	 * in the current voice. But as soon as the voice is over (shown
	 * by --- above), the part dynamic is valid again.
	 *
	 */
	private Score getScore() {
		return ScoreFactory.create1Staff();
	}

	/**
	 * See {@link #getScore()}.
	 */
	private DynamicsPeriods getExpectedPeriods() {
		val p = new DynamicsPeriodsBuilder();
		//staff 0
		p.addPeriodToStaff(new DynamicsPeriod(
				time(0, _0), time(2, _0), new FixedDynamics(mp)), 0, 0);
		p.addPeriodToStaff(new DynamicsPeriod(
				time(2, _0), time(3, _0), new FixedDynamics(pp)), 0, 0);
		p.addPeriodToStaff(new DynamicsPeriod(
				time(1, _0), time(4, _0), new FixedDynamics(pp)), 0, 1);
		//voice 1 in staff 0
		p.addPeriodToVoice(new DynamicsPeriod(
				time(0, _1$2), time(2, _0), new FixedDynamics(mf)), 0, 1, 0); //only 1st time; we do not see the mf again
		//staff 1
		p.addPeriodToStaff(new DynamicsPeriod(
				time(0, _0), time(2, _0), new GradientDynamics(DynamicsType.p, f)), 1, 0);
		p.addPeriodToStaff(new DynamicsPeriod(
				time(2, _0), time(3, _0), new FixedDynamics(f)), 1, 0);
		p.addPeriodToStaff(new DynamicsPeriod( // f remains, we do not jump in the middle of a cresc
				time(1, _0), time(3, _0), new FixedDynamics(f)), 1, 1);
		p.addPeriodToStaff(new DynamicsPeriod(
				time(3, _0), time(4, _0), new FixedDynamics(ff)), 1, 1);
		return p.build();
	}



}
