package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.io.midi.out.MidiConverter.calculateTickFromFraction;
import static com.xenoage.zong.io.score.ScoreController.getMeasureBeats;

import com.leff.midi.MidiTrack;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.header.ColumnHeader;
import com.xenoage.zong.core.music.direction.Tempo;
import com.xenoage.zong.core.music.util.BeatE;
import com.xenoage.zong.core.music.util.BeatEList;
import com.xenoage.zong.io.midi.out.Playlist.PlayRange;


/**
 * This class provides the ability to calculate the tempo for the midifile.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class MidiTempoConverterAndroid
{


	/**
	 * Writes tempo changes into the given tempo track.
	 * Only tempos found in the {@link ColumnHeader}s are used.
	 */
	public static void writeTempoTrack(Score score, Playlist playList,
		int resolution, MidiTrack track)
	{
		long measurestarttick = 0;
		for (PlayRange playRange : playList.ranges)
		{
			for (int iMeasure : range(playRange.from.measure, playRange.to.measure))
			{
				Fraction start = (playRange.from.measure == iMeasure ?
					playRange.from.beat : _0);
				Fraction end = (playRange.to.measure == iMeasure ?
					playRange.to.beat : getMeasureBeats(score, iMeasure));

				BeatEList<Tempo> tempos = score.header.getColumnHeader(iMeasure).tempos;
				for (BeatE<Tempo> beatE : tempos)
				{
					long tick = measurestarttick + calculateTickFromFraction(beatE.beat.sub(start), resolution);
					com.leff.midi.event.meta.Tempo event = new com.leff.midi.event.meta.Tempo(tick, 0, 0);
					event.setBpm(beatE.element.getBeatsPerMinute());
					track.insertEvent(event);
				}
				
				Fraction measureDuration = end.sub(start);
				measurestarttick += MidiConverter.calculateTickFromFraction(measureDuration,
					resolution);
			}
		}
	}

}
