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
import com.xenoage.zong.core.position.BP;
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
import static com.xenoage.zong.core.position.BP.bp;
import static com.xenoage.zong.core.position.BP.bp0;
import static java.lang.Math.min;

/**
 * Finds the {@link Repetitions} in a score.
 *
 * @author Andreas Wenger
 */
public class RepetitionsFinder {

	/**
	 * A jump start a given {@link MP} to a given {@link MP}.
	 */
	@Const @Data @AllArgsConstructor
	public static final class Jump {
		public final BP from, to;
	}

	private Score score;
	private VoltaGroups voltaGroups;


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

		BP start = bp(0, _0);
		BP end = bp(score.getMeasuresCount(), _0);

		ArrayList<Jump> jumplist = createJumpList();

		if (jumplist.size() == 0) {
			//simple case: no jumps
			ranges.add(new PlayRange(start, end));
		}
		else {
			//one or more jumps
			ranges.add(new PlayRange(start, jumplist.get(0).from));
			for (int i : range(1, jumplist.size() - 1)) {
				BP lastEnd = jumplist.get(i - 1).to;
				BP currentStart = jumplist.get(i).from;
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

		Map<BP, Integer> barlineRepeatCounter = map(); //maps backward repeats (their BP) to the number of already played repeats

		int lastVoltaCounter = 1; //in the next volta group, jump to this repeat number

		Fraction measureStartBeat = null; //null = complete measure
		nextMeasure: for (int iMeasure = 0; iMeasure < score.getMeasuresCount();) {

			//inner barlines and special signs (segno, coda, dacapo)
			val innerElements = getInnerBarlinesAndNavigationSigns(iMeasure, measureStartBeat);
			for (val e : innerElements) {
				val eBp = bp(iMeasure, e.beat);

				//inner backward repeat barline
				if (e.element instanceof Barline) {
					val innerBarline = (Barline) e.element;
					if (innerBarline.getRepeat().isBackward()) {
						int counter = notNull(barlineRepeatCounter.get(eBp), 0);
						if (counter < innerBarline.getRepeatTimes()) {
							barlineRepeatCounter.put(eBp, counter + 1);
							BP to = findLastForwardRepeatBp(eBp);
							jumps.add(new Jump(eBp, to));
							iMeasure = to.measure;
							measureStartBeat = to.beat;
							continue nextMeasure;
						}
						else {
							//finished, delete counter
							barlineRepeatCounter.remove(eBp);
						}
					}
				}

				//GOON: coda, segno, dacapo
			}

			//GOON: volta

			//backward repeat at measure end
			val endBarline = score.getColumnHeader(iMeasure).getEndBarline();
			if (endBarline != null) {
				if (endBarline.getRepeat().isBackward()) {
					BP end = bp(iMeasure + 1, _0);
					int counter = notNull(barlineRepeatCounter.get(end), 0);
					if (counter < endBarline.getRepeatTimes()) {
						barlineRepeatCounter.put(end, counter + 1);
						BP to = findLastForwardRepeatBp(end);
						jumps.add(new Jump(end, to));
						iMeasure = to.measure;
						measureStartBeat = to.beat;
						continue nextMeasure;
					}
					else {
						//finished, delete counter
						barlineRepeatCounter.remove(end);
					}
				}
			}

			//no jump found in this measure, continue
			iMeasure++;
			measureStartBeat = null;
		}

		return jumps;
	}

	/**
	 * Gets the middle {@link Barline}s and {@link NavigationSign}s in the given measure,
	 * sorted by beat. If a barline is on the same beat as a sign,
	 * the barline is listed before the sign, since it played first (e.g. first repeat,
	 * then, the second time, play the coda sign and jump).
	 * When the afterBeat argument is not null, only elements after (not at) the given beat
	 * are returned.
	 */
	private BeatEList<ColumnElement> getInnerBarlinesAndNavigationSigns(int iMeasure, @MaybeNull Fraction afterBeat) {
		BeatEList<ColumnElement> ret = beatEList();
		ret.addAll(getInnerBarlines(iMeasure));
		ret.addAll(getNavigationSigns(iMeasure));
		if (afterBeat != null)
			ret = ret.filter(Interval.After, afterBeat);
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
	private BP findLastForwardRepeatBp(BP from) {

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
				return bp(iMeasure + 1, _0);

			//forward repeat barline within the measure?
			val innerBarlines = measure.getMiddleBarlines();
			//if starting in this measure, only before the given beat
			val innerStartBeat = (iMeasure == from.measure ? from.beat : null);
			for (val innerBarline : innerBarlines.reverseIt()) {
				if ((innerStartBeat == null || innerBarline.beat.compareTo(innerStartBeat) < 0) &&
						innerBarline.getElement().getRepeat().isForward())
					return bp(iMeasure, innerBarline.beat);
			}

			//forward repeat at the beginning of the measure? (but not when we started at beat 0 of this measure)
			if (false == bp(iMeasure, _0).equals(from))
				if (measure.getStartBarline() != null && measure.getStartBarline().getRepeat().isForward())
					return bp(iMeasure, _0);
		}

		//nothing was found, so return the beginning of the score
		return bp0;
	}

}
