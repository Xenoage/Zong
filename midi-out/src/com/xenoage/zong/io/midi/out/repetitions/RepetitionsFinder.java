package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.ColumnElement;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.NavigationSign;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.core.position.MP;
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
import static com.xenoage.zong.core.music.util.BeatEList.beatEList;
import static com.xenoage.zong.core.position.Time.time;
import static com.xenoage.zong.core.position.Time.time0;
import static java.lang.Math.min;

/**
 * Finds the {@link Repetitions} in a score.
 *
 * @author Andreas Wenger
 */
public class RepetitionsFinder {

	/**
	 * A jump from a given {@link Time} to a given {@link Time}.
	 */
	@Const @Data @AllArgsConstructor
	public static final class Jump {
		public final Time from, to;
	}

	//state
	private Score score;
	private VoltaGroups voltaGroups;
	//maps backward repeats (their Time) to the number of already played repeats
	private Map<Time, Integer> barlineRepeatCounter = map();
	//loop index: index of the current measure
	private int currentMeasure;
	//where to start in the current measure. null = complete measure
	@MaybeNull private Fraction currentMeasureStartBeat;

	/**
	 * Finds the {@link Repetitions} in the given score.
	 */
	public static Repetitions findRepetitions(Score score) {
		return new RepetitionsFinder(score).find();
	}

	private RepetitionsFinder(Score score) {
		this.score = score;
	}

