package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.header.ScoreHeader;
import com.xenoage.zong.core.music.barline.Barline;
import com.xenoage.zong.core.music.barline.BarlineRepeat;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.music.volta.Volta;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.io.midi.out.repetitions.Repetitions.PlayRange;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.map;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atBeat;
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

		MP pos = atBeat(unknown, 0, unknown, _0);
		for (int iMeasure : range(score.getMeasuresCount())) {

			if (voltaGroups.containsKey(iMeasure)) {

				//volta group starts at this measure
				VoltaGroup voltaGroup = voltaGroups.get(iMeasure);
				int voltatime = 1;
				while (voltatime <= voltaGroup.getRepeatCount()) {
					Range range = voltaGroup.getRange(voltatime);
					if (range == null)
						range = range(iMeasure, iMeasure); //TODO...
					MP stopPosition = atBeat(-1, range.getStop(), -1, score.getMeasureBeats(iMeasure));
					if (iMeasure != range.getStart()) {
						MP currentPosition = atBeat(-1, iMeasure, -1, _0);
						MP startPosition = atBeat(-1, range.getStart(), -1, _0);
						jumps.add(new Jump(currentPosition, startPosition));
					}
					if (!voltaGroup.isLastTime(voltatime)) {
						jumps.add(new Jump(stopPosition, pos));
					}
					voltatime++;
				}

				iMeasure += voltaGroup.getMeasuresCount() - 1;
				pos = atBeat(-1, iMeasure, -1, _0);
			}
			else {
				//check for normal barline repetition
				ColumnHeader columnHeader = score.getHeader().getColumnHeader(iMeasure);

				BeatEList<Barline> barlines = columnHeader.getMiddleBarlines();
				if (columnHeader.getStartBarline() != null) {
					barlines.add(columnHeader.getStartBarline(), _0);
				}
				if (columnHeader.getEndBarline() != null) {
					barlines.add(columnHeader.getEndBarline(), score.getMeasureBeats(iMeasure));
				}

				for (BeatE<Barline> barline : barlines.getElements()) {
					BarlineRepeat repeat = barline.element.getRepeat();
					if (repeat == BarlineRepeat.Backward || repeat == BarlineRepeat.Both) {
						//jump back end last forward repeat sign or beginning
						MP barlinePosition = atBeat(-1, iMeasure, -1, barline.beat);
						for (int a = 0; a < barline.element.getRepeatTimes(); a++) {
							jumps.add(new Jump(barlinePosition, pos));
						}
					}
					if (repeat == BarlineRepeat.Forward || repeat == BarlineRepeat.Both) {
						pos = atBeat(-1, iMeasure, -1, barline.beat);
					}
				}
			}
		}

		return jumps;
	}



}
