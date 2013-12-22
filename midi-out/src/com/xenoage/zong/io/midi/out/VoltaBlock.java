package com.xenoage.zong.io.midi.out;

import static com.xenoage.utils.kernel.Range.range;

import java.util.HashMap;
import java.util.Set;

import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.music.volta.Volta;

/**
 * This class packs a number of connected {@link Volta}s to one block.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
public class VoltaBlock {

	private HashMap<Volta, Integer> startMeasures = new HashMap<Volta, Integer>();


	public void addVolta(Volta volta, int measure) {
		startMeasures.put(volta, measure);
	}

	/**
	 * Returns the measure numbers of the measures that
	 * have to be played in the volta, when the playback reaches the
	 * volta block the given time, or null if no appropriate volta
	 * can be found.
	 */
	public Range getRange(int repeatTime) {
		Set<Volta> voltas = startMeasures.keySet();
		Volta foundVolta = null;
		//search for volta which matches the given time
		for (Volta volta : voltas) {
			Range range = volta.getNumbers();
			if (range != null && range.isInRange(repeatTime)) {
				foundVolta = volta;
				break;
			}
		}
		if (foundVolta == null) {
			//no volta found. search for default volta
			for (Volta volta : voltas) {
				if (volta.getNumbers() == null) {
					foundVolta = volta;
					break;
				}
			}
		}
		//return measure range
		if (foundVolta != null) {
			int startMeasure = startMeasures.get(foundVolta);
			return range(startMeasure, startMeasure + foundVolta.getLength() - 1);
		}
		else {
			return null;
		}
	}

	/**
	 * Calculates whether this time is the last time to repeat.
	 */
	public boolean isLastTime(int repeatTime) {
		if (repeatTime == getRepeatCount()) {
			return true;
		}
		return false;
	}

	/**
	 * Gets the number of repetitions.
	 */
	public int getRepeatCount() {
		int count = 0;
		for (Volta volta : startMeasures.keySet()) {
			Range range = volta.getNumbers();
			int size;
			if (range == null) {
				size = 1;
			}
			else {
				size = range.getStop() - range.getStart() + 1;
			}
			count += size;
		}
		return count;
	}

	/**
	 * Returns the number of measures in the block.
	 */
	public int getBlockLength() {
		int length = 0;
		for (Volta volta : startMeasures.keySet()) {
			length += volta.getLength();
		}
		return length;
	}
}
