package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.position.Time;
import com.xenoage.zong.io.midi.out.dynamics.type.DynamicsType;
import com.xenoage.zong.io.midi.out.dynamics.type.FixedDynamics;
import com.xenoage.zong.io.midi.out.dynamics.type.GradientDynamics;
import com.xenoage.zong.io.midi.out.repetitions.Repetitions;
import com.xenoage.zong.io.midi.out.score.MeasureBeats;
import com.xenoage.zong.io.midi.out.score.PartStaves;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.val;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.direction.DynamicValue.mf;
import static com.xenoage.zong.core.position.MP.atMeasure;
import static com.xenoage.zong.core.position.Time.time;

/**
 * Finds the {@link Dynamics} in a {@link Score}.
 *
 * Each staff has its own dynamics, represented by {@link DynamicsPeriod}s.
 * Additionally, dynamics may appear in voices. Then, the voice dynamics
 * are valid until the next voice or staff dynamics appears or when the voice ends,
 * i.e. when the following measure does not contain this voice any more.
 *
 * TODO (ZONG-98): Also support dynamics/wedges in column header
 * TODO (ZONG-99): Playback sfz, ftp and other dynamic accents
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DynamicsFinder {

	private final Score score;
	private final DynamicsInterpretation interpretation;
	private final Repetitions repetitions;
	private final MeasureBeats measureBeats;
	private final PartStaves partStaves;

	private DynamicsPeriodsBuilder periods = new DynamicsPeriodsBuilder();


	public static Dynamics findDynamics(
			Score score, DynamicsInterpretation interpretation, Repetitions repetitions) {
		return new DynamicsFinder(score, interpretation, repetitions,
				MeasureBeats.findMeasureBeats(score), PartStaves.findPartStaves(score)).find();
	}

	private Dynamics find() {
		//find dynamics for each staff and repetition
		for (int iStaff : range(score.getStavesCount())) {
			DynamicValue currentValue = mf;
			for (int iRep : range(repetitions.getRepetitions())) {
				currentValue = findStaffDynamics(iStaff, iRep, currentValue);
			}
		}
		return new Dynamics(periods.build(), interpretation, measureBeats, partStaves);
	}

	/**
	 * Walk through the measures of the given staff and repetition and find the dynamics.
	 * Starts with the given initial dynamic. Returns the dynamic value at the end of the
	 * repetition.
	 */
	private DynamicValue findStaffDynamics(int staff, int repetition, DynamicValue startDynamics) {
		val rep = repetitions.getRepetitions().get(repetition);

		//walk through the measures
		Time currentStartTime = rep.start;
		DynamicsType currentDynamic = new FixedDynamics(startDynamics);
		for (int iMeasure : range(rep.start.measure, rep.end.measure)) {
			if (iMeasure >= score.getMeasuresCount())
				break;
			val measure = score.getMeasure(atMeasure(staff, iMeasure));
			if (measure.getDirections() == null)
				continue;
			for (val beat : measure.getDirections().getBeats()) {
				//if beat is out of the repetition range, ignore it
				if ((iMeasure == rep.start.measure && beat.compareTo(rep.start.beat) < 0) ||
						(iMeasure == rep.end.measure && beat.compareTo(rep.end.beat) >= 0))
					continue;
				//wedge ending at this beat?
				//GOON
				//new dynamic starting? then close currently open period and open new one
				val newTime = time(iMeasure, beat);
				DynamicsType newDynamic = getStaffDynamicsAt(staff, newTime, currentDynamic.getEndValue());
				if (newDynamic != null) {
					if (false == currentStartTime.equals(newTime) && false == currentDynamic.equals(newDynamic)) {
						val period = new DynamicsPeriod(currentStartTime, newTime, currentDynamic);
						periods.addPeriodToStaff(period, staff, repetition);
						currentStartTime = newTime;
					}
					currentDynamic = newDynamic;
				}
			}
		}

		//close the dynamics period at the end of the repetition
		val period = new DynamicsPeriod(currentStartTime, rep.end, currentDynamic);
		periods.addPeriodToStaff(period, staff, repetition);

		return currentDynamic.getEndValue();
	}

	/**
	 * Returns the {@link DynamicsType} starting at the given position within a staff (not a voice),
	 * or null, if nothing starts here.
	 */
	@MaybeNull private DynamicsType getStaffDynamicsAt(int staff, Time time, DynamicValue currentDynamicValue) {
		val measure = score.getMeasure(atMeasure(staff, time.measure));

		//are there dynamics at all?
		if (measure.getDirections() == null)
			return null;
		val dirs = measure.getDirections().getAll(time.beat);

		//when there is a Wedge (possible with a Dynamic as the start volume), create
		//a gradient dynamic, when there is only a Dynamic, create a fixed dynamic
		Dynamic foundDynamic = null;
		Wedge foundWedge = null;
		for (val dir : dirs) {
			if (MusicElementType.Dynamic.is(dir))
				foundDynamic = (Dynamic) dir;
			else if (MusicElementType.Wedge.is(dir))
				foundWedge = (Wedge) dir;
		}
		if (foundWedge != null) {
			//gradient
			val startDynamicValue = (foundDynamic != null ? foundDynamic.getValue() : currentDynamicValue);
			val endDynamicValue = startDynamicValue.getWedgeEndValue(foundWedge.getType());
			return new GradientDynamics(startDynamicValue, endDynamicValue); //can be replaced later, when end dynamic is found
		}
		else if (foundDynamic != null) {
			//fixed value
			return new FixedDynamics(foundDynamic.getValue());
		}

		//nothing found
		return null;
	}

}
