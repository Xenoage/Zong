package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.core.position.MP.atBeat;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.AllArgsConstructor;

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
import com.xenoage.zong.io.midi.out.Playlist.PlayRange;

/**
 * Class to calculate a {@link Playlist} for the midi converter.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MidiRepetitionCalculator {

	/**
	 * A jump from a given {@link MP} to a given {@link MP}.
	 */
	@AllArgsConstructor
	public static final class Jump {

		public final MP from, to;
	}


	/**
	 * Creates a playlist from the given score.
	 */
	public static Playlist createPlaylist(Score score) {
		ArrayList<PlayRange> ret = alist();

		MP start = atBeat(-1, 0, -1, _0);
		Fraction lastMeasureBeats = score.getMeasureBeats(score.getMeasuresCount() - 1);
		MP end = atBeat(-1, score.getMeasuresCount() - 1, -1, lastMeasureBeats);

		ArrayList<Jump> jumplist = createJumpList(score);
		int size = jumplist.size();

		if (size == 0) {
			ret.add(new PlayRange(start, end));
		}
		else {
			ret.add(new PlayRange(start, jumplist.get(0).from));
			for (int i : range(1, jumplist.size() - 1)) {
				MP pos1 = jumplist.get(i - 1).to;
				MP pos2 = jumplist.get(i).from;
				ret.add(new PlayRange(pos1, pos2));
			}
			ret.add(new PlayRange(jumplist.get(size - 1).to, end));
		}

		return new Playlist(ret);
	}

	/**
	 * Creates the list of jumps for the given {@link Score}.
	 */
	private static ArrayList<Jump> createJumpList(Score score) {
		ArrayList<Jump> jumps = alist();

		HashMap<Integer, VoltaBlock> voltaBlocks = createVoltaBlocks(score);

		MP pos = atBeat(1, 0, -1, _0);
		for (int iMeasure : range(score.getMeasuresCount())) {
			if (voltaBlocks.containsKey(iMeasure)) {
				//volta
				VoltaBlock voltaBlock = voltaBlocks.get(iMeasure);

				int voltatime = 1;
				while (voltatime <= voltaBlock.getRepeatCount()) {
					Range range = voltaBlock.getRange(voltatime);
					if (range == null)
						range = range(iMeasure, iMeasure); //TODO...
					MP stopPosition = atBeat(-1, range.getStop(), -1, score.getMeasureBeats(iMeasure));
					if (iMeasure != range.getStart()) {
						MP currentPosition = atBeat(-1, iMeasure, -1, _0);
						MP startPosition = atBeat(-1, range.getStart(), -1, _0);
						jumps.add(new Jump(currentPosition, startPosition));
					}
					if (!voltaBlock.isLastTime(voltatime)) {
						jumps.add(new Jump(stopPosition, pos));
					}
					voltatime++;
				}

				iMeasure += voltaBlock.getBlockLength() - 1;
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
						//jump back to last forward repeat sign or beginning
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

	/**
	 * Looks for {@link VoltaBlock}s in the score and creates a list of them.
	 */
	private static HashMap<Integer, VoltaBlock> createVoltaBlocks(Score score) {
		HashMap<Integer, VoltaBlock> map = new HashMap<Integer, VoltaBlock>();
		ScoreHeader scoreHeader = score.getHeader();
		for (int i : range(score.getMeasuresCount())) {
			if (scoreHeader.getColumnHeader(i).getVolta() != null) {
				VoltaBlock voltaBlock = createVoltaBlock(score, i);
				map.put(i, voltaBlock);
				i += voltaBlock.getBlockLength() - 1;
			}
		}
		return map;
	}

	/**
	 * Creates a {@link VoltaBlock} starting at the measure with the given index.
	 */
	private static VoltaBlock createVoltaBlock(Score score, int startMeasure) {
		ScoreHeader scoreHeader = score.getHeader();
		VoltaBlock block = new VoltaBlock();
		int iMeasure = startMeasure;
		while (iMeasure < score.getMeasuresCount()) {
			Volta volta = scoreHeader.getColumnHeader(iMeasure).getVolta();
			if (volta != null) {
				block.addVolta(volta, iMeasure);
				iMeasure += volta.getLength();
			}
			else {
				break;
			}
		}
		return block;
	}
}