	private Repetitions find() {
		ArrayList<PlayRange> ranges = alist();

		this.voltaGroups = new VoltaGroupFinder(score).findAllVoltaGroups();

		Time start = time(0, _0);
		Time end = time(score.getMeasuresCount(), _0);

		ArrayList<Jump> jumplist = createJumpList();

		if (jumplist.size() == 0) {
			//simple case: no jumps
			ranges.add(new PlayRange(start, end));
		}
		else {
			//one or more jumps
			ranges.add(new PlayRange(start, jumplist.get(0).from));
			for (int i : range(1, jumplist.size() - 1)) {
				Time lastEnd = jumplist.get(i - 1).to;
				Time currentStart = jumplist.get(i).from;
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

		int lastVoltaCounter = 1; //in the next volta group, jump to this repeat number

		Fraction measureStartBeat = null;
		nextMeasure: for (currentMeasure = 0; currentMeasure < score.getMeasuresCount();) {

			//inner barlines and special signs (segno, coda, dacapo)
			val innerElements = getInnerBarlinesAndNavigationSigns();
			for (val e : innerElements) {
				val eTime = time(currentMeasure, e.beat);

				//inner backward repeat barline
				if (e.element instanceof Barline) {
					val innerBarline = (Barline) e.element;
					if (innerBarline.getRepeat().isBackward()) {
						val jump = processBackwardRepeat(innerBarline, eTime);
						if (jump != null) {
							jumps.add(jump);
							continue nextMeasure;
						}
					}
				}

				//GOON: coda, segno, dacapo
			}

			//GOON: volta

			//backward repeat at measure end
			val endBarline = score.getColumnHeader(currentMeasure).getEndBarline();
			if (endBarline != null) {
				if (endBarline.getRepeat().isBackward()) {
					Time end = time(currentMeasure + 1, _0);
					val jump = processBackwardRepeat(endBarline, end);
					if (jump != null) {
						jumps.add(jump);
						continue nextMeasure;
					}
				}
			}

			//no jump found in this measure, continue
			currentMeasure++;
			currentMeasureStartBeat = null;
		}

		return jumps;
	}

	/**
	 * Processes the given backward repeat barline at the given time.
	 * When another repeat is to be played, a {@link Jump} is returned
	 * and the current measure and start beat is modified.
	 * Otherwise null is returned.
	 */
	@MaybeNull private Jump processBackwardRepeat(Barline barline, Time barlineTime) {
		int counter = notNull(barlineRepeatCounter.get(barlineTime), 0);
		if (counter < barline.getRepeatTimes()) {
			//repeat. jump back to last forward repeat
			barlineRepeatCounter.put(barlineTime, counter + 1);
			Time to = findLastForwardRepeatTime(barlineTime);
			currentMeasure = to.measure;
			currentMeasureStartBeat = to.beat;
			return new Jump(barlineTime, to);
		}
		else {
			//finished, delete counter
			barlineRepeatCounter.remove(barlineTime);
			return null;
		}
	}

	/**
	 * Gets the middle {@link Barline}s and {@link NavigationSign}s in the current measure,
	 * sorted by beat. If a barline is on the same beat as a sign,
	 * the barline is listed before the sign, since it played first (e.g. first repeat,
	 * then, the second time, play the coda sign and jump).
	 * When the current measure start beat is not null, only elements after (not at) that beat
	 * are returned.
	 */
	private BeatEList<ColumnElement> getInnerBarlinesAndNavigationSigns() {
		BeatEList<ColumnElement> ret = beatEList();
		ret.addAll(getInnerBarlines());
		ret.addAll(getNavigationSigns());
		if (currentMeasureStartBeat != null)
			ret = ret.filter(Interval.After, currentMeasureStartBeat);
		return ret;
	}

	/**
	 * Gets the {@link NavigationSign}s in the current measure.
	 */
	private BeatEList<Direction> getNavigationSigns() {
		val ret = new BeatEList<Direction>();
		for (val direction : score.getColumnHeader(currentMeasure).getOtherDirections()) {
			if (direction.element instanceof NavigationSign)
				ret.add(direction);
		}
		return ret;
	}

	/**
	 * Gets the inner {@link Barline}s within of the current measure.
	 */
	private BeatEList<Barline> getInnerBarlines() {
		return score.getColumnHeader(currentMeasure).getMiddleBarlines();
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
	private Time findLastForwardRepeatTime(Time from) {

		//if we are within a volta group, find repeat within the current volta
		//or before the volta group, but not in the previous voltas
		val startVolta = voltaGroups.getStateAt(from.measure);
		val startVoltaGroup = (startVolta != null ? startVolta.group : null);
		Range ignoreMeasuresRange = null;
		if (startVolta != null)
			ignoreMeasuresRange = range(startVolta.group.startMeasure, startVolta.voltaStartMeasure - 1);

		//iterate through measures in reverse order
		int fromMeasure = min(from.measure, score.getMeasuresCount() - 1); //from.measure could be 1 measure after score
		for (int iMeasure : rangeReverse(fromMeasure, 0)) {
			val measure = score.getColumnHeader(iMeasure);

			//ignore this measure?
			if (ignoreMeasuresRange != null && ignoreMeasuresRange.isInRange(iMeasure))
				continue;

			//another volta group at this measure? then we use an implicit forward repeat
			//at the first measure after that volta group
			val voltaGroup = voltaGroups.getVoltaGroupAt(iMeasure);
			if (voltaGroup != startVoltaGroup)
				return time(iMeasure + 1, _0);

			//forward repeat barline within the measure?
			val innerBarlines = measure.getMiddleBarlines();
			//if starting in this measure, only before the given beat
			val innerStartBeat = (iMeasure == from.measure ? from.beat : null);
			for (val innerBarline : innerBarlines.reverseIt()) {
				if ((innerStartBeat == null || innerBarline.beat.compareTo(innerStartBeat) < 0) &&
						innerBarline.getElement().getRepeat().isForward())
					return time(iMeasure, innerBarline.beat);
			}

			//forward repeat at the beginning of the measure? (but not when we started at beat 0 of this measure)
			if (false == time(iMeasure, _0).equals(from))
				if (measure.getStartBarline() != null && measure.getStartBarline().getRepeat().isForward())
					return time(iMeasure, _0);
		}

		//nothing was found, so return the beginning of the score
		return time0;
	}

}
