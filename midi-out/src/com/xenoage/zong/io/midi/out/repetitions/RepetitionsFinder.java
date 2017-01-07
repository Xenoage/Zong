package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.direction.Direction;
import com.xenoage.zong.core.music.direction.NavigationSign;
import com.xenoage.zong.core.music.direction.Segno;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.core.position.MP;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.val;

import java.util.ArrayList;

import static com.xenoage.utils.collections.CollectionUtils.alist;
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
		Segno lastSegno = null; //if not null, the next segno will jump back to this one

		for (int iMeasure = 0; iMeasure < score.getMeasuresCount(); iMeasure++) {

			//special signs: segno, coda, dacapo
			val navSigns = getNavigationSigns(iMeasure);



		}

		return jumps;
	}

	private BeatEList<Direction> getNavigationSigns(int iMeasure) {
		val ret = new BeatEList<Direction>();
		for (val direction : score.getColumnHeader(iMeasure).getOtherDirections()) {
			if (direction.element instanceof NavigationSign)
				ret.add(direction);
		}
		return ret;
	}

}
