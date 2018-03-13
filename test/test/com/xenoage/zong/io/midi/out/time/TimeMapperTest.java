package com.xenoage.zong.io.midi.out.time;

import com.xenoage.zong.commands.core.music.MeasureAdd;
import com.xenoage.zong.commands.core.music.MeasureElementWrite;
import com.xenoage.zong.commands.core.music.PartAdd;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.Part;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.direction.DaCapo;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.rest.Rest;
import com.xenoage.zong.core.music.time.TimeSignature;
import com.xenoage.zong.core.music.time.TimeType;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.io.selection.Cursor;
import lombok.val;
import org.junit.Test;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._1;
import static com.xenoage.utils.math.Fraction._1$2;
import static com.xenoage.utils.math.Fraction.fr;
import static com.xenoage.zong.core.music.barline.Barline.barlineBackwardRepeat;
import static com.xenoage.zong.core.music.barline.Barline.barlineForwardRepeat;
import static com.xenoage.zong.core.music.barline.BarlineStyle.Regular;
import static com.xenoage.zong.core.position.MP.*;
import static com.xenoage.zong.core.position.Time.time;
import static com.xenoage.zong.io.midi.out.repetitions.RepetitionsFinder.findRepetitions;
import static junit.framework.TestCase.assertEquals;

/**
 * Tests for {@link TimeMapper}.
 *
 * @author Andreas Wenger
 */
public class TimeMapperTest {

	//ticks per quarter note
	private static final int resolution = 8;


	@Test public void createTimeMapTest() {
		val score = getScore();
		val expectedTimeMap = getTimeMap();
		assertEquals(expectedTimeMap, new TimeMapper(score, findRepetitions(score),8).createTimeMap());
	}

	/**
	 * Creates the following score (each space is a quarter note)
	 *
	 * measures:
	 * repetitions: voltas/signs:            1───┐2───┐   d.c. senza rep
	 *              barlines:     |    |:   |   :|    |1   ||
	 * part 0: staff 0: voice 0:  |1   |1   |2 2 |1   |1   ||
	 *                  voice 1:  |    |442 |42 4|1 d |1   ||   //d = direction
	 *         staff 1: voice 0:  |2 44|1   |    |1   |1   ||
	 * part 1: staff 2: voice 0:  |1   |1   |2 2 |    |2 2 ||
	 *
	 * play ranges:               0---------------
	 *                                 1----     2---------
	 *                            3---------     4---------
	 */
	private Score getScore() {
		val score = new Score();
		new PartAdd(score, new Part("p0", null, 2, null),
				0, null).execute();
		new PartAdd(score, new Part("p1", null, 1, null),
				1, null).execute();
		new MeasureAdd(score, 5).execute();
		//time, barlines, voltas and signs
		score.getColumnHeader(0).setTime(new TimeSignature(TimeType.time_4_4));
		score.getColumnHeader(1).setStartBarline(Companion.barlineForwardRepeat(Regular));
		score.getColumnHeader(2).setVolta(new Volta(1, range(1, 1), "1", true));
		score.getColumnHeader(2).setEndBarline(Companion.barlineBackwardRepeat(Regular, 1));
		score.getColumnHeader(3).setVolta(new Volta(1, range(2, 2), "2", true));
		score.getColumnHeader(4).setNavigationOrigin(new DaCapo(false));
		//staff 0, voice 0
		val cursor = new Cursor(score, mp0, true);
		cursor.write(e(1));
		cursor.write(e(1));
		cursor.write(e(2));
		cursor.write(e(2));
		cursor.write(e(1));
		cursor.write(e(1));
		//staff 0, voice 1
		cursor.setMp(atElement(0, 1, 1, 0));
		cursor.write(e(4));
		cursor.write(e(4));
		cursor.write(e(2));
		cursor.write(e(4));
		cursor.write(e(2));
		cursor.write(e(4));
		cursor.write(e(1));
		cursor.write(e(1));
		new MeasureElementWrite(new Dynamic(DynamicValue.f),
				score.getMeasure(atMeasure(0, 3)), Companion.get_1$2()).execute();
		//staff 1, voice 0
		cursor.setMp(atElement(1, 0, 0, 0));
		cursor.write(e(2));
		cursor.write(e(4));
		cursor.write(e(4));
		cursor.write(e(1));
		cursor.setMp(atElement(1, 2, 0, 0));
		cursor.write(e(1));
		cursor.write(e(1));
		//staff 2, voice 0
		cursor.setMp(atElement(2, 0, 0, 0));
		cursor.write(e(1));
		cursor.write(e(1));
		cursor.write(e(2));
		cursor.write(e(2));
		cursor.setMp(atElement(2, 4, 0, 0));
		cursor.write(e(2));
		cursor.write(e(2));
		return score;
	}

