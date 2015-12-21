package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamPlacer.beamPlacer;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSlanter.beamSlanter;

import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.StavesSpacing;

/**
 * Creates the {@link BeamSpacing} for a beam.
 * 
 * @author Andreas Wenger
 */
public class BeamSpacer {
	
	public static final BeamSpacer beamSpacer = new BeamSpacer();
	
	public BeamSpacing compute(BeamNotation beam, StavesSpacing staves) {
		int size = beam.chords.size();
		//get LPs of inner notes
		//GOON
		//get staves
		ChordNotation firstChord = getFirst(beam.chords);
		int firstStaffIndex = getMP(firstChord.element).staff;
		Staff firstStaff = staves.getStaves().get(firstStaffIndex);
		//compute slant
		StemDirection stemDir = firstChord.stemDirection; //TODO: stem dirs can be different
		Slant slant = beamSlanter.compute(notesLp, stemDir, stemsXIs, firstStaff.getLinesCount());
		//get lengths of stems
		float[] stemsLengthIs = new float[size];
		for (int i : range(size))
			stemsLengthIs[i] = beam.chords.get(i).stem.getLengthIs();
		//compute stem ends
		Placement offset = beamPlacer.compute(slant, notesLp, stemDir, stemsXIs,
			stemsLengthIs, 1, StaffLines.staff5Lines);
		return new BeamSpacing(beam, stemEndSps);
	}

}
