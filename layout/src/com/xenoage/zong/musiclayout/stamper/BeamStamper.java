package com.xenoage.zong.musiclayout.stamper;

import com.xenoage.utils.math.MathUtils.LinearInterpolationPoints;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.beam.Fragment;
import com.xenoage.zong.musiclayout.notation.beam.Fragments;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.stampings.BeamStamping;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import lombok.val;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.core.music.format.SP.sp;
import static com.xenoage.zong.musiclayout.notation.BeamNotation.hookLengthIs;
import static com.xenoage.zong.musiclayout.notation.BeamNotation.lineHeightIs;
import static com.xenoage.zong.musiclayout.notation.beam.Fragment.*;

/**
 * This strategy creates the {@link BeamStamping}s for a beam.
 * 
 * @author Andreas Wenger
 */
public class BeamStamper {

	public static final BeamStamper beamStamper = new BeamStamper();
	

	/**
	 * Computes the stampings for the given beam and returns them.
	 * @param beam    the beam to stamp
	 * @param staff   the staff stamping of the first chord of the beam
	 */
	public List<BeamStamping> stamp(BeamSpacing beam, StaffStamping staff) {
		
		int beamSize = beam.chords.size();
		val leftEndSp = beam.getLeftSp();
		val rightEndSp = beam.getRightSp();
		val primaryStemDir = beam.getStemDirection(0);
		
		//number of beam lines
		int linesCount = beam.notation.getLinesCount();
		List<BeamStamping> ret = alist(linesCount); //first guess of the size (is correct for simple beams)
		
		//first line (8th line) is always continuous
		val beam8th = new BeamStamping(beam, staff,
			leftEndSp, rightEndSp, primaryStemDir);
		ret.add(beam8th);
		
		//the next lines can be broken, if there are different rhythms or beam subdivisions.
		//this is stored in the notation
		for (int i : range(beam.notation.linesFragments)) {
			int line = i + 1;
			float lineLp = -1 * primaryStemDir.getSign() * (lineHeightIs + beam.notation.gapIs) * 2 * line;
			val beamLinePoints = new LinearInterpolationPoints(
					leftEndSp.lp + lineLp, rightEndSp.lp + lineLp, leftEndSp.xMm, rightEndSp.xMm);
			//create the line stampings
			float startXMm = 0;
			Fragments fragments = beam.notation.linesFragments.get(i);
			for (int iChord : range(fragments)) {
				Fragment fragment = fragments.get(iChord);
				float stemXMm = beam.getStemEndSp(iChord).xMm;
				if (fragment == Start) {
					//begin a new beam line
					startXMm = stemXMm;
				}
				else if (fragment == Stop) {
					//end the beam line and stem it
					float stopXMm = stemXMm;
					SP leftSp = sp(startXMm, INSTANCE.interpolateLinear(beamLinePoints, startXMm));
					SP rightSp = sp(stopXMm, INSTANCE.interpolateLinear(beamLinePoints, stopXMm));
					val stamping = new BeamStamping(beam, staff, leftSp, rightSp, primaryStemDir);
					ret.add(stamping);
				}
				else if (fragment == HookLeft || fragment == HookRight) {
					//left or right hook
					float lengthMm = hookLengthIs * staff.is;
					float x1Mm = (fragment == HookLeft ? stemXMm - lengthMm : stemXMm);
					float x2Mm = (fragment == HookLeft ? stemXMm : stemXMm + lengthMm);
					SP leftSp = sp(x1Mm, INSTANCE.interpolateLinear(beamLinePoints, x1Mm));
					SP rightSp = sp(x2Mm, INSTANCE.interpolateLinear(beamLinePoints, x2Mm));
					val stamping = new BeamStamping(beam, staff, leftSp, rightSp, primaryStemDir);
					ret.add(stamping);
				}
			}
		}

		return ret;
	}

}
