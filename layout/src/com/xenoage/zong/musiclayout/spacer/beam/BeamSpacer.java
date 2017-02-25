package com.xenoage.zong.musiclayout.spacer.beam;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.ChordSpacing;
import com.xenoage.zong.musiclayout.spacing.SystemSpacing;
import lombok.val;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.zong.core.music.beam.Beam.VerticalSpan.TwoAdjacentStaves;
import static com.xenoage.zong.musiclayout.spacer.beam.SingleStaffBeamSpacer.singleStaffBeamSpacer;
import static com.xenoage.zong.musiclayout.spacer.beam.TwoStavesBeamSpacer.twoStavesBeamSpacer;

/**
 * Creates the {@link BeamSpacing} for a beam.
 *
 * TODO: ZONG-108: Beams spanning over multiple measures
 * 
 * @author Andreas Wenger
 */
public class BeamSpacer {

	public static BeamSpacer beamSpacer = new BeamSpacer();


	public BeamSpacing compute(BeamNotation beam, SystemSpacing systemSpacing, Score score) {
		BeamSpacer beamSpacer;

		if (beam.element.getVerticalSpan() == TwoAdjacentStaves) {
			//beam spanning over two staves
			return twoStavesBeamSpacer.compute(beam, systemSpacing);
		}
		else {
			//beam within a single measure, or an even more complicated beam, for which
			//we have no special strategy
			int staff = beam.getMp().staff;
			int staffLinesCount = score.getStaff(staff).getLinesCount();
			return singleStaffBeamSpacer.compute(beam, systemSpacing, staffLinesCount);
		}
	}

	List<ChordSpacing> getBeamChordSpacings(BeamNotation beam, SystemSpacing systemSpacing) {
		val column = systemSpacing.getColumn(beam.mp.measure);
		List<ChordSpacing> chords = alist(beam.element.size());
		for (val chord : beam.chords)
			chords.add((ChordSpacing) column.getElement(chord));
		return chords;
	}

}
