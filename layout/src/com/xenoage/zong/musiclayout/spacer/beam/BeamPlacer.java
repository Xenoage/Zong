package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.ArrayUtils.getFirst;
import static com.xenoage.utils.collections.ArrayUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.kernel.Range.rangeReverse;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Hang;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Sit;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Straddle;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.WhiteSpace;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Ascending;
import static com.xenoage.zong.musiclayout.spacer.beam.Direction.Descending;
import static java.lang.Math.abs;
import static java.lang.Math.round;

import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notator.beam.lines.BeamRules;

/**
 * Computes the {@link Placement} of a beam, given its {@link Slant}.
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
public class BeamPlacer {
	
	public static final BeamPlacer beamPlacer = new BeamPlacer();
	
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
	 * @param notesLp        the LP of the inner note of each chord (note at the stem side)
	 * @param stemDir        the stem direction for the whole beam
	 * @param stemsXIs       the horizontal offset of its stem in IS
	 * @param stemsLengthIs  the preferred length of each stem in IS
	 * @param staffLines     the number of staff lines, e.g. 5
	 */
	public Placement compute(Slant slant, int[] notesLp, StemDirection stemDir,
		float[] stemsXIs, float[] stemsLengthIs, int beamLinesCount, StaffLines staffLines) {
		float leftX = getFirst(stemsXIs);
		float rightX = getLast(stemsXIs);
		float slantIs;
		int dictatorIndex;
		Placement candidate;
		//compute default stem end LPs
		float[] stemsEndLp = new float[notesLp.length];
		for (int i : range(notesLp))
			stemsEndLp[i] = notesLp[i] + stemDir.getSign() * stemsLengthIs[i] * 2;
		//try to find the optimum placement
		//start with default stem length of the dictator stem, and try the allowed
		//slants, beginning with the steepest one. if no solution can be found,
		//try with a steeper slant, then with shorter and longer stems
		for (int stemLengthAddQs : stemLengthModLp) { 
			for (int slantAbsQs : rangeReverse(slant.maxAbsQs, slant.minAbsQs)) { //slant in allowed range
				slantIs = slant.direction.getSign() * slantAbsQs / 4f;
				dictatorIndex = getDictatorStemIndex(stemsEndLp, stemsXIs, slantIs, stemDir);
				float dictatorStemEndLp = stemsEndLp[dictatorIndex] + stemDir.getSign() * stemLengthAddQs;
				candidate = getPlacement(leftX, rightX, stemsXIs[dictatorIndex],
					dictatorStemEndLp, slantIs);
				if (isPlacementCorrect(candidate, stemDir, beamLinesCount, staffLines)) {
					//try to shorten
					candidate = shorten(candidate, stemDir, notesLp, stemsXIs, beamLinesCount, staffLines);
					return candidate;
				}
			}
		}
		//no optimal placement could be found. just use the minimum slant
		//and the stem lengths enforced by the dictator stem
		slantIs = slant.getFlattestIs();
		dictatorIndex = getDictatorStemIndex(stemsEndLp, stemsXIs, slantIs, stemDir);
		return getPlacement(leftX, rightX, stemsXIs[dictatorIndex], stemsEndLp[dictatorIndex], slantIs);
	}
	
	/**
	 * Gets the {@link Placement}, rounded to quarter spaces, of the beam which is
	 * placed by the given dictator stem.
	 */
	Placement getPlacement(float leftXIs, float rightXIs, float dictatorXIs,
		float dictatorStemEndLp, float slantIs) {
		float widthIs = rightXIs - leftXIs;
		float slantIsPerIs = slantIs / widthIs;
		//compute exact end LPs
		float leftEndLpExact = dictatorStemEndLp + slantIsPerIs * 2 * (leftXIs - dictatorXIs);
		float rightEndLpExact = dictatorStemEndLp + slantIsPerIs * 2 * (rightXIs - dictatorXIs);
		//round to quarter spaces (both in the same direction!)
		float leftEndLp = round(leftEndLpExact * 2) / 2f;
		float rightEndLp = (int)(rightEndLpExact * 2 + (leftEndLp > leftEndLpExact ? 1 : 0)) / 2f;
		return new Placement(leftEndLp, rightEndLp);
	}
	
	/**
	 * Gets the index of the dictator stem. This is the index of the stem, which
	 * ends at the lowest/highest LP for downstem/upstem beams, including the
	 * given beam slant.
	 */
	int getDictatorStemIndex(float stemsEndLp[], float[] stemsXIs, float slantIs, StemDirection stemDir) {
		int sign = stemDir.getSign();
		float leftX = getFirst(stemsXIs);
		float rightX = getLast(stemsXIs);
		float extremeDistance = (stemDir == Up ? Float.MIN_VALUE : Float.MAX_VALUE);
		int extremeIndex = 0;
		for (int i : range(stemsEndLp)) {
			float distance = getDistanceToLineLp(stemsEndLp[i], stemsXIs[i], slantIs, leftX, rightX);
			if (distance * sign > extremeDistance * sign) {
				extremeDistance = distance;
				extremeIndex = i;
			}
		}
		return extremeIndex;
	}
	
	/**
	 * Gets the vertical distance between the given LP at the given horizontal
	 * position in IS to an imaginary line starting at (lineLeftXIs,0) and
	 * ending at (lineRightXIs,lineSlantIs).
	 * A positive value means, that the lp is above the line.
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
	 * @param totalBeamHeightIs  the total height of the beam lines (including gaps) in IS
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
	Placement shorten(Placement candidate, StemDirection stemDir, int[] notesLp, float[] stemsXIs,
		int beamLinesCount, StaffLines staffLines) {
		//shorten
		Placement shorterCandidate = new Placement(
			candidate.leftEndLp - stemDir.getSign() * 0.5f,
			candidate.rightEndLp - stemDir.getSign() * 0.5f);
		//stems still long enough?
		float slantIs = (shorterCandidate.rightEndLp - shorterCandidate.leftEndLp) / 2;
		BeamRules beamRules = BeamRules.getRules(beamLinesCount);
		for (int iNote : range(notesLp)) {
			float distanceToBeam = abs(getDistanceToLineLp(notesLp[iNote], stemsXIs[iNote],
				slantIs, getFirst(stemsXIs), getLast(stemsXIs)) - shorterCandidate.leftEndLp) / 2;
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
