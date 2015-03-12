package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.musiclayout.notations.BeamNotation.hookLengthIs;
import static com.xenoage.zong.musiclayout.notations.BeamNotation.lineHeightIs;

import java.util.List;

import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.layouter.cache.util.BeamedStemStampings;
import com.xenoage.zong.musiclayout.notations.BeamNotation;
import com.xenoage.zong.musiclayout.notations.BeamNotation.Waypoint;
import com.xenoage.zong.musiclayout.stampings.BeamStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import com.xenoage.zong.musiclayout.stampings.StemStamping;

/**
 * This strategy creates the stampings for a beam.
 * 
 * @author Andreas Wenger
 */
public class BeamStamper {

	public static final BeamStamper beamStamper = new BeamStamper();
	

	/**
	 * Computes the stampings for the given beam and returns them. TIDY
	 */
	public BeamStamping[] createBeamStampings(BeamNotation beam) {
		
		/*
		float leftX = beam.leftSp.xMm;
		float rightX = beam.rightSp.xMm;
		//number of beam levels
		int levels = beam.linesCount;
		BeamStamping[] ret = new BeamStamping[levels];
		
		//first line (8th line) is always continuous
		float leftLp = beam.leftSp.lp + leftStemDir.getSign() * lineHeightIs / 4; //4: looks ok
		float rightLp = beam.rightSp.lp + rightStemDir.getSign() * lineHeightIs / 4; //4: looks ok
		BeamStamping beam8th = new BeamStamping(beam, leftChordStaff, rightChordStaff,
			sp(leftX, leftLp), sp(rightX, rightLp));
		ret[0] = beam8th;
		
		//the next lines can be broken, if there are different rhythms or beam subdivisions
		//this is stored in the notation
		for (int i : range(beam.waypoints)) {
			int line = i + 1;
			float lineLp = -1 * leftStemDir.getSign() * (lineHeightIs + beam.gapIs) * 2 * line;
			float leftLineLp = leftLp + lineLp;
			float rightLineLp = rightLp + lineLp;
			//create the line stampings
			float startX = 0;
			List<Waypoint> waypoints = beam.waypoints.get(i);
			for (int iChord : range(waypoints)) {
				Waypoint wp = waypoints.get(iChord);
				float stemX = beamedStems.stems[iChord].xMm;
				if (wp == Waypoint.Start) {
					//begin a new beam line
					startX = stemX;
				}
				else if (wp == Waypoint.Stop || wp == Waypoint.StopHookRight) {
					//end the beam line and stem it
					float stopX = stemX +
						(wp == Waypoint.StopHookRight ? hookLengthIs * leftStaff.is : 0);
					SP leftSp = sp(startX, interpolateLinear(leftLineLp, rightLineLp, leftX, rightX, startX));
					SP rightSp = sp(stopX, interpolateLinear(leftLineLp, rightLineLp, leftX, rightX, stopX));
					BeamStamping stamping = new BeamStamping(beam, leftStaff, rightStaff, leftSp, rightSp);
					ret[i+1] = stamping;
				}
				else if (wp == Waypoint.HookLeft || wp == Waypoint.HookRight) {
					//left hook
					float length = hookLengthIs * leftStaff.is;
					float x1 = (wp == Waypoint.HookLeft ? stemX - length : stemX);
					float x2 = (wp == Waypoint.HookLeft ? stemX : stemX + length);
					SP leftSp = sp(x1, interpolateLinear(leftLineLp, rightLineLp, leftX, rightX, x1));
					SP rightSp = sp(x2, interpolateLinear(leftLineLp, rightLineLp, leftX, rightX, x2));
					BeamStamping stamping = new BeamStamping(beam, leftStaff, rightStaff, leftSp, rightSp);
					ret[i+1] = stamping;
				}
			}
		} */

		return new BeamStamping[0];
	}

}
