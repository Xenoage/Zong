package com.xenoage.zong.io.midi.out.score;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.kernel.Range;
import com.xenoage.zong.core.Score;
import lombok.Data;

import static com.xenoage.utils.kernel.Range.range;

/**
 * The staves of each part of a score.
 */
@Const @Data
public class PartStaves {

	private final int[] partStavesCount;


	/**
	 * Finds the {@link PartStaves} in a {@link Score}.
	 */
	public static PartStaves findPartStaves(Score score) {
		int[] partStavesCount = new int[score.getStavesList().getParts().size()];
		for (int iPart : range(partStavesCount))
			partStavesCount[iPart] = score.getStavesList().getParts().get(iPart).getStavesCount();
		return new PartStaves(partStavesCount);
	}

	/**
	 * Gets the staves indices of the given part.
	 */
	public Range getStaves(int part) {
		int start = 0;
		for (int iPart : range(0, part - 1))
			start += partStavesCount[iPart];
		return range(start, start + partStavesCount[part]);
	}

	/**
	 * Gets the part index, given the global staff index.
	 */
	public int getPartByStaff(int staff) {
		int nextPartStart = 0;
		for (int iPart : range(partStavesCount)) {
			nextPartStart += partStavesCount[iPart];
			if (staff < nextPartStart)
				return iPart;
		}
		throw new IllegalArgumentException("Staff " + staff + " does not exist");
	}

}
