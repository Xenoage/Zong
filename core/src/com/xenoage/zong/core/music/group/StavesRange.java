package com.xenoage.zong.core.music.group;

import static com.xenoage.utils.kernel.Range.range;
import lombok.Data;

import com.xenoage.utils.kernel.Range;

/**
 * Range of adjacent staves.
 *
 * @author Andreas Wenger
 */
@Data
public final class StavesRange {

	/** The index of the first staff of the range. */
	private int start;
	/** The index of the last staff of the range. */
	private int stop;


	/**
	 * Creates a new {@link StavesRange} with at least 1 staff.
	 */
	public StavesRange(int start, int stop) {
		if (stop - start < 0)
			throw new IllegalArgumentException("must contain at least 1 staff");
		this.start = start;
		this.stop = stop;
	}

	/**
	 * Gets the number of staves.
	 */
	public int getCount() {
		return stop - start + 1;
	}

	/**
	 * Returns true, if the group contains the whole given group.
	 */
	public boolean contains(StavesRange staves) {
		return (staves.start >= start && staves.stop <= stop);
	}
	
	/**
	 * Returns true, if the group intersects with the given group.
	 */
	public boolean intersects(StavesRange staves) {
		return staves.start <= stop && staves.stop >= start;
	}

	/**
	 * Returns true, if the group contains the staff
	 * with the given index.
	 */
	public boolean contains(int index) {
		return (index >= start && index <= stop);
	}
	
	/**
	 * Adds the given staves to this range.
	 */
	public StavesRange merge(StavesRange staves) {
		int mergedStart = Math.min(staves.start, start);
		int mergedStop = Math.max(staves.stop, stop);
		return new StavesRange(mergedStart, mergedStop);
	}

	/**
	 * Gets this range as a {@link Range}.
	 */
	public Range getRange() {
		return range(start, stop);
	}

	/**
	 * Shifts the indices by the given amount.
	 */
	public void shift(int amount) {
		start += amount;
		stop += amount;
	}

	/**
	 * Shifts the end index by the given amount.
	 */
	public void shiftEnd(int amount) {
		stop += amount;
	}

	/**
	 * Inserts the given number of staves at the given position.
	 * If the position is before the start, the whole range is shifted.
	 * If the position is at or after the start, but at or before the end, only the end is shifted
	 * (i.e. the range is enlarged).
	 * If the position is after the end, nothing happens.
	 */
	public void insert(int index, int stavesCount) {
		if (index < start)
			shift(stavesCount); //shift start and end
		else if (index <= stop)
			shiftEnd(stavesCount); //shift only the end
	}

	/**
	 * Removes the given number of staves starting at the given position.
	 * If true is returned, the whole range was removed. If false is returned,
	 * this range is still alive and the start and stop values were modified. 
	 */
	public boolean remove(int index, int stavesCount) {
		int lastIndex = index + stavesCount - 1;
		if (index <= start && lastIndex >= stop) {
			//whole range is removed
			return true;
		}
		else if (index >= start && lastIndex <= stop) {
			//all removed staves are within this range
			stop -= stavesCount;
		}
		else if (lastIndex < start) {
			//removed range before this range. shift start and end
			shift(-stavesCount);
		}
		else if (index > stop) {
			//removed range after this range. nothing to do.
		}
		else if (index < start && lastIndex >= start) {
			//a upper part of the range was removed
			start = index;
			stop = stop - lastIndex;
		}
		else if (index <= stop) {
			//a lower part of the range was removed
			stop = index - 1;
		}
		return false;
	}

}
