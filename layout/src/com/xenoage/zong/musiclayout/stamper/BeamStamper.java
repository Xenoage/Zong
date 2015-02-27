package com.xenoage.zong.musiclayout.stamper;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.musiclayout.notations.BeamNotation.hookLengthIs;
import static com.xenoage.zong.musiclayout.notations.BeamNotation.lineHeightIs;

import java.util.List;

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
	 * Computes the stampings for the given beam and returns them.
	 * The middle {@link StemStamping}s are modified.
	 */
	public BeamStamping[] createBeamStampings(BeamedStemStampings beamedStems) {
		BeamNotation beam = beamedStems.beam;
		StemStamping firstStem = beamedStems.firstStem();
		StemStamping lastStem = beamedStems.lastStem();
		StaffStamping leftStaff = firstStem.parentStaff;
		StaffStamping rightStaff = lastStem.parentStaff;
		float leftX = firstStem.xMm;
		float rightX = lastStem.xMm;
		//number of beam levels
		int levels = beam.linesCount;
		BeamStamping[] ret = new BeamStamping[levels];
		
		//first line (8th line) is always continuous
		float leftLp = firstStem.endLp + firstStem.direction.getSign() * lineHeightIs / 4; //4: looks ok
		float rightLp = lastStem.endLp + lastStem.direction.getSign() * lineHeightIs / 4; //4: looks ok
		BeamStamping beam8th = new BeamStamping(beam, leftStaff, rightStaff,
			sp(leftX, leftLp), sp(rightX, rightLp));
		ret[0] = beam8th;
		
		//the next lines can be broken, if there are different rhythms or beam subdivisions
		//this is stored in the notation
		for (int i : range(beam.waypoints)) {
			int line = i + 1;
			float lineLp = -1 * firstStem.direction.getSign() *
				(lineHeightIs + beam.gapIs) * 2 * line;
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
		}

		return ret;
	}

}
