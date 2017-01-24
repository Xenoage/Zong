package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.io.midi.out.repetitions.Repetitions;
import com.xenoage.zong.io.midi.out.score.MeasureBeats;
import com.xenoage.zong.io.midi.out.score.PartStaves;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Finds the {@link Dynamics} in a {@link Score}.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicsFinder {

	private final Score score;
	private final DynamicsInterpretation interpretation;
	private final Repetitions repetitions;
	private final MeasureBeats measureBeats;
	private final PartStaves partStaves;

	public static Dynamics findDynamics(
			Score score, DynamicsInterpretation interpretation, Repetitions repetitions) {
		return new DynamicsFinder(score, interpretation, repetitions,
				MeasureBeats.findMeasureBeats(score), PartStaves.findPartStaves(score)).find();
	}

	private Dynamics find() {
		return new Dynamics(new DynamicsPeriodsBuilder().build(), interpretation, measureBeats, partStaves);
	}

}
