package com.xenoage.zong.musiclayout.layouter.scoreframelayout;

import static com.xenoage.utils.collections.CList.clist;

import java.util.ArrayList;
import java.util.List;

import com.xenoage.utils.collections.CList;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.math.Fraction;
import com.xenoage.utils.math.MathUtils;
import com.xenoage.zong.core.music.VoiceElement;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.musiclayout.layouter.ScoreLayouterStrategy;
import com.xenoage.zong.musiclayout.layouter.cache.util.BeamedStemStampings;
import com.xenoage.zong.musiclayout.layouter.cache.util.OpenBeamMiddleStem;
import com.xenoage.zong.musiclayout.stampings.BeamStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.Stamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

/**
 * This strategy creates the stampings for a beam and
 * its middle stems.
 * 
 * @author Andreas Wenger
 */
public class BeamStampingStrategy
	implements ScoreLayouterStrategy {

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
	 */
	public IList<Stamping> createBeamStampings(BeamedStemStampings beamedStems) {
		//everything needed there?
		if (beamedStems.getLastStem() == null) {
			throw new RuntimeException("Missing end stem for beam beginning at " +
				MP.getMP((VoiceElement) beamedStems.getFirstStem().musicElement));
		}
		//compute beams
		Beam beam = beamedStems.getBeam();
		StaffStamping firstChordStaff = beamedStems.getFirstStem().parentStaff;
		StaffStamping lastChordStaff = beamedStems.getLastStem().parentStaff;
		CList<Stamping> ret = clist();
		float lineHeight = BeamStamping.beamHeight;
		//first level (8th line) is always continuous
		float leftX = beamedStems.getFirstStem().xMm;
		float rightX = beamedStems.getLastStem().xMm;
		float leftLP = beamedStems.getFirstStem().endLp +
			beamedStems.getFirstStem().direction.getSign() * lineHeight / 4; //4: looks ok
		float rightLP = beamedStems.getLastStem().endLp +
			beamedStems.getLastStem().direction.getSign() * lineHeight / 4; //4: looks ok
		BeamStamping beam8th = new BeamStamping(beam, firstChordStaff, lastChordStaff, leftX, rightX,
			leftLP, rightLP);
		ret.add(beam8th);
		//the next levels can be broken, if there are different rhythms or beam subdivisions
		int maxLevel = getMaxLevel(beam);
		List<Waypoint> lastWaypoints = null;
		for (int iLevel = maxLevel; iLevel >= 1; iLevel--) {
			float leveledLP = -1 * beamedStems.getFirstStem().direction.getSign() *
				(BeamStamping.beamHeight + BeamStamping.beamGap) * iLevel * 2; //TODO: find right value (*2 is a hack)
			float leftLeveledLP = leftLP + leveledLP;
			float rightLeveledLP = rightLP + leveledLP;
			//compute the waypoints
			List<Waypoint> waypoints = computeWaypoints(beam, iLevel, lastWaypoints);
			lastWaypoints = waypoints;
			//create the line stampings
			float startX = 0;
			for (int iChord = 0; iChord < waypoints.size(); iChord++) {
				Waypoint wp = waypoints.get(iChord);
				if (wp == Waypoint.Start) {
					//begin a new beam line
					startX = beamedStems.getStemX(iChord);
				}
				else if (wp == Waypoint.Stop || wp == Waypoint.StopHookRight) {
					//end the beam line and stem it
					float stopX = beamedStems.getStemX(iChord) +
						(wp == Waypoint.StopHookRight ? hookLength * firstChordStaff.is : 0);
					BeamStamping line = new BeamStamping(beam, firstChordStaff, lastChordStaff, startX,
						stopX,
						MathUtils.interpolateLinear(leftLeveledLP, rightLeveledLP, leftX, rightX, startX),
						MathUtils.interpolateLinear(leftLeveledLP, rightLeveledLP, leftX, rightX, stopX));
					ret.add(line);
				}
				else if (wp == Waypoint.HookLeft || wp == Waypoint.HookRight) {
					//left hook
					float length = hookLength * firstChordStaff.is;
					float x = beamedStems.getStemX(iChord);
					float x1 = (wp == Waypoint.HookLeft ? x - length : x);
					float x2 = (wp == Waypoint.HookLeft ? x : x + length);
					BeamStamping line = new BeamStamping(beam, firstChordStaff, lastChordStaff, x1, x2,
						MathUtils.interpolateLinear(leftLeveledLP, rightLeveledLP, leftX, rightX, x1),
						MathUtils.interpolateLinear(leftLeveledLP, rightLeveledLP, leftX, rightX, x2));
					ret.add(line);
				}
			}
		}

		//middle stems
		for (OpenBeamMiddleStem openStem : beamedStems.getMiddleStems()) {
			float stemX = openStem.positionX;
			float f = (stemX - leftX) / (rightX - leftX);
			float endLP = 0;
			if (beam.getVerticalSpan() == VerticalSpan.SingleStaff) {
				//single staff beam: LPs are easy to compute
				endLP = leftLP + f * (rightLP - leftLP);
			}
			else if (beam.getVerticalSpan() == VerticalSpan.TwoAdjacentStaves) {
				//two staff beam: LPs are more complicated to compute. we have first to translate
				//the beam in absolute frame coordinates, then we have to translate it into the
				//coordinates of the parent staff of the current stem
				float leftStemEndMm = beamedStems.getFirstStem().parentStaff.computeYMm(leftLP);
				float rightStemEndMm = beamedStems.getLastStem().parentStaff.computeYMm(rightLP);
				float endMm = leftStemEndMm + f * (rightStemEndMm - leftStemEndMm);
				endLP = openStem.staff.computeYLP(endMm);
			}
			float startLP = (endLP > openStem.topNoteLP ? openStem.bottomNoteLP : openStem.topNoteLP);
			StemStamping stem = new StemStamping(openStem.staff, openStem.chord, stemX, startLP, endLP,
				openStem.stemDirection);
			ret.add(stem);
		}

		return ret.close();
	}

	/**
	 * Gets the highest level of this beam. This is 0 for 8th beams,
	 * 1 for beams which contain up to 16th notes, 2 for 3th notes and so on.
	 */
	int getMaxLevel(Beam beam) {
		int maxLevel = 0;
		for (BeamWaypoint bw : beam.getWaypoints()) {
			int level = DurationInfo.getFlagsCount(bw.getChord().getDuration()) - 1;
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
