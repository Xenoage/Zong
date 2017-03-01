package com.xenoage.zong.musiclayout.spacer.beam.placement;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.SLP;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.spacer.beam.Slant;
import com.xenoage.zong.musiclayout.spacer.beam.stem.BeamedStems;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import lombok.AllArgsConstructor;

import static com.xenoage.zong.musiclayout.SLP.slp;
import static com.xenoage.zong.musiclayout.spacer.beam.placement.SingleStaffBeamPlacer.singleStaffBeamPlacer;

/**
 * Computes the {@link Placement} of a beam on two adjacent staffStampings, given its {@link Slant}.
 * 
 * We find the dictator stem of the upper staff (pointing down) and the dictator stem of
 * the lower staff (pointing up). The beam is centered between these positions.
 * 
 * @author Andreas Wenger
 */
public class TwoStavesBeamPlacer {
	
	public static final TwoStavesBeamPlacer twoStavesBeamPlacer = new TwoStavesBeamPlacer();

	/**
	 * Vertical placement of a beam on two adjacent staves,
	 * defined by the {@link SLP}s of the left and the right stem.
	 *
	 * @author Andreas Wenger
	 */
	@Const @AllArgsConstructor
	public static final class Placement {
		public final SLP leftSlp, rightSlp;
	}

	/**
	 * Computes the {@link Placement} of a beam on two adjacent staves.
	 * @param slant          the slant for this beam
	 */
	public Placement compute(Slant slant, BeamedStems stems, BeamNotation beam, SystemSpacing system, int upperStaffIndex) {

		//find the dictator stems of the upper and lower staff
		float slantIs = slant.getMaxIs();
		int upperDictatorStemIndex = singleStaffBeamPlacer.getDictatorStemIndex(StemDirection.Down, stems, slantIs);
		int lowerDictatorStemIndex = singleStaffBeamPlacer.getDictatorStemIndex(StemDirection.Up, stems, slantIs);

		//compute their end positions in mm, to make it independent from the staff (may have different sizes),
		//and place the beam in the middle of these two endpoints
		float upperDictatorYMm = system.getYMm(slp(upperStaffIndex, stems.get(upperDictatorStemIndex).endSlp.lp));
		float lowerDictatorYMm = system.getYMm(slp(upperStaffIndex + 1, stems.get(lowerDictatorStemIndex).endSlp.lp));
		float upperDictatorXMm = stems.get(upperDictatorStemIndex).xIs * system.getInterlineSpace(upperStaffIndex);
		float lowerDictatorXMm = stems.get(lowerDictatorStemIndex).xIs * system.getInterlineSpace(upperStaffIndex + 1);
		float middleXmm = (upperDictatorXMm + lowerDictatorXMm) / 2;
		float middleYMm = (upperDictatorYMm + lowerDictatorYMm) / 2;

		//lengthen the stem by the half of the height of the beam lines, to center the whole beam and not its end
		middleYMm -= stems.primaryStemDir.getSign() * beam.getTotalHeightIs() *
				system.getInterlineSpace(stems.getFirst().noteSlp.staff) / 2;

		//transform the positions in mm into IS/LP makle them staff-dependent again
		float middleXIsFromUpperStaff = middleXmm / system.getInterlineSpace(upperStaffIndex);
		float middleXIsFromLowerStaff = middleXmm / system.getInterlineSpace(upperStaffIndex + 1);
		float middleYLpFromUpperStaff = system.getLp(upperStaffIndex, middleYMm);
		float middleYLpFromLowerStaff = system.getLp(upperStaffIndex + 1, middleYMm);

		//compute the end LPs at the left and right side
		boolean isLeftUpperStaff = (stems.getFirst().dir == StemDirection.Down);
		boolean isRightUpperStaff = (stems.getLast().dir == StemDirection.Down);
		float leftMiddleXIs = (isLeftUpperStaff ? middleXIsFromUpperStaff : middleXIsFromLowerStaff);
		float rightMiddleXIs = (isRightUpperStaff ? middleXIsFromUpperStaff : middleXIsFromLowerStaff);
		float leftYLp = (isLeftUpperStaff ? middleYLpFromUpperStaff : middleYLpFromLowerStaff);
		float rightYLp = (isRightUpperStaff ? middleYLpFromUpperStaff : middleYLpFromLowerStaff);
		float beamWidthIs = stems.rightXIs - stems.leftXIs;
		float leftLp = singleStaffBeamPlacer.getBeamLpAtXIs(stems.leftXIs, leftMiddleXIs, leftYLp, slantIs, beamWidthIs);
		float rightLp = singleStaffBeamPlacer.getBeamLpAtXIs(stems.rightXIs, rightMiddleXIs, rightYLp, slantIs, beamWidthIs);

		int leftStaffIndex = upperStaffIndex + (isLeftUpperStaff ? 0 : 1);
		int rightStaffIndex = upperStaffIndex + (isRightUpperStaff ? 0 : 1);
		return new Placement(slp(leftStaffIndex, leftLp), slp(rightStaffIndex, rightLp));
	}
	
}
