package com.xenoage.zong.musiclayout.spacer.beam.placement;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.SLP;
import com.xenoage.zong.musiclayout.spacer.beam.Slant;
import com.xenoage.zong.musiclayout.spacer.beam.stem.BeamedStems;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import lombok.AllArgsConstructor;

import static com.xenoage.zong.musiclayout.SLP.slp;
import static com.xenoage.zong.musiclayout.spacer.beam.placement.SingleStaffBeamPlacer.singleStaffBeamPlacer;

/**
 * Computes the {@link Placement} of a beam on two adjacent staves, given its {@link Slant}.
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
	public Placement compute(Slant slant, BeamedStems stems, SystemSpacing system, int upperStaffIndex) {

		//find the dictator stems of the upper and lower staff
		float slantIs = slant.getMaxIs();
		int upperDictatorStemIndex = singleStaffBeamPlacer.getDictatorStemIndex(StemDirection.Down, stems, slantIs);
		int lowerDictatorStemIndex = singleStaffBeamPlacer.getDictatorStemIndex(StemDirection.Up, stems, slantIs);

		//compute their vertical position in mm, to make it independent from the staff (may have different sizes and so on),
		//and place the beam in the middle of these two values
		//GOON: plus the height of the beam lines!!
		float upperDictatorYMm = system.getYMm(slp(upperStaffIndex, stems.get(upperDictatorStemIndex).getEndLp()));
		float lowerDictatorYMm = system.getYMm(slp(upperStaffIndex + 1, stems.get(lowerDictatorStemIndex).getEndLp()));
		//GOON: we must transform xIS to xMm first, since the staff sizes may be different!
		float middleXIs = (stems.get(upperDictatorStemIndex).xIs + stems.get(lowerDictatorStemIndex).xIs) / 2;
		float middleYMm = (upperDictatorYMm + lowerDictatorYMm) / 2;

		//transform the vertical position of the beam relative to the staves
		float middleYLpFromUpperStaff = system.getLp(upperStaffIndex, middleYMm);
		float middleYLpFromLowerStaff = system.getLp(upperStaffIndex + 1, middleYMm);

		//compute the end LPs at each side
		boolean isLeftUpperStaff = (stems.getFirst().dir == StemDirection.Down);
		boolean isRightUpperStaff = (stems.getLast().dir == StemDirection.Down);
		int leftStaffIndex = upperStaffIndex + (isLeftUpperStaff ? 0 : 1);
		int rightStaffIndex = upperStaffIndex + (isRightUpperStaff ? 0 : 1);
		float leftYLp = (isLeftUpperStaff ? middleYLpFromUpperStaff : middleYLpFromLowerStaff);
		float rightYLp = (isRightUpperStaff ? middleYLpFromUpperStaff : middleYLpFromLowerStaff);
		float beamWidthIs = stems.rightXIs - stems.leftXIs;
		float leftLp = singleStaffBeamPlacer.getBeamLpAtXIs(stems.leftXIs, middleXIs, leftYLp, slantIs, beamWidthIs);
		float rightLp = singleStaffBeamPlacer.getBeamLpAtXIs(stems.rightXIs, middleXIs, rightYLp, slantIs, beamWidthIs);

		return new Placement(slp(leftStaffIndex, leftLp), slp(rightStaffIndex, rightLp));
	}
	
}
