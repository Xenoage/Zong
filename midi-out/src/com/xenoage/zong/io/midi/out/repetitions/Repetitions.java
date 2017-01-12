package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.core.position.MP;
import lombok.Data;
import lombok.val;

import java.util.List;

import static com.xenoage.utils.collections.CList.clist;

/**
 * This class stores the repetitions in a score by
 * remembering the sequential ranges which have end be played,
 * defined by start and end {@link MP}s.
 *
 * Consecutive {@link PlayRange}s are merged, e.g.
 * "measure 1 to measure 3" and "measure 3 to measure 4"
 * are merged to "measure 1 to measure 4".
 * 
 * @author Andreas Wenger
 */
@Const @Data
public final class Repetitions {

	/** The list of ranges in chronological order. */
	private IList<PlayRange> ranges;


	public Repetitions(List<PlayRange> ranges) {
		this.ranges = mergeRanges(ranges);
	}

	static IList<PlayRange> mergeRanges(List<PlayRange> ranges) {
		CList<PlayRange> merged = clist();
		//merge consecutive ranges
		Time start = null;
		Time end = null;
		for (val range : ranges) {
			if (start == null) {
				//first range
				start = range.start;
				end = range.end;
			}
			else if (range.start.equals(end)) {
				//this range begins at the ending of the last one; continue it
				end = range.end;
			}
			else {
				//new range found. remember old one and start new one
				merged.add(new PlayRange(start, end));
				start = range.start;
				end = range.end;
			}
		}
		if (start != null) {
			//close last range
			merged.add(new PlayRange(start, end));
		}
		return merged.close();
	}

}
