package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.dynamics.type.DynamicsType;
import com.xenoage.zong.io.midi.out.score.MeasureBeats;
import lombok.Data;
import lombok.val;

/**
 * A period of time with a fixed or changing dynamic.
 *
 * When a dynamics sign appears, this is usually the begin of a new
 * {@link DynamicsPeriod}. It also has a duration, since it may not only
 * end when another sign appears, but also when it belongs to a voice
 * which ends somewhere.
 *
 * @author Andreas Wenger
 */
@Const @Data
public class DynamicsPeriod {

	/** Start time (inclusive). */
	public final Time startTime;
	/** End time (exclusive). */
	public final Time endTime;
	/** The fixed or gradient dynamics. */
	public final DynamicsType dynamics;

	/**
	 * Returns true, iff the given time is within this period.
	 */
	public boolean contains(Time time) {
		return startTime.compareTo(time) <= 0 && time.compareTo(endTime) < 0;
	}

	/**
	 * Gets the exact dynamics at the given time, using the given {@link DynamicsInterpretation}.
	 * To compute the value, also the length of each measure must be given.
	 */
	public float getVolumeAt(Time time, DynamicsInterpretation interpretation,
																MeasureBeats measures) {
		if (time.compareTo(endTime) > 0)
			throw new IllegalArgumentException("given time is after period");
		else if (time.compareTo(startTime) < 0)
			throw new IllegalArgumentException("given time is before period");
		val beatsToTime = measures.computeBeatsBetween(startTime, time);
		val beatsToEnd = measures.computeBeatsBetween(startTime, endTime);
		float progress = beatsToTime.divideBy(beatsToEnd).toFloat();
		return dynamics.getVolumeAt(progress, interpretation);
	}

	@Override public String toString() {
		return "{start=" + startTime.toStringCompact() + ", end=" + endTime.toStringCompact() +
				", dynamics=" + dynamics + "}";
	}

}