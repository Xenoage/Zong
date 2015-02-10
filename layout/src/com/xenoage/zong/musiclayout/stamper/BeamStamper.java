package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.kernel.Range;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.layouter.cache.util.BeamedStemStampings;
import com.xenoage.zong.musiclayout.stampings.BeamStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

/**
 * This strategy creates the stampings for a beam and
 * adjust the length of the middle stem stampings.
 * 
 * @author Andreas Wenger
 */
public class BeamStamper {

	public static final BeamStamper beamStamper = new BeamStamper();
	
	private static final float hookLength = 1.25f; //Chlapik TODO


	enum Waypoint {
		None,
		Start,
		Stop,
		HookLeft,
		HookRight,
		StopHookRight
	}


	/**
	 * Computes the stampings for the given beam and returns them.
	 * The middle {@link StemStamping}s are modified.
	 */
	public BeamStamping[] createBeamStampings(BeamedStemStampings beamedStems) {
		beamedStems.checkComplete();
		Beam beam = beamedStems.beam;
		StemStamping firstStem = beamedStems.firstStem();
		StemStamping lastStem = beamedStems.lastStem();
		StaffStamping leftStaff = firstStem.parentStaff;
		StaffStamping rightStaff = lastStem.parentStaff;
		float leftX = firstStem.xMm;
		float rightX = lastStem.xMm;
		float lineHeight = BeamStamping.beamHeight;
		//number of beam levels
		int levels = getLevels(beam);
		BeamStamping[] ret = new BeamStamping[levels];
		
		//first level (8th line) is always continuous
		float leftLp = firstStem.endLp + firstStem.direction.getSign() * lineHeight / 4; //4: looks ok
		float rightLp = lastStem.endLp + lastStem.direction.getSign() * lineHeight / 4; //4: looks ok
		BeamStamping beam8th = new BeamStamping(beam, leftStaff, rightStaff, leftX, rightX,
			leftLp, rightLp);
		ret[0] = beam8th;
		
		//the next levels can be broken, if there are different rhythms or beam subdivisions
		List<Waypoint> lastWaypoints = null;
		for (int i : range(levels - 1)) {
			int level = levels - i + 1;
			//TODO: use BeamNotation here!!!
			float leveledLp = -1 * firstStem.direction.getSign() *
				(BeamStamping.beamHeight + BeamStamping.beamGap) * level * 2; //TODO: find right value (*2 is a hack)
			float leftLeveledLp = leftLp + leveledLp;
			float rightLeveledLp = rightLp + leveledLp;
			//compute the waypoints
			List<Waypoint> waypoints = computeWaypoints(beam, level, lastWaypoints);
			lastWaypoints = waypoints;
			//create the line stampings
			float startX = 0;
			for (int iChord : Range.range(waypoints)) {
				Waypoint wp = waypoints.get(iChord);
				float stemX = beamedStems.stems[iChord].xMm;
				if (wp == Waypoint.Start) {
					//begin a new beam line
					startX = stemX;
				}
				else if (wp == Waypoint.Stop || wp == Waypoint.StopHookRight) {
					//end the beam line and stem it
					float stopX = stemX +
						(wp == Waypoint.StopHookRight ? hookLength * leftStaff.is : 0);
					BeamStamping line = new BeamStamping(beam, leftStaff, rightStaff, startX, stopX,
						interpolateLinear(leftLeveledLp, rightLeveledLp, leftX, rightX, startX),
						interpolateLinear(leftLeveledLp, rightLeveledLp, leftX, rightX, stopX));
					ret[i+1] = line;
				}
				else if (wp == Waypoint.HookLeft || wp == Waypoint.HookRight) {
					//left hook
					float length = hookLength * leftStaff.is;
					float x1 = (wp == Waypoint.HookLeft ? stemX - length : stemX);
					float x2 = (wp == Waypoint.HookLeft ? stemX : stemX + length);
					BeamStamping line = new BeamStamping(beam, leftStaff, rightStaff, x1, x2,
						interpolateLinear(leftLeveledLp, rightLeveledLp, leftX, rightX, x1),
						interpolateLinear(leftLeveledLp, rightLeveledLp, leftX, rightX, x2));
					ret[i+1] = line;
				}
			}
		}

		//update middle stems
		for (int i : range(1, beamedStems.stems.length - 2)) {
			StemStamping openStem = beamedStems.stems[i];
			float stemX = openStem.xMm;
			float f = (stemX - leftX) / (rightX - leftX);
			float endLp = 0;
			if (beam.getVerticalSpan() == VerticalSpan.SingleStaff) {
				//single staff beam: LPs are easy to compute
				endLp = leftLp + f * (rightLp - leftLp);
			}
			else if (beam.getVerticalSpan() == VerticalSpan.TwoAdjacentStaves) {
				//two staff beam: LPs are more complicated to compute. we have first to translate
				//the beam in absolute frame coordinates, then we have to translate it into the
				//coordinates of the parent staff of the current stem
				float leftStemEndMm = leftStaff.computeYMm(leftLp);
				float rightStemEndMm = rightStaff.computeYMm(rightLp);
				float endMm = leftStemEndMm + f * (rightStemEndMm - leftStemEndMm);
				endLp = openStem.parentStaff.computeYLP(endMm);
			}
			openStem.endLp = endLp;
		}

		return ret;
	}

