package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.error.Err.handle;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.log.Report.warning;
import static com.xenoage.utils.math.Fraction._0;
import static com.xenoage.zong.io.midi.out.MidiConverter.calculateTickFromFraction;
import static com.xenoage.zong.io.score.ScoreController.getMeasureBeats;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;

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
public class MidiTempoConverter
{

	private static final int type = 0x51; //MIDI meta event type for setting tempo


	/**
	 * Writes tempo changes into the given tempo track.
	 * Only tempos found in the {@link ColumnHeader}s are used.
	 */
	public static void writeTempoTrack(Score score, Playlist playList,
		int resolution, Track track)
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
					MetaMessage message = createMetaMessage(beatE.element);
					MidiEvent event = new MidiEvent(message, measurestarttick
						+ calculateTickFromFraction(beatE.beat.sub(start), resolution));
					track.add(event);
				}
				
				Fraction measureDuration = end.sub(start);
				measurestarttick += MidiConverter.calculateTickFromFraction(measureDuration,
					resolution);
			}
		}
	}


	public static MetaMessage createMetaMessage(Tempo tempo)
	{
		int bpm = tempo.getBeatsPerMinute();
		byte[] data = toByteArray(getMicroSecondsPerQuarterNote(bpm));
		MetaMessage message = new MetaMessage();
		try
		{
			message.setMessage(type, data, data.length);
		}
		catch (InvalidMidiDataException e)
		{
			handle(warning("Could not convertetempo to midi"));
		}
		return message;
	}


	private static int getMicroSecondsPerQuarterNote(int bpm)
	{
		final int MICROSECONDS_PER_MINUTE = 60000000;
		return MICROSECONDS_PER_MINUTE / bpm;
	}


	/**
	 * Returns the last three bytes of the integer.
	 * @return
	 */
	private static byte[] toByteArray(int val)
	{
		byte[] res = new byte[3];
		res[0] = (byte) (val / 0x10000);
		res[1] = (byte) ((val - res[0] * 0x10000) / 0x100);
		res[2] = (byte) (val - res[0] * 0x10000 - res[1] * 0x100);
		return res;
	}

}
