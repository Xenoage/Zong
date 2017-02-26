package com.xenoage.zong.musiclayout.spacer.beam.placement;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notator.beam.lines.BeamRules;
import com.xenoage.zong.musiclayout.spacer.beam.Anchor;
import com.xenoage.zong.musiclayout.spacer.beam.Direction;
import com.xenoage.zong.musiclayout.spacer.beam.Slant;
import com.xenoage.zong.musiclayout.spacer.beam.stem.BeamedStems;
import lombok.Data;
import lombok.val;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.*;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.*;
import static java.lang.Math.abs;
import static java.lang.Math.round;

/**
 * Computes the {@link Placement} of a beam within a single staff, given its {@link Slant}.
 *
 * The rules are adopted from Ross, p. 98-101 and 120-126, and Chlapik, p. 41.
 *
 * When the beam falls within or touches the staff lines, the following rules apply:
 * <ul>
 *   <li>A horizontal beam may straddle, sit on or hang below a staff line (Chlapik p. 41, 3)</li>
 *   <li>For ascending or descending beams (observed, but no explicit rule found)
 *     (H=Hang, S=Sit, St=Straddle):
 *     <table border="1">
 *       <tr>
 *         <td></td>
 *         <th>First stem</th>
 *         <th>Last stem</th>
 *       </tr>
 *       <tr>
 *         <th>Ascending</th>
 *         <td>H, St</td>
 *         <td>S, St</td>
 *       </tr>
 *       <tr>
 *         <th>Descending</th>
 *         <td>S, St</td>
 *         <td>H, St</td>
 *       </tr>
 *     </table>
 *   </li>
 * </ul>
 *
 * These rules ensure, that white wedges within the staff are avoided.
 *
 * If in doubt, a smaller slant may be the better choice, and a slant of one space
 * should not be exceeded (Ross, p. 98). Also, shortening the stems is usually better
 * than lengthening them (Ross, p. 103).
 *
 * TODO (ZONG-92): Support multi-line beams
 *
 * @author Andreas Wenger
 */
public class SingleStaffBeamPlacer {

	public static final SingleStaffBeamPlacer singleStaffBeamPlacer = new SingleStaffBeamPlacer();

	/**
	 * Vertical placement of a single-staff beam, defined by the outer LPs of
	 * the left and the right stem.
	 *
	 * @author Andreas Wenger
	 */
	@Const @Data
	public static final class Placement {
		public final float leftEndLp, rightEndLp;

		public Direction getDirection() {
			float d = rightEndLp - leftEndLp;
			if (d > 0.1)
				return Ascending;
			else if (d < -0.1)
				return Descending;
			else
				return Horizontal;
		}
	}


	//stems at maximum 8 quarter spaces (2 spaces) shorter or longer
	private static final int[] stemLengthModLp = {
			//Ross recommends shortening first on p. 103, last paragraph.
			0, //first, try the perfect solutin
			-1, -2, -3, -4, //then, try to shorten (up to 1 IS)
			+1, +2, +3, +4, //then, try to lengthen (up to 1 IS)
			-5, +5, -6, +6, -7, +7, -8, +8 //if still not found, try to shorten or lengthen up to 2 IS
	};


	/**
	 * Computes the {@link Placement} of a beam within a single staff.
	 * @param slant          the preferred slant for this beam
	 * @param stems          the positions of the stems and their preferred lengths
	 * @param staffLines     the number of staff lines, e.g. 5
	 */
	public Placement compute(Slant slant, BeamedStems stems, int beamLinesCount, StaffLines staffLines) {
		val stemDir = stems.getFirst().dir; //TODO: different stem directions are possible?
		float slantIs;
		int dictatorIndex;
		Placement candidate;
		//try to find the optimum placement
		//start with default stem length of the dictator stem, and try the allowed
		//slants, beginning with the steepest one. if no solution can be found,
		//try with a steeper slant, then with shorter and longer stems
		for (int stemLengthAddQs : stemLengthModLp) {
			for (int slantAbsQs : rangeReverse(slant.maxAbsQs, slant.minAbsQs)) { //slant in allowed range
				slantIs = slant.direction.getSign() * slantAbsQs / 4f;
				dictatorIndex = getDictatorStemIndex(stemDir, stems, slantIs);
				float dictatorStemEndLp = stems.get(dictatorIndex).getEndLp() + stemDir.getSign() * stemLengthAddQs;
				candidate = getPlacement(stems.leftXIs, stems.rightXIs, stems.get(dictatorIndex).xIs,
						dictatorStemEndLp, slantIs);
				if (isPlacementCorrect(candidate, stemDir, beamLinesCount, staffLines)) {
					//try to shorten
					candidate = shorten(candidate, stemDir, stems, beamLinesCount, staffLines);
					return candidate;
				}
			}
		}
		//no optimal placement could be found. just use the minimum slant
		//and the stem lengths enforced by the dictator stem
		slantIs = slant.getFlattestIs();
		dictatorIndex = getDictatorStemIndex(stemDir, stems, slantIs);
		return getPlacement(stems.leftXIs, stems.rightXIs, stems.get(dictatorIndex).xIs,
				stems.get(dictatorIndex).getEndLp(), slantIs);
	}

