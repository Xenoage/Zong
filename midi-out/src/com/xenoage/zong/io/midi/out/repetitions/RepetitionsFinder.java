package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.direction.Coda;
import com.xenoage.zong.core.music.direction.DaCapo;
import com.xenoage.zong.core.music.direction.NavigationSign;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.music.util.Interval;
import com.xenoage.zong.core.position.Time;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.collections.CollectionUtils.*;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.utils.math.Fraction._0;
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
	private List<Jump> jumps = alist();
	//voltas
	private VoltaGroups voltaGroups;
	//repeat counter for all volta groups
	private Map<VoltaGroup, Integer> voltaGroupsCounter = map();;
	//maps backward repeats (their Time) to the number of already played repeats
	private Map<Time, Integer> barlineRepeatCounter = map();
	//list of played navigation signs
	private Set<NavigationSign> playedNavigationSigns = set();
	//true after a dacapo or segno jump, before we see the next origin navigation sign
	private boolean isWithinJumpRepeat = false;
	//loop index: index of the current measure
	private int currentMeasureIndex;
	//where to start in the current measure. null = complete measure
	@MaybeNull private Fraction currentMeasureStartBeat;
	//con repetizione (true, play repeats) or senza repetizione (false, ignore repeats)
	private boolean isWithRepeats = true;

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

		collectJumps();

		if (jumps.size() == 0) {
			//simple case: no jumps
			ranges.add(new PlayRange(start, end));
		}
		else {
			//one or more jumps
			ranges.add(new PlayRange(start, jumps.get(0).from));
			for (int i : range(1, jumps.size() - 1)) {
				Time lastEnd = jumps.get(i - 1).to;
				Time currentStart = jumps.get(i).from;
				ranges.add(new PlayRange(lastEnd, currentStart));
			}
			ranges.add(new PlayRange(jumps.get(jumps.size() - 1).to, end));
		}

		return new Repetitions(ranges);
	}

	/**
	 * Creates the list of jumps for this score.
	 */
	private void collectJumps() {
		Segno lastSegno = null; //if not null, the next segno will jump back to this one

		int lastVoltaCounter = 1; //in the next volta group, jump to this repeat number

		Fraction measureStartBeat = null;
		nextMeasure: for (currentMeasureIndex = 0; currentMeasureIndex < score.getMeasuresCount();) {
			val measure = score.getColumnHeader(currentMeasureIndex);

			//enter a volta
			if (voltaGroups.getVoltaGroupStartingAt(currentMeasureIndex) != null)
				if (processVolta())
					continue nextMeasure;

			//inner backward repeat barlines
			if (isWithRepeats) {
				for (val e : getInnerBarlines()) {
					val innerBarline = e.element;
					val eTime = time(currentMeasureIndex, e.beat);
					if (innerBarline.getRepeat().isBackward())
						if (processBackwardRepeat(innerBarline, eTime))
							continue nextMeasure;
				}
			}

			//backward repeat at measure end
			val endBarline = measure.getEndBarline();
			if (isWithRepeats && endBarline != null) {
				if (endBarline.getRepeat().isBackward()) {
					Time end = time(currentMeasureIndex + 1, _0);
					if (processBackwardRepeat(endBarline, end))
						continue nextMeasure;
				}
			}

			//origin navigation sign
			//we read them after the backward repeat barlines. e.g. when there is both
			//a repeat and a "to coda", we first play the repeat and then the "to coda".
			val sign = measure.getNavigationOrigin();
			if (sign != null) {
				//da capo ,
				if (MusicElementType.DaCapo.is(sign))
					if (processDaCapo((DaCapo) sign))
						continue nextMeasure;

				//target segno
				if (MusicElementType.Segno.is(sign))
					if (processSegno((Segno) sign))
						continue nextMeasure;

				//to coda
				if (MusicElementType.Coda.is(sign))
					if (processCoda((Coda) sign))
						continue nextMeasure;
			}

			//no jump found in this measure, continue
			currentMeasureIndex++;
			currentMeasureStartBeat = null;
		}
	}

	/**
	 * Processes the given backward repeat {@link Barline} at the given {@link Time}.
	 * When another repeat is to be played, a {@link Jump} is added to
	 * the jump list, the current measure and start beat are modified
	 * and true is returned.
	 * Otherwise false is returned.
	 */
	private boolean processBackwardRepeat(Barline barline, Time barlineTime) {
		int counter = notNull(barlineRepeatCounter.get(barlineTime), 0);
		if (counter < barline.getRepeatTimes()) {
			//repeat. jump back to last forward repeat
			if (voltaGroups.getVoltaGroupAt(barlineTime.measure) == null)
				barlineRepeatCounter.put(barlineTime, counter + 1); //remember repeats only for barlines from outside voltas
			Time to = findLastForwardRepeatTime(barlineTime);
			addJump(barlineTime, to);
			return true;
		}
		else {
			//finished, delete counter
			barlineRepeatCounter.remove(barlineTime);
			return false;
		}
	}

	/**
	 * Processes the given {@link DaCapo} in the current measure.
	 * When it was not played yet, a {@link Jump} to the beginning is added to
	 * the jump list,the con repetizione and jump repeat flags are updated,
	 * the current measure and start beat are modified and true is returned.
	 * Otherwise false is returned.
	 */
	private boolean processDaCapo(DaCapo daCapo) {
		//each da capo is played only one time to avoid endless repeats
		if (false == playedNavigationSigns.contains(daCapo)) {
			//jump back to the beginning
			playedNavigationSigns.add(daCapo);
			isWithRepeats = daCapo.isWithRepeats();
			isWithinJumpRepeat = true;
			addJump(time(currentMeasureIndex + 1, _0), time0);
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Processes the given origin {@link Segno} in the current measure.
	 * When it was not played yet, and there is an earlier target segno,
	 * a {@link Jump} to that last target segno is added to
	 * the jump list, the con repetizione and jump repeat flags are updated,
	 * the current measure and start beat are modified and true is returned.
	 * Otherwise false is returned.
	 */
	private boolean processSegno(Segno segno) {
		//each origin segno is played only one time to avoid endless repeats
		if (false == playedNavigationSigns.contains(segno)) {
			//jump back to last target segno
			playedNavigationSigns.add(segno);
			isWithRepeats = segno.isWithRepeats();
			isWithinJumpRepeat = true;
			addJump(time(currentMeasureIndex + 1, _0), time(findSegnoTargetMeasure(), _0));
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Processes the given origin "to {@link Coda}" at the current measure.
	 * When we are within a jump repeat and the coda was not played yet,
	 * and there is a later target coda,
	 * a {@link Jump} to that target coda is added to
	 * the jump list, the con repetizione flag set back to true,
	 * the jump repeat flag set back to false,
	 * the current measure and start beat are modified and true is returned.
	 * Otherwise false is returned.
	 */
	private boolean processCoda(Coda coda) {
		//codas are only played when we are within a repeat caused by a dacapo or segno
		if (isWithinJumpRepeat) {
			//each origin coda is played only one time to avoid endless repeats
			if (false == playedNavigationSigns.contains(coda)) {
				//find next target coda
				val nextCodaMeasure = findCodaTargetMeasure();
				if (nextCodaMeasure > 0) {
					//jump forward to the next coda
					playedNavigationSigns.add(coda);
					isWithRepeats = true; //after coda jump, always con repetizione
					isWithinJumpRepeat = false;
					addJump(time(currentMeasureIndex + 1, _0), time(nextCodaMeasure, _0));
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Processes the volta group at the current measure.
	 * When playing the first volta is not fine, a {@link Jump} into the right volta
	 * is created, or if nothing suitable is found, a {@link Jump} into to the first measure
	 * after the volta group is created.
	 * True is returned, iff a jump was created.
	 */
	private boolean processVolta() {
		val voltaGroup = voltaGroups.getVoltaGroupAt(currentMeasureIndex);
		int nextNumber = notNull(voltaGroupsCounter.get(voltaGroup), 1);
		val targetVolta = voltaGroup.findVolta(nextNumber);
		if (targetVolta != null) {
			//suitable volta found
			//update repeat counter
			if (voltaGroup.getRepeatCount() == nextNumber)
				voltaGroupsCounter.remove(voltaGroup); //last repeat; volta group is then finished
			else
				voltaGroupsCounter.put(voltaGroup, nextNumber + 1); //next time one number higher
			//prepare jump
			if (currentMeasureIndex == targetVolta.startMeasure) {
				//we are already in the right volta; no jump needed
				return false;
			}
			else {
				//create jump into right volta
				addJump(time(currentMeasureIndex, _0), time(targetVolta.startMeasure, _0));
				return true;
			}
		}
		else {
			//no suitable volta found
			voltaGroupsCounter.remove(voltaGroup);
			addJump(time(currentMeasureIndex, _0), time(voltaGroup.endMeasure + 1, _0));
			return true;
		}
	}

	/**
	 * Adds a {@link Jump} to the list of jumps and updates the current
	 * measure index and start beat accordingly.
	 */
	private void addJump(Time from, Time to) {
		jumps.add(new Jump(from, to));
		currentMeasureIndex = to.measure;
		currentMeasureStartBeat = to.beat;
	}

	/**
	 * Gets the inner {@link Barline}s within of the current measure.
	 * When the current measure start beat is not null, only barlines after (not at) that beat
	 * are returned.
	 */
	private BeatEList<Barline> getInnerBarlines() {
		val ret = score.getColumnHeader(currentMeasureIndex).getMiddleBarlines();
		if (currentMeasureStartBeat != null)
			return ret.filter(Interval.After, currentMeasureStartBeat);
		return ret;
	}

	/**
	 * Finds the {@link Time} of the last forward repeat (also within measures),
	 * starting from the given time. If there is none, the beginning of the score is returned.
	 * This value can not be cached during playback, but must be searched each time when
	 * needed. For example, imagine a score where a segno jumps into the middle of a
	 * repeating sequence. When reaching the right backward repeat, the left forward
	 * repeat should be used, and not the last forward repeat that was visited before
	 * the segno jump.
	 */
	private Time findLastForwardRepeatTime(Time from) {

		//if we are within a volta group, find repeat within the current volta
		//or before the volta group, but not in the previous voltas of this group
		val startVolta = voltaGroups.getStateAt(from.measure);
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

			//barline within the measure?
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

	/**
	 * Finds the measure of the last target {@link Segno} starting from the current measure.
	 * If there is none, the first measure is returned.
	 */
	private int findSegnoTargetMeasure() {
		//iterate through measures in reverse order
		for (int iMeasure : rangeReverse(currentMeasureIndex, 0)) {
			val sign = score.getColumnHeader(iMeasure).getNavigationTarget();
			if (MusicElementType.Segno.is(sign))
				return iMeasure;
		}
		//nothing was found, jump to the beginning of the score
		return 0;
	}

	/**
	 * Finds the measure of the next target {@link Coda} starting from the current measure.
	 * If there is none, -1 is returned.
	 */
	private int findCodaTargetMeasure() {
		//iterate through measures
		for (int iMeasure : range(currentMeasureIndex, score.getMeasuresCount() - 1)) {
			val sign = score.getColumnHeader(iMeasure).getNavigationTarget();
			if (MusicElementType.Coda.is(sign))
				return iMeasure;
		}
		//nothing was found
		return -1;
	}

}
