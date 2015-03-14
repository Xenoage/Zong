package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.mod;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Hang;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Sit;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.Straddle;
import static com.xenoage.zong.musiclayout.spacer.beam.Anchor.WhiteSpace;

import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.BeamNotation;

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
 * @author Andreas Wenger
 */
public class BeamPlacer {
	
	public static final BeamPlacer beamOffsetter = new BeamPlacer();
	
	/**
	 * Computes the {@link Placement} of a beam within a single staff.
	 * @param slant          the preferred slant for this beam
	 * @param notesLp        the LP of each inner note (note at the stem side)
	 * @param stemDir        the stem direction for the whole beam
	 * @param stemsXIs       the horizontal offset of its stem in IS
	 * @param stemsLengthIs  the preferred length of each stem in IS
	 * @param staffLines     the number of staff lines, e.g. 5
	 */
	public Placement compute(int[] notesLp, StemDirection stemDir,
		float[] stemsXIs, int beamLinesCount, StaffLines staffLines) {
		//GOON
		return new Placement(0, 0);
	}
	
	/**
	 * Returns true, iff the given placement does not violate the rules
	 * listed in the documentation of this class.
	 */
	public boolean isPlacementCorrect(Placement candidate, StemDirection stemDir,
		int beamLinesCount, StaffLines staffLines) {
		//when the beam does not touch the staff at all, its exact placement
		//does not matter (Ross p. 98)
		if (false == isTouchingStaff(candidate, stemDir, BeamNotation.lineHeightIs, staffLines))
			return true;
		//check anchor
		
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
	
	boolean isAnchor8thCorrect(Anchor leftAnchor, Anchor rightAnchor, Placement candidate) {
		if (candidate.isAscending()) {
			//ascending beam: left may hang or straddle, right may sit or straddle
			if ((leftAnchor == Hang || leftAnchor == Straddle) &&
				(rightAnchor == Sit || rightAnchor == Straddle))
				return true;
		}
		else if (candidate.isDescending()) {
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
	
}
