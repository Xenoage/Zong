package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.mod;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;

import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Computes the {@link Placement} of a beam, given its {@link Slant}.
 * 
 * The rules are adopted from Ross, p. 98-101, and Chlapik, p. 41.
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
	public boolean isPlacementCorrect(Placement candidate) {
		if (false == isTouchingStaff(candidate))
			return true;
		for (int side : range(2)) { //0: left stem, 1: right stem
			//
		}
			//TODO: some of the following 4 are possibly not really always 4 but
			//are dependent on the staffLinesCount.
			int linepositionstart = mod((int) (startLp * 2), 4);
			int linepositionend = mod((int) ((startLp + slantIs * 2) * 2), 4);
			if (stemDirection == Down) {
				if (startLp <= 4 && startLp + slantIs * 2 <= 4) {
					//downstems must only straddle the line or sit on it (at the beginning)
					//the end of the stem must not be in the space between two lines
					if (Math.abs(slantIs) < 0.1f) {
						if (linepositionstart == 0 || linepositionstart == 3)
							return true;
					}
					else {
						if (linepositionstart != 1 && linepositionend != 1)
							return true;
					}
				}
			}
			else {
				if (startLp >= 4 && startLp + slantIs * 2 >= 4) {
					if (Math.abs(slantIs) < 0.1f) {
						if (linepositionstart == 0 || linepositionstart == 1)
							return true;
					}
					else {
						if (linepositionstart != 3 && linepositionend != 3)
							return true;
					}
				}
			}
			return false;
	}
	
	/**
	 * Returns true, iff both the left LP and the right LP are completely
	 * outside the staff and do not touch it.
	 * @param totalBeamHeightIs  the total height of the beam lines (including gaps) in IS
	 */ //GOON: test
	boolean isTouchingStaff(Placement candidate, StemDirection stemDir,
		float beamHeightIs, StaffLines staffLines) {
		float minDistanceIs = 0.5f;
		//beam lines above the staff?
		float minLp = staffLines.topLp + minDistanceIs * 2 -
			stemDir.getSign() * beamHeightIs * 2;
		if (candidate.leftEndLp >= minLp && candidate.rightEndLp >= minLp)
			return true;
		//beam lines below the staff?
		float maxLp = -minDistanceIs * 2 +
			stemDir.getSign() * beamHeightIs * 2;
		if (candidate.leftEndLp <= maxLp && candidate.rightEndLp <= maxLp)
			return true;
		return false;
	}
	
}
