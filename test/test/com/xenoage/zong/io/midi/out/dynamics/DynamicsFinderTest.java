package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.zong.commands.core.music.ColumnElementWrite;
import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.commands.core.music.MeasureElementWrite;
import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MeasureSide;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeEnd;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.io.midi.out.dynamics.type.FixedDynamics;
import com.xenoage.zong.io.midi.out.dynamics.type.GradientDynamics;
import com.xenoage.zong.io.midi.out.repetitions.RepetitionsFinder;
import com.xenoage.zong.io.selection.Cursor;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.math.Fraction.*;
import static com.xenoage.zong.core.music.Pitch.pi;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;
import static com.xenoage.zong.core.music.barline.BarlineStyle.Regular;
import static com.xenoage.zong.core.music.chord.ChordFactory.chord;
import static com.xenoage.zong.core.music.direction.DynamicValue.*;
import static com.xenoage.zong.core.music.direction.DynamicValue.mp;
import static com.xenoage.zong.core.music.direction.WedgeType.Crescendo;
import static com.xenoage.zong.core.music.direction.WedgeType.Diminuendo;
import static com.xenoage.zong.core.position.MP.*;
import static com.xenoage.zong.core.position.Time.time;
import static org.junit.Assert.*;

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
	 *           voice 0: ------ ----- ----- -----
	 *           voice 1:     mf------------       -----
	 * staff 1: measures:  ff>>>>>>>>>>mp    p
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
		Wedge w;
		new PartAdd(score, new Part("", null, 2, null), 0, null).execute();
		new MeasureAdd(score, 5).execute();
		//time signature and repeats
		new ColumnElementWrite(new TimeSignature(TimeType.time_4_4),
				score.getColumnHeader(0), null, null ).execute();
		new ColumnElementWrite(barlineForwardRepeat(Regular),
				score.getColumnHeader(1), null, MeasureSide.Left).execute();
		new ColumnElementWrite(barlineBackwardRepeat(Regular, 1),
				score.getColumnHeader(2), null, MeasureSide.Right).execute();
		//staff 0
		new MeasureElementWrite(new Dynamic(mp), score.getMeasure(atMeasure(0, 0)), _0).execute();
		new MeasureElementWrite(new Dynamic(pp), score.getMeasure(atMeasure(0, 2)), _0).execute();
		new MeasureElementWrite(new Dynamic(f), score.getMeasure(atMeasure(0, 3)), _1$2).execute();
		new MeasureElementWrite(w = new Wedge(Crescendo), score.getMeasure(atMeasure(0, 3)), _3$4).execute();
		new MeasureElementWrite(new WedgeEnd(w), score.getMeasure(atMeasure(0, 4)), _1$4).execute();
		new MeasureElementWrite(new Dynamic(pp), score.getMeasure(atMeasure(0, 4)), _1$2).execute();
		//voice 0
		val cursor = new Cursor(score, mp0, true);
		cursor.write(new Rest(_1));
		cursor.write(new Rest(_1));
		cursor.write(new Rest(_1));
		cursor.write(new Rest(_1));
		//voice 1
		cursor.setMp(atElement(0, 0, 1, 0));
		cursor.write(new Rest(_1$2));
		val chord = chord(pi(0, 4), _1$2);
		chord.addDirection(new Dynamic(mf));
		cursor.write(chord);
		cursor.write(new Rest(_1));
		cursor.write(new Rest(_1));
		cursor.setMp(atElement(0, 4, 1, 0));
		cursor.write(new Rest(_1));
		//staff 1
		new MeasureElementWrite(new Dynamic(ff), score.getMeasure(atMeasure(1, 0)), _0).execute();
		new MeasureElementWrite(w = new Wedge(Diminuendo), score.getMeasure(atMeasure(1, 0)), _0).execute();
		new MeasureElementWrite(new WedgeEnd(w), score.getMeasure(atMeasure(1, 2)), _0).execute();
		new MeasureElementWrite(new Dynamic(mp), score.getMeasure(atMeasure(1, 2)), _0).execute();
		new MeasureElementWrite(new Dynamic(p), score.getMeasure(atMeasure(1, 3)), _0).execute();
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
				time(3, _3$4), time(4, _1$4), new GradientDynamics(f, ff)), 0, 1); //ff: implicit target after f-cresc
		d.addPeriodToStaff(new DynamicsPeriod(
				time(4, _1$4), time(4, _1$2), new FixedDynamics(ff)), 0, 1);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(4, _1$2), time(5, _0), new FixedDynamics(pp)), 0, 1);
		//voice 1 in staff 0
		d.addPeriodToVoice(new DynamicsPeriod(
				time(0, _1$2), time(3, _0), new FixedDynamics(mf)), 0, 1, 0); //only 1st time; we do not see the mf again
		//staff 1
		d.addPeriodToStaff(new DynamicsPeriod(
				time(0, _0), time(2, _0), new GradientDynamics(ff, mp)), 1, 0);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(2, _0), time(3, _0), new FixedDynamics(mp)), 1, 0);
		d.addPeriodToStaff(new DynamicsPeriod( // f remains, we do not jump in the middle of a cresc
				time(1, _0), time(3, _0), new FixedDynamics(mp)), 1, 1);
		d.addPeriodToStaff(new DynamicsPeriod(
				time(3, _0), time(5, _0), new FixedDynamics(p)), 1, 1);
		return d.build();
	}



}
