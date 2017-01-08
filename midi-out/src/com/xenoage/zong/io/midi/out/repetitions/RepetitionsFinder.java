package com.xenoage.zong.io.midi.out.repetitions;

import com.sun.istack.internal.NotNull;
import com.xenoage.utils.NullUtils;
import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.NavigationSign;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.position.MP;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.Map;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atBeat;
import static com.xenoage.zong.core.position.MP.mp0;
import static com.xenoage.zong.core.position.MP.unknown;

/**
 * Finds the {@link Repetitions} in a score.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RepetitionsFinder {

	/**
	 * A jump start a given {@link MP} to a given {@link MP}.
	 */
	@Const @Data @AllArgsConstructor
	public static final class Jump {
		public final MP from, to;
	}

	private Score score;
	private VoltaGroups voltaGroups;


	/**
	 * Finds the {@link Repetitions} in the given score.
	 */
	public static Repetitions findRepetitions(Score score) {
		return new RepetitionsFinder(score).find();
	}

	private Repetitions find() {
		ArrayList<PlayRange> ranges = alist();

		MP start = atBeat(unknown, 0, unknown, _0);
		Fraction lastMeasureBeats = score.getMeasureBeats(score.getMeasuresCount() - 1);
		MP end = atBeat(unknown, score.getMeasuresCount() - 1, unknown, lastMeasureBeats);

		ArrayList<Jump> jumplist = createJumpList();

		if (jumplist.size() == 0) {
			//simple case: no jumps
			ranges.add(new PlayRange(start, end));
		}
		else {
			//one or more jumps
			ranges.add(new PlayRange(start, jumplist.get(0).from));
			for (int i : range(1, jumplist.size() - 1)) {
				MP lastEnd = jumplist.get(i - 1).to;
				MP currentStart = jumplist.get(i).from;
				ranges.add(new PlayRange(lastEnd, currentStart));
			}
			ranges.add(new PlayRange(jumplist.get(jumplist.size() - 1).to, end));
		}

		return new Repetitions(ranges);
	}

	/**
	 * Creates the list of jumps for this score.
	 */
	private ArrayList<Jump> createJumpList() {
		ArrayList<Jump> jumps = alist();

		val voltaGroups = new VoltaGroupFinder(score).findAllVoltaGroups();
		Segno lastSegno = null; //if not null, the next segno will jump back to this one
		Map<MP, Integer> barlineRepeatCounter = map(); //maps backward repeats (their MP) to the number of already played repeats
		int lastVoltaCounter = 1; //in the next volta group, jump to this repeat number

		Fraction measureStartBeat = _0;
		nextMeasure: for (int iMeasure = 0; iMeasure < score.getMeasuresCount(); iMeasure++) {

			//inner barlines and special signs (segno, coda, dacapo)
			val innerElements = getInnerBarlinesAndNavigationSigns(iMeasure);
			for (val e : innerElements) {
				val eMp = MP.atBeat(unknown, iMeasure, unknown, e.beat);

				//inner backward repeat barline
				if (e.element instanceof Barline) {
					val innerBarline = (Barline) e.element;
					if (innerBarline.getRepeat().isBackward()) {
						int counter = notNull(barlineRepeatCounter.get(eMp), 0);
						if (counter < innerBarline.getRepeatTimes()) {
							barlineRepeatCounter.put(eMp, counter + 1);
							MP to = findLastForwardRepeatMp(eMp);
							jumps.add(new Jump(eMp, to));
							iMeasure = to.measure;
							measureStartBeat = to.beat;
							continue nextMeasure;
						}
					}
				}
			}

			measureStartBeat = _0;
		}

		return jumps;
	}

	/**
	 * Gets the middle {@link Barline}s and {@link NavigationSign}s in the given measure,
	 * sorted by beat. If a barline is on the same beat as a sign,
	 * the barline is listed before the sign, since it played first (e.g. first repeat,
	 * then, the second time, play the coda sign and jump).
	 */
	private BeatEList<ColumnElement> getInnerBarlinesAndNavigationSigns(int iMeasure) {
		val ret = new BeatEList<ColumnElement>();
		ret.addAll(getInnerBarlines(iMeasure));
		ret.addAll(getNavigationSigns(iMeasure));
		return ret;
	}

	private BeatEList<Direction> getNavigationSigns(int iMeasure) {
		val ret = new BeatEList<Direction>();
		for (val direction : score.getColumnHeader(iMeasure).getOtherDirections()) {
			if (direction.element instanceof NavigationSign)
				ret.add(direction);
		}
		return ret;
	}

	private BeatEList<Barline> getInnerBarlines(int iMeasure) {
		return score.getColumnHeader(iMeasure).getMiddleBarlines();
	}

	/**
	 * Finds the {@link MP} of the last forward repeat (also within measures),
	 * starting from the given {@link MP}. If there is none, {@link MP#mp0} is returned.
	 * If another volta group is found before, the measure after the volta is returned (since voltas
	 * can not be "nested").
	 * This value can not be cached during playback, but must be searched each time when
	 * needed. For example, imagine a score where a segno jumps into the middle of a
	 * repeating sequence. When reaching the right backward repeat, the left forward
	 * repeat should be used, and not the last forward repeat that was visited before
	 * the segno jump.
	 */
	private MP findLastForwardRepeatMp(MP fromMp) {

		//if we are within a volta group, find repeat within the current volta
		//or before the volta group, but not in the previous voltas
		val startVolta = voltaGroups.getStateAt(fromMp.measure);
		val startVoltaGroup = (startVolta != null ? startVolta.group : null);
		Range ignoreMeasuresRange = null;
		if (startVolta != null)
			ignoreMeasuresRange = range(startVolta.group.startMeasure, startVolta.voltaStartMeasure - 1);

		//iterate through measures in reverse order
		for (int iMeasure : rangeReverse(fromMp.measure)) {
			val measure = score.getColumnHeader(iMeasure);

			//ignore this measure?
			if (ignoreMeasuresRange != null && ignoreMeasuresRange.isInRange(iMeasure))
				continue;

			//another volta group at this measure? then we use an implicit forward repeat
			//at the first measure after that volta group
			val voltaGroup = voltaGroups.getVoltaGroupAt(iMeasure);
			if (voltaGroup != startVoltaGroup)
				return mp0.withMeasure(iMeasure + 1);

			//forward repeat barline within the measure?
			val innerBarlines = measure.getMiddleBarlines();
			//if starting in this measure, only before the given beat
			val innerStartBeat = (iMeasure == fromMp.measure ? fromMp.beat : null);
			for (val innerBarline : innerBarlines.reverseIt()) {
				if ((innerStartBeat == null || innerBarline.beat.compareTo(innerStartBeat) < 0) &&
						innerBarline.getElement().getRepeat().isForward())
					return atBeat(unknown, iMeasure, unknown, innerBarline.beat);
			}

			//forward repeat at the beginning of the measure?
			if (measure.getStartBarline() != null && measure.getStartBarline().getRepeat().isForward()) {
				return mp0.withMeasure(iMeasure);
			}
		}

		//nothing was found, so return the beginning of the score
		return mp0;
	}

}
