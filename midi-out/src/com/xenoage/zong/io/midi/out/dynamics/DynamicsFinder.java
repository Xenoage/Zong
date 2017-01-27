package com.xenoage.zong.io.midi.out.dynamics;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.music.direction.Dynamic;
import com.xenoage.zong.core.music.direction.DynamicValue;
import com.xenoage.zong.core.music.direction.Wedge;
import com.xenoage.zong.core.music.direction.WedgeEnd;
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
 * TODO (ZONG-100): Playback dynamics in voices
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
		//staves: find dynamics for each staff and repetition
		for (int iStaff : range(score.getStavesCount())) {
			DynamicValue currentValue = mf;
			for (int iRep : range(repetitions)) {
				currentValue = findStaffDynamics(iStaff, iRep, currentValue);
			}
		}
		//voices: find dynamics for each staff, voice and repetition
		for (int iStaff : range(score.getStavesCount())) {
			for (int iVoice : range(score.getStaff(iStaff).getVoicesCount())) {
				for (int iRep : range(repetitions)) {
					//TODO: ZONG-100 findVoiceDynamics(iStaff, iVoice, iRep);
				}
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
		val rep = repetitions.get(repetition);

		//walk through the measures
		Time currentStartTime = rep.start;
		DynamicsType currentDynamic = new FixedDynamics(startDynamics);
		for (int iMeasure : range(rep.start.measure, rep.end.measure)) {
			if (iMeasure >= score.getMeasuresCount())
				break;
			val measure = score.getMeasure(atMeasure(staff, iMeasure));
			for (val beat : measure.getDirections().getBeats()) {
				val newTime = time(iMeasure, beat);
				DynamicsType newDynamic = null;

				//if beat is out of the repetition range, ignore it
				if (false == rep.contains(newTime))
					continue;

				//find dynamics change
				if (currentDynamic instanceof GradientDynamics) {
					//wedge ending at this beat?
					val closedWedge = getWedgeEndAt(staff, newTime, (GradientDynamics) currentDynamic);
					if (closedWedge != null) {
						currentDynamic = closedWedge;
						newDynamic = new FixedDynamics(closedWedge.end); //continue with the end dynamic of the wedge
					}
				}
				else if (newDynamic == null) {
					//new dynamic starting? then close currently open period and open new one
					newDynamic = getStaffDynamicStartAt(staff, newTime, currentDynamic.getEndValue());
				}
				//when change was found, apply it
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
	 * Returns the {@link DynamicsType} starting at the given time within the given staff (not a voice),
	 * or null, if nothing starts here.
	 */
	@MaybeNull private DynamicsType getStaffDynamicStartAt(int staff, Time time, DynamicValue currentDynamicValue) {
		val measure = score.getMeasure(atMeasure(staff, time.measure));
		//when there is a Wedge (possible with a Dynamic as the start volume), create
		//a gradient dynamic, when there is only a Dynamic, create a fixed dynamic
		val foundDynamic = (Dynamic) measure.getDirections().get(time.beat, MusicElementType.Dynamic);
		val foundWedge = (Wedge) measure.getDirections().get(time.beat, MusicElementType.Wedge);
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
		return null;
	}

	/**
	 * Checks, if there is a {@link WedgeEnd} at the given time within the given staff (not a voice).
	 * If yes, the given open wedge is closed and returned.
	 */
	@MaybeNull private GradientDynamics getWedgeEndAt(int staff, Time time, GradientDynamics openWedge) {
		val measure = score.getMeasure(atMeasure(staff, time.measure));
		val foundDynamic = (Dynamic) measure.getDirections().get(time.beat, MusicElementType.Dynamic);
		val foundWedgeEnd = (WedgeEnd) measure.getDirections().get(time.beat, MusicElementType.WedgeEnd);
		if (foundWedgeEnd != null) {
			val endDynamicValue = (foundDynamic != null ? foundDynamic.getValue() : openWedge.end);
			return new GradientDynamics(openWedge.start, endDynamicValue);
		}
		return null;
	}

}
