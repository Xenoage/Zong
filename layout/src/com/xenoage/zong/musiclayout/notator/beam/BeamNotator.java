package com.xenoage.zong.musiclayout.notator.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.alistInit;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.zong.core.music.beam.Beam.HorizontalSpan.SingleMeasure;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.SingleStaff;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.TwoAdjacentStaves;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.core.music.util.DurationInfo.getFlagsCount;
import static com.xenoage.zong.musiclayout.notator.beam.range.OneMeasureOneStaff.oneMeasureOneStaff;
import static com.xenoage.zong.musiclayout.notator.beam.range.OneMeasureTwoStaves.oneMeasureTwoStaves;

import java.util.List;

import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.beam.BeamWaypoint;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.musiclayout.notations.BeamNotation;
import com.xenoage.zong.musiclayout.notations.BeamNotation.Waypoint;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.notator.beam.lines.BeamRules;
import com.xenoage.zong.musiclayout.spacing.ScoreSpacing;

/**
 * Computes {@link BeamNotation}s and the {@link StemNotation}s of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
public class BeamNotator {
	
	public static final BeamNotator beamNotator = new BeamNotator();


	public void compute(Beam beam, ScoreSpacing scoreSpacing) {

		//choose appropriate strategy
		if (beam.getHorizontalSpan() == SingleMeasure) {
			if (beam.getVerticalSpan() == SingleStaff) {
				oneMeasureOneStaff.compute(beam, scoreSpacing);
			}
			else if (beam.getVerticalSpan() == TwoAdjacentStaves) {
				oneMeasureTwoStaves.compute(beam, scoreSpacing);
			}
			else {
				throw new IllegalStateException("No strategy for more than two or non-adjacent staves");
			}
		}
		else {
			//Multi-measure beams are not supported yet - TODO
		}
	}
	
	/**
	 * Computes and returns the {@link BeamNotation} for the given beam data.
	 * The {@link BeamNotation} is also set to all given chords of the beam.
	 */
	public static BeamNotation computeBeamNotation(Beam beam, List<ChordNotation> chords,
		SP leftSp, SP rightSp, int beamLinesCount, BeamRules beamDesign) {
		
		//TODO remove this - currently needed for some unit tests
		if (beam == null)
			return null;
		
		List<List<Waypoint>> waypoints = BeamNotator.computeWaypoints(beam);
		float gapIs = beamDesign.getGapIs();
		BeamNotation beamNot = new BeamNotation(beam, leftSp, rightSp, beamLinesCount, waypoints, gapIs);
		for (ChordNotation chord : chords)
			chord.beam = beamNot;
		return beamNot;
	}
	
	/**
	 * Gets the maximum number of beam lines used in the given beam.
	 */
	public static int getMaxLinesCount(Beam beam) {
		List<BeamWaypoint> waypoints = beam.getWaypoints();
		Fraction minDuration = waypoints.get(0).getChord().getDuration();
		for (BeamWaypoint waypoint : waypoints) {
			Fraction duration = waypoint.getChord().getDuration();
			if (duration.compareTo(minDuration) < 0)
				minDuration = duration;
		}
		return DurationInfo.getFlagsCount(minDuration);
	}
	
	/**
	 * Computes the line waypoints for the 16th, 32th, ... line.
	 */
	public static List<List<Waypoint>> computeWaypoints(Beam beam) {
		int linesCount = getMaxLinesCount(beam);
		List<List<Waypoint>> ret = alist(linesCount - 1);
		List<Waypoint> lastWaypoints = null;
		for (int line : rangeReverse(linesCount - 1, 1))
			ret.add(lastWaypoints = computeWaypoints(beam, line, lastWaypoints));
		return ret;
	}
	
	/**
	 * Computes the waypoints for the given line (1: 16th line, 2: 32th line, ...).
	 * Use an algorithm based on the rules in Chlapik, page 45, rule 6.
	 * 
	 * Begin with the highest line (e.g. 32th before 16th), and use the result of line n
	 * as a parameter to compute line n-1 (for the first computation, use null).
	 * This is needed to support Chlapik, page 45, rule 6, example of row 3, column 6.
	 * Without that, the 16th line would go from the second note to the fourth one.
	 */
	static List<Waypoint> computeWaypoints(Beam beam, int line, List<Waypoint> higherLine) {
		if (line < 1)
			throw new IllegalArgumentException("This method only works for 16th lines or higher");
		//in this algorithm, we go from note to note, looking for "groups".
		//groups are consecutive chords/stems with the same number of flags (or
		//a higher number inbetween) and not divided by a subdivision break.
		//initialize return array with none-waypoints
		List<Waypoint> ret = alistInit(Waypoint.None, beam.size());
		int lastFlagsCount = -1;
		int startChord = -1; //start chord of the last group, or -1 if no group is open
		int stopChord = -1; //stop chord of the last group, or -1 if group is open
		for (int iChord : range(beam.size() + 1)) {
			if (iChord < beam.getWaypoints().size()) {
				//another chord within the beam
				Chord chord = beam.getChord(iChord);
				int flagsCount = getFlagsCount(chord.getDuration());
				//enough flags for the given line? (e.g. a 8th beam has no 16th line)
				if (flagsCount >= line + 1) {
					//yes, we need a line of the given line for this stem
					if (startChord == -1) {
						if (higherLine == null || higherLine.get(iChord) != Waypoint.HookLeft) {
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
						beam.isEndOfSubdivision(iChord))) { //forced subdivision break
						//end the group here
						stopChord = iChord - 1;
					}
				}
				else {
					//no, we need no line of the given line for this stem
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
					if (higherLine == null ||
						(higherLine.get(stopChord) != Waypoint.HookRight && higherLine.get(stopChord) != Waypoint.StopHookRight))
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

	
	/**
	 * Returns true, when the lines of the given beam are completely outside the staff
	 * (not even touching a staff line).
	 * @param stemDirection      the direction of the stems
	 * @param firstStemEndLp     the LP of the endpoint of the first stem
	 * @param lastStemEndLp      the LP of the endpoint of the last stem  
	 * @param staffLinesCount    the number of staff lines 
	 * @param totalBeamHeightIs  the total height of the beam lines (including gaps) in IS
	 */
	public boolean isBeamOutsideStaff(StemDirection stemDirection, float firstStemEndLp,
		float lastStemEndLp, int staffLinesCount, float totalBeamHeightIs) {
		float maxStaffLp = (staffLinesCount - 1) * 2;
		if (stemDirection == Up) {
			//beam lines above the staff?
			if (firstStemEndLp > maxStaffLp + totalBeamHeightIs * 2 &&
				lastStemEndLp > maxStaffLp + totalBeamHeightIs * 2) {
				return true;
			}
			//beam lines below the staff?
			if (firstStemEndLp < 0 && lastStemEndLp < 0) {
				return true;
			}
		}
		else if (stemDirection == Down) {
			//beam lines above the staff?
			if (firstStemEndLp > maxStaffLp && lastStemEndLp > maxStaffLp) {
				return true;
			}
			//beam lines below the staff?
			if (firstStemEndLp < -1 * totalBeamHeightIs * 2 &&
				lastStemEndLp < -1 * totalBeamHeightIs * 2) {
				return true;
			}
		}
		return false;
	}

}
