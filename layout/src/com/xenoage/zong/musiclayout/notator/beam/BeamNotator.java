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
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.BeamNotation.Waypoint;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
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