	/**
	 * Gets the {@link Placement}, rounded to quarter spaces, of the beam which is
	 * placed by the given dictator stem.
	 */
	Placement getPlacement(float leftXIs, float rightXIs, float dictatorXIs,
												 float dictatorStemEndLp, float slantIs) {
		float widthIs = rightXIs - leftXIs;
		//compute exact end LPs
		float leftEndLpExact = getBeamLpAtXIs(leftXIs, dictatorXIs, dictatorStemEndLp, slantIs, widthIs);
		float rightEndLpExact = getBeamLpAtXIs(rightXIs, dictatorXIs, dictatorStemEndLp, slantIs, widthIs);
		//round to quarter spaces (both in the same direction!)
		float leftEndLp = round(leftEndLpExact * 2) / 2f;
		float rightEndLp = (int)(rightEndLpExact * 2 + (leftEndLp > leftEndLpExact ? 1 : 0)) / 2f;
		return new Placement(leftEndLp, rightEndLp);
	}

	/**
	 * Given a beam, defined by a point on the beam (stem end), and its slant and width,
	 * this method computes the LP of the beam (end of the stem) at the given hroizontal position.
	 */
	float getBeamLpAtXIs(float atXIs, float beamPointXIs, float beamPointLp, float slantIs, float beamWidthIs) {
		float slantIsPerIs = slantIs / beamWidthIs;
		return beamPointLp + slantIsPerIs * 2 * (atXIs - beamPointXIs);
	}

	/**
	 * Gets the index of the dictator stem for the given stem direction.
	 * This is the index of the stem, which ends at the lowest/highest LP for downstem/upstem beams,
	 * with respect to the given beam slant.
	 */
	int getDictatorStemIndex(StemDirection forStemDir, BeamedStems stems, float slantIs) {
		int sign = forStemDir.getSign();
		float extremeDistance = (forStemDir == Up ? Float.MIN_VALUE : Float.MAX_VALUE);
		int extremeIndex = 0;
		for (int i : range(stems)) {
			if (stems.get(i).dir == forStemDir) {
				float distance = getDistanceToLineLp(stems.get(i).getEndLp(), stems.get(i).xIs, slantIs,
						stems.leftXIs, stems.rightXIs);
				if (distance * sign > extremeDistance * sign) {
					extremeDistance = distance;
					extremeIndex = i;
				}
			}
		}
		return extremeIndex;
	}

	/**
	 * Gets the vertical distance between the given LP at the given horizontal
	 * position in IS to an imaginary line starting at (lineLeftXIs,0) and
	 * ending at (lineRightXIs,lineSlantIs).
	 * A positive value means, that the layoutPos is above the line.
	 */
	float getDistanceToLineLp(float lp, float xIs, float lineSlantIs,
														float lineLeftXIs, float lineRightXIs) {
		//horizontal position of stem between 0 (left) and 1 (right)
		float t = (xIs - lineLeftXIs) / (lineRightXIs - lineLeftXIs);
		//LP on the line at this position
		float lineLp = t * lineSlantIs * 2;
		//return distance
		return lp - lineLp;
	}

	/**
	 * Returns true, iff the given placement does not violate the rules
	 * listed in the documentation of this class.
	 */
	public boolean isPlacementCorrect(Placement candidate, StemDirection stemDir,
																		int beamLinesCount, StaffLines staffLines) {
		//when the beam does not touch the staff at all, its exact placement
		//does not matter (p. 98; and p. 103, last sentence before the box).
		if (false == isTouchingStaff(candidate, stemDir, BeamNotation.lineHeightIs, staffLines))
			return true;
		//check anchor
		Anchor leftAnchor = Anchor.fromLp(candidate.leftEndLp, stemDir);
		Anchor rightAnchor = Anchor.fromLp(candidate.rightEndLp, stemDir);
		if (beamLinesCount == 1)
			return isAnchor8thCorrect(leftAnchor, rightAnchor, candidate.getDirection());
		else if (beamLinesCount == 2)
			return isAnchor16thCorrect(leftAnchor, rightAnchor, stemDir);
		else
			return isAnchor32ndOrHigherCorrect(leftAnchor, rightAnchor, stemDir);
	}

	/**
	 * Returns true, iff both the left LP and the right LP are completely
	 * outside the staff and do not touch it.
	 * @param beamHeightIs  the total height of the beam lines (including gaps) in IS
	 */
	boolean isTouchingStaff(Placement candidate, StemDirection stemDir,
													float beamHeightIs, StaffLines staffLines) {
		float minDistanceIs = 0.45f; //at least about an half space
		//beam lines above the staff?
		float minLp = staffLines.topLp + minDistanceIs * 2 +
				(stemDir == Up ? beamHeightIs * 2 : 0);
		if (candidate.leftEndLp >= minLp && candidate.rightEndLp >= minLp)
			return false;
		//beam lines below the staff?
		float maxLp = -minDistanceIs * 2 -
				(stemDir == Down ? beamHeightIs * 2 : 0);
		if (candidate.leftEndLp <= maxLp && candidate.rightEndLp <= maxLp)
			return false;
		return true;
	}

