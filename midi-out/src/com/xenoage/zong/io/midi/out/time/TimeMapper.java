package com.xenoage.zong.io.midi.out.time;

import com.xenoage.zong.core.Score;

/**
 * Creates a {@link TimeMap} for the whole score.
 *
 * @author Andreas Wenger
 */
public class TimeMapper {

	public static TimeMap createTimeMap(Score score) {
		return new TimeMapBuilder(0).build();
	}

}
