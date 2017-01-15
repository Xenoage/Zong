package com.xenoage.zong.io.midi.out.repetitions;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.position.Time;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A playback range from a given {@link Time} to a given {@link Time}.
 *
 * @author Andreas Wenger
 */
@Const @Data @AllArgsConstructor
public final class PlayRange {

	/** The beginning of the range. This is the first position where notes are played. */
	public final Time start;
	/** The ending of the range. Notes at this position are not played any more. */
	public final Time end;

	@Override public String toString() {
		return String.format("[%d;%s to %d;%s]", start.measure, start.beat, end.measure, end.beat);
	}

}