	boolean isAnchor8thCorrect(Anchor leftAnchor, Anchor rightAnchor, Direction beamDir) {
		if (beamDir == Ascending) {
			//ascending beam: left may hang or straddle, right may sit or straddle
			if ((leftAnchor == Hang || leftAnchor == Straddle) &&
					(rightAnchor == Sit || rightAnchor == Straddle))
				return true;
		}
		else if (beamDir == Descending) {
			//descending beam: left may sit or straddle, right may hang or straddle
			if ((leftAnchor == Sit || leftAnchor == Straddle) &&
					(rightAnchor == Hang || rightAnchor == Straddle))
				return true;
		}
		else {
			//horizontal beam: both sides may sit, hang or straddle
			if (leftAnchor != WhiteSpace && rightAnchor != WhiteSpace)
				return true;
		}
		//violates the rules
		return false;
	}

	boolean isAnchor16thCorrect(Anchor leftAnchor, Anchor rightAnchor, StemDirection stemDir) {
		//see Ross, p. 120-121
		if (stemDir == Up) {
			//upstem beam: both sides may straddle or hang (Ross, p. 120, section 8, 1)
			if ((leftAnchor == Straddle || leftAnchor == Hang) &&
					(rightAnchor == Straddle || rightAnchor == Hang))
				return true;
		}
		else {
			//downstem beam: both sides may sit or straddle (Ross, p. 121, section 8, 2)
			if ((leftAnchor == Sit || leftAnchor == Straddle) &&
					(rightAnchor == Sit || rightAnchor == Straddle))
				return true;
		}
		//violates the rules
		return false;
	}

	boolean isAnchor32ndOrHigherCorrect(Anchor leftAnchor, Anchor rightAnchor, StemDirection stemDir) {
		//see Ross, p. 125, section 10
		//Beam always hangs (upstem) or sits (downstem), so it fills exactly 2 spaces
		//same for quadruple beams, see Ross p. 125, section 11.
		if (stemDir == Up) {
			//upstem beam: both sides must hang
			if (leftAnchor == Hang & rightAnchor == Hang)
				return true;
		}
		else {
			//downstem beam: both sides must sit
			if (leftAnchor == Sit & rightAnchor == Sit)
				return true;
		}
		//violates the rules
		return false;
	}

	/**
	 * Shortens the stem lengths of the given placement candidate by one quarter space,
	 * if possible and when no stem gets shorter than the {@link BeamRules} allow it.
	 * This rule not found explicitly mentioned by Ross, but applies to many examples and
	 * conforms to the general rule that beamed stems tend to be shortened (p. 103, last
	 * paragraph). See for example:
	 * <ul>
	 * 	<li>p104 r1 c1: could be 3.5/sit, but is 3.25/straddle</li>
	 *  <li>p104 r6 c1: could be 3.75/straddle and 3.5/hang, but is 3.5/sit and 3.25/straddle</li>
	 *  <li>p105 r1 c2: could be 3.5/hang, but is 3.25/staddle</li>
	 * </ul>
	 */
	Placement shorten(Placement candidate, StemDirection stemDir, BeamedStems stems,
										int beamLinesCount, StaffLines staffLines) {
		//shorten
		Placement shorterCandidate = new Placement(
				candidate.leftEndLp - stemDir.getSign() * 0.5f,
				candidate.rightEndLp - stemDir.getSign() * 0.5f);
		//stems still long enough?
		float slantIs = (shorterCandidate.rightEndLp - shorterCandidate.leftEndLp) / 2;
		BeamRules beamRules = BeamRules.getRules(beamLinesCount);
		for (val stem : stems) {
			float distanceToBeam = abs(getDistanceToLineLp(stem.noteLp, stem.xIs,
					slantIs, stems.leftXIs, stems.rightXIs) - shorterCandidate.leftEndLp) / 2;
			if (distanceToBeam < beamRules.getMinimumStemLengthIs())
				return candidate; //shortening not possible
		}
		//edges correct?
		if (isPlacementCorrect(shorterCandidate, stemDir, beamLinesCount, staffLines))
			return shorterCandidate; //success
		else
			return candidate; //shortening not possible
	}


	/**
	 * Returns true, when the lines of the given beam are completely outside the staff
	 * (not even touching a staff line).
	 * @param stemDirection      the direction of the stems
	 * @param firstStemEndLp     the LP of the endpoint of the first stem
	 * @param lastStemEndLp      the LP of the endpoint of the last stem
	 * @param staffLinesCount    the number of staff lines
	 * @param totalBeamHeightIs  the total height of the beam lines (including gaps) in IS
	 * TODO (ZONG-92): use this method for multiline beams to find the smallest
	 * possible line distance
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
