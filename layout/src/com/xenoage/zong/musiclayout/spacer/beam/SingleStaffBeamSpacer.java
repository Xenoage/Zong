package com.xenoage.zong.musiclayout.spacer.beam;

import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.spacer.beam.stem.BeamedStems;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import lombok.val;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.core.music.StaffLines.staffLines;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSpacer.beamSpacer;
import static com.xenoage.zong.musiclayout.spacer.beam.placement.SingleStaffBeamPlacer.singleStaffBeamPlacer;
import static com.xenoage.zong.musiclayout.spacer.beam.slant.SingleStaffBeamSlanter.singleStaffBeamSlanter;

/**
 * Creates the {@link BeamSpacing} for a beam within a single staff.
 *
 * First, the preferred slant is computed. Then, the length of the first and last stem
 * is computed. Finally, the lengths of the inner stems are interpolated.
 *
 * @author Andreas Wenger
 */
public class SingleStaffBeamSpacer {

	static final SingleStaffBeamSpacer singleStaffBeamSpacer = new SingleStaffBeamSpacer();

	BeamSpacing compute(BeamNotation beam, SystemSpacing systemSpacing,
														 int staffLinesCount) {
		int size = beam.element.size();

		//compute slant
		val chords = beamSpacer.getBeamChordSpacings(beam, systemSpacing);
		val stems = BeamedStems.fromBeam(chords);
		val slant = singleStaffBeamSlanter.compute(stems, staffLinesCount);

		//compute the ends of the first and last stem
		val placement = singleStaffBeamPlacer.compute(slant, stems, 1, staffLines(staffLinesCount));

		//adjust the stem lengths by interpolating the other values
		for (int i : range(size)) {
			float lp = interpolateLinear(placement.leftEndLp, placement.rightEndLp,
					stems.leftXIs, stems.rightXIs, stems.get(i).xIs);
			val stem = beam.chords.get(i).stem;
			if (stem != null) //it could be possible that there is no stem
				stem.endSlp = stem.endSlp.withLp(lp);
		}

		return new BeamSpacing(beam, placement.leftEndLp, placement.rightEndLp, chords);
	}

}
