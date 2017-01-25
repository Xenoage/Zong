package com.xenoage.zong.io.midi.out.time;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.midi.out.MidiSettings;
import com.xenoage.zong.io.midi.out.repetitions.Repetition;
import com.xenoage.zong.io.midi.out.repetitions.Repetitions;
import lombok.AllArgsConstructor;
import lombok.val;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.Time.time;

/**
 * Creates a {@link TimeMap} for the whole score.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class TimeMapper {

	/** The score, for which the map is to be computed. */
	Score score;
	/** The precomputed {@link Repetitions} in this score (repeats barlines, voltas, segnos...). */
	Repetitions repetitions;
	/** See {@link MidiSettings#resolutionFactor}. */
	int resolutionFactor;


	public TimeMap createTimeMap() {
		//resolution per quarter note
		int resolution = score.getDivisions() * resolutionFactor;
		//for each play range compute the used beats in each measure column
		int tick = 0;
		val timeMap = new TimeMapBuilder(repetitions.getRepetitions().size());
		for (int iRange : range(repetitions.getRepetitions())) {
			val range = repetitions.getRepetitions().get(iRange);
			for (int iMeasure : range(range.start.measure, range.end.measure)) {
				val usedBeats = getUsedBeats(iMeasure, range);
				Fraction lastBeat = null;
				for (int iBeat : range(usedBeats)) {
					val beat = usedBeats.get(iBeat);
					//increase tick by distance between last and this beat
					if (lastBeat != null)
						tick += beat.sub(lastBeat).mult(resolution * 4).getNumerator();
					//add time
					timeMap.addTick(tick, time(iMeasure, beat), iRange);
					lastBeat = beat;
				}
			}
		}
		return timeMap.build();
	}

	/**
	 * Gets the used beats in the given measure column.
	 * When the given play range starts in this measure, only beats at or after the start beat
	 * of the range are returned.
	 * When the given play range ends in this measure, only beats at or before the end beat
	 * of the given range are returned.
	 */
	private List<Fraction> getUsedBeats(int iMeasure, Repetition range) {
		val usedBeats = score.getMeasureUsedBeats(iMeasure, true); //beats where something begins
		usedBeats.add(score.getMeasureFilledBeats(iMeasure)); //last beat (where last elements ends)
		List<Fraction> ret = alist(usedBeats.getSize());
		for (val beat : usedBeats) {
			if (iMeasure == range.start.measure) {
				if (beat.compareTo(range.start.beat) >= 0)
					ret.add(beat);
			}
			else if (iMeasure == range.end.measure) {
				if (beat.compareTo(range.end.beat) <= 0)
					ret.add(beat);
			}
			else {
				ret.add(beat);
			}
		}
		return ret;
	}

}
