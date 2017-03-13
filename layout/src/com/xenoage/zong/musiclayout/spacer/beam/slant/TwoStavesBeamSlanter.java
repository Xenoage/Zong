package com.xenoage.zong.musiclayout.spacer.beam.slant;

import com.xenoage.zong.core.music.clef.ClefType;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.spacer.beam.Slant;
import lombok.val;

import static com.xenoage.utils.collections.ArrayUtils.getFirst;
import static com.xenoage.utils.collections.ArrayUtils.getLast;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.musiclayout.spacer.beam.Slant.horizontalSlant;
import static com.xenoage.zong.musiclayout.spacer.beam.slant.SingleStaffBeamSlanter.singleStaffBeamSlanter;

/**
 * Computes the {@link Slant} of a beam that spans over two adjacent staves.
 * 
 * The beam line is drawn between the staves, so that this class expects all stems
 * of the upper staff to point down, while the stems of the lower staff must point up.
 *
 * For the slant, we use a simple subset of the rules of the single staff beam,
 * pretending the beam chords to be placed on a single staff instead of two ones.
 * 
 * @author Andreas Wenger
 */
public class TwoStavesBeamSlanter {
	
	public static final TwoStavesBeamSlanter twoStavesBeamSlanter = new TwoStavesBeamSlanter();


	public Slant compute(BeamNotation beam) {
		//compute LPs, all within a single staff. imagine a beam on two staves (g- and f-clef).
		//the first note d1 on the upper staff and the second note c1 on the lower staff.
		//the c1 has a much higher LP due to the f-clef, but in the musical sense, it is a lower note,
		//so we expect the beam to fall. we find the musical direction by positioning all notes on a
		//single staff (here, by imagining a g-clef staff).
		int[] singleStaffLps = new int[beam.chords.size()];
		val clef = ClefType.clefTreble;
		for (int i : range(singleStaffLps)) {
			val pitch =  beam.chords.get(i).getStemSideNotePitch();
			singleStaffLps[i] = clef.getLp(pitch);
		}
		return compute(singleStaffLps);
	}

	/**
	 * This method contains a subset of the rules defined in {@link SingleStaffBeamSlanter}.
	 */
	public Slant compute(int... singleStaffLps) {
		int leftLp = getFirst(singleStaffLps);
		int rightLp = getLast(singleStaffLps);
		//Ross, p. 115, row 1: use horizontal beam, if first and last note is on the same LP
		if (leftLp == rightLp)
			return horizontalSlant;
		//otherwise normal slanting
		return singleStaffBeamSlanter.computeNormal(leftLp, rightLp);
	}
	
}