	/**
	 * Gets the number of levels of this beam. This is 1 for 8th beams,
	 * 2 for beams which contain up to 16th notes, 3 for 3th notes and so on.
	 */
	int getLevels(Beam beam) {
		int maxLevel = 0;
		for (BeamWaypoint bw : beam.getWaypoints()) {
			int level = DurationInfo.getFlagsCount(bw.getChord().getDuration());
			maxLevel = Math.max(level, maxLevel);
		}
		return maxLevel;
	}

	/**
	 * Computes the waypoints for the given level (1: 16th line, 2: 32th line, ...).
	 * Use an algorithm based on the rules in Chlapik, page 45, rule 6.
	 * 
	 * Begin with the highest level, and use the result of level n as a parameter
	 * to compute level n-1 (for the first computation, use null). This is needed
	 * to support Chlapik, page 45, rule 6, example of row 3, column 6. Without
	 * that, the 16th line would go from the second note to the fourth one.
	 */
	List<Waypoint> computeWaypoints(Beam beam, int level, List<Waypoint> higherLevel) {
		if (level < 1)
			throw new IllegalArgumentException("This method only works for 16th lines or higher");
		//in this algorithm, we go from note to note, looking for "groups".
		//groups are consecutive chords/stems with the same number of flags (or
		//a higher number inbetween) and not divided by a subdivision break.
		//initialize return array with none-waypoints
		List<Waypoint> ret = new ArrayList<Waypoint>(beam.getWaypoints().size());
		for (int i = 0; i < beam.getWaypoints().size(); i++) {
			ret.add(Waypoint.None);
		}
		int lastFlagsCount = -1;
		int startChord = -1; //start chord of the last group, or -1 if no group is open
		int stopChord = -1; //stop chord of the last group, or -1 if group is open
		for (int iChord = 0; iChord <= beam.getWaypoints().size(); iChord++) {
			if (iChord < beam.getWaypoints().size()) {
				//another chord within the beam
				Chord chord = beam.getWaypoints().get(iChord).getChord();
				int flagsCount = DurationInfo.getFlagsCount(chord.getDuration());
				//enough flags for the given level? (e.g. a 8th beam has no 16th line)
				if (flagsCount >= level + 1) {
					//yes, we need a line of the given level for this stem
					if (startChord == -1) {
						if (higherLevel == null || higherLevel.get(iChord) != Waypoint.HookLeft) {
							//start new group
							startChord = iChord;
							lastFlagsCount = flagsCount;
						}
						else {
							//example mentioned in the method documentation (Chlapik, page 45, row 3, col 6)
							//we place a hook. this is not explicitly mentioned in the text, but seems to
							//be right when looking at the example.
							startChord = iChord;
							stopChord = iChord;
						}
					}
					else if (lastFlagsCount > -1 && (flagsCount < flagsCount || //less flags than previous stem
						beam.isEndOfSubdivision(iChord))) //forced subdivision break
					{
						//end the group here
						stopChord = iChord - 1;
					}
				}
				else {
					//no, we need no line of the given level for this stem
					//so, close the last group
					stopChord = iChord - 1;
				}
			}
			else {
				//no more chord in the beam, so we have to close
				stopChord = iChord - 1;
			}
			//if a group was closed, create it
			if (startChord > -1 && stopChord > -1) {
				//type of line is dependent on number of chords in the group
				int chordsCount = stopChord - startChord + 1;
				if (chordsCount > 1) {
					//simple case: more than one chord. create a normal line
					//between those stems
					ret.set(startChord, Waypoint.Start);
					if (higherLevel == null ||
						(higherLevel.get(stopChord) != Waypoint.HookRight && higherLevel.get(stopChord) != Waypoint.StopHookRight))
						ret.set(stopChord, Waypoint.Stop);
					else
						ret.set(stopChord, Waypoint.StopHookRight); //extend the line to the above hook
				}
				else {
					//more difficult case: exactly one chord.
					if (startChord == 0) {
						//first chord in beam has always hook to the right
						ret.set(startChord, Waypoint.HookRight);
					}
					else if (startChord == beam.getWaypoints().size() - 1) {
						//last chord in beam has always hook to the left
						ret.set(startChord, Waypoint.HookLeft);
					}
					else {
						//middle chords have left hook, if the preceding chord
						//has a longer or equal duration than the following chord,
						//otherwise they have a right hook
						Fraction left = beam.getChord(startChord - 1).getDuration();
						Fraction right = beam.getChord(startChord + 1).getDuration();
						if (left.compareTo(right) >= 0)
							ret.set(startChord, Waypoint.HookLeft);
						else
							ret.set(startChord, Waypoint.HookRight);
					}
				}
				//reset group data
				startChord = -1;
				stopChord = -1;
				lastFlagsCount = -1;
			}
		}
		return ret;
	}

}
