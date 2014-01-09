package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.io.midi.out.MidiConverter.calculateTickFromFraction;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.io.midi.out.Playlist.PlayRange;

/**
 * This class calculates the tempo changes for the {@link MidiConverter}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MidiTempoConverter {

	/**
	 * Writes tempo changes into the given tempo track.
	 * Only tempos found in the {@link ColumnHeader}s are used.
	 */
	public static void writeTempoTrack(Score score, Playlist playList, int resolution,
		MidiSequenceWriter<?> writer, int track) {
		long measurestarttick = 0;
		for (PlayRange playRange : playList.getRanges()) {
			for (int iMeasure : range(playRange.from.measure, playRange.to.measure)) {
				Fraction start = (playRange.from.measure == iMeasure ? playRange.from.beat : _0);
				Fraction end = (playRange.to.measure == iMeasure ?
					playRange.to.beat : score.getMeasureBeats(iMeasure));

				BeatEList<Tempo> tempos = score.getHeader().getColumnHeader(iMeasure).getTempos();
				if (tempos != null) {
					for (BeatE<Tempo> beatE : tempos) {
						long tick = measurestarttick +
							calculateTickFromFraction(beatE.beat.sub(start), resolution);
						writer.writeTempoChange(track, tick, beatE.getElement().getBeatsPerMinute());
					}
				}

				Fraction measureDuration = end.sub(start);
				measurestarttick += MidiConverter.calculateTickFromFraction(measureDuration, resolution);
			}
		}
	}

}
