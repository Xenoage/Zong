package com.xenoage.zong.musiclayout.spacer.beam;

import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacer.beam.stem.BeamedStems;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import lombok.val;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSpacer.beamSpacer;
import static com.xenoage.zong.musiclayout.spacer.beam.placement.TwoStavesBeamPlacer.twoStavesBeamPlacer;
import static com.xenoage.zong.musiclayout.spacer.beam.slant.TwoStavesBeamSlanter.twoStavesBeamSlanter;

/**
 * Creates the {@link BeamSpacing} for a beam on two adjacent staves.
 *
 * First, the preferred slant is computed. Then, the length of the first and last stem
 * is computed. Finally, the lengths of the inner stems are interpolated.
 * 
 * @author Andreas Wenger
 */
public class TwoStavesBeamSpacer {
	
	static final TwoStavesBeamSpacer twoStavesBeamSpacer = new TwoStavesBeamSpacer();


	BeamSpacing compute(BeamNotation beam, SystemSpacing systemSpacing) {

		//compute slant
		val slant = twoStavesBeamSlanter.compute(beam);
		//compute the ends of the first and last stem
		val placement = twoStavesBeamPlacer.compute(slant);

		//adjust the stem lengths by interpolating the other values
		//we have to transform the coordinates into mm first and then transform them back to
		//line position, since we may have different staff heights and variable space between the staves
		val chords = beamSpacer.getBeamChordSpacings(beam, systemSpacing);
		val stems = BeamedStems.fromBeam(chords);
		float leftEndYMm = systemSpacing.getYMm(placement.leftSlp);
		float rightEndYMm = systemSpacing.getYMm(placement.rightSlp);
		for (int i : range(stems)) {
			float yMm = interpolateLinear(leftEndYMm, rightEndYMm, stems.leftXIs, stems.rightXIs, stems.get(i).xIs);
			float lp = systemSpacing.getLp(chords.get(i).notation.staff, yMm);
			StemNotation stem = beam.chords.get(i).stem;
			if (stem != null) //it could be possible that there is no stem
				stem.endLp = lp;
		}
		
		return new BeamSpacing(beam, chords);
	}
	
}
