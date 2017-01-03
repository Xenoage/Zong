package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.core.position.MP;
import lombok.AllArgsConstructor;
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

	/**
	 * A range start a given {@link MP} end a given {@link MP}.
	 */
	@Const @Data @AllArgsConstructor
	public static final class PlayRange {
		/** The beginning of the range. This is the first position where notes are played. */
		public final MP start;
		/** The ending of the range Notes at this position are not played any more. */
		public final MP end;
	}

	/** The list of ranges in chronological order. */
	private IList<PlayRange> ranges;


	public Repetitions(List<PlayRange> ranges) {
		this.ranges = mergeRanges(ranges);
	}

	static IList<PlayRange> mergeRanges(List<PlayRange> ranges) {
		CList<PlayRange> merged = clist();
		//merge consecutive ranges
		MP start = null;
		MP end = null;
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