	/**
	 * See {@link #getScore()}.
	 * We use 8 ticks per quarter note.
	 */
	private TimeMap getTimeMap() {
		val timeMap = new TimeMapBuilder();
		//range 0
		addTick(timeMap, 0, 0, 0);
		addTick(timeMap, 0, 0, 2);
		addTick(timeMap, 0, 0, 3);
		addTick(timeMap, 0, 1, 0, true);
		addTick(timeMap, 0, 1, 1);
		addTick(timeMap, 0, 1, 2);
		addTick(timeMap, 0, 2, 0, true);
		addTick(timeMap, 0, 2, 1);
		addTick(timeMap, 0, 2, 2);
		addTick(timeMap, 0, 2, 3);
		addTick(timeMap, 0, 3, 0, true);
		//range 1
		addTick(timeMap, 1, 1, 0);
		addTick(timeMap, 1, 1, 1);
		addTick(timeMap, 1, 1, 2);
		addTick(timeMap, 1, 2, 0, true);
		//range 2
		addTick(timeMap, 2, 3, 0);
		addTick(timeMap, 2, 3, 2);
		addTick(timeMap, 2, 4, 0, true);
		addTick(timeMap, 2, 4, 2);
		addTick(timeMap, 2, 5, 0, true);
		//range 3
		addTick(timeMap, 3, 0, 0);
		addTick(timeMap, 3, 0, 2);
		addTick(timeMap, 3, 0, 3);
		addTick(timeMap, 3, 1, 0, true);
		addTick(timeMap, 3, 1, 1);
		addTick(timeMap, 3, 1, 2);
		addTick(timeMap, 3, 2, 0, true);
		//range 4
		addTick(timeMap, 4, 3, 0);
		addTick(timeMap, 4, 3, 2);
		addTick(timeMap, 4, 4, 0, true);
		addTick(timeMap, 4, 4, 2);
		addTick(timeMap, 4, 5, 0, true);
		return timeMap.build();
	}

	private void addTick(TimeMapBuilder timeMap, int repetition, int measure, int quarterBeat) {
		addTick(timeMap, repetition, measure, quarterBeat, false);
	}

	private void addTick(TimeMapBuilder timeMap, int repetition, int measure, int quarterBeat,
											 boolean isAlsoLastBeatInPreviosMeasure) {
		int playRangeMeasureOffset = new int[]{0, 3, 4, 6, 8}[repetition];
		int playRangeStartMeasure = new int[]{0, 1, 3, 0, 3}[repetition];
		int internalMeasure = measure - playRangeStartMeasure;
		if (isAlsoLastBeatInPreviosMeasure)
			timeMap.addTimeNoMs(resolution * ((playRangeMeasureOffset + internalMeasure - 1) * 4 + 4),
					new RepTime(repetition, Companion.time(measure - 1, Companion.get_1())));
		timeMap.addTimeNoMs(resolution * ((playRangeMeasureOffset + internalMeasure) * 4 + quarterBeat),
				new RepTime(repetition, Companion.time(measure, Companion.fr(quarterBeat, 4))));
	}

	/**
	 * Creates a note. 4 = quarter, 2 = half, 1 = whole
	 */
	private VoiceElement e(int note) {
		return new Rest(Companion.fr(1, note));
	}

}
