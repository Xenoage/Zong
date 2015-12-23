package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.core.position.MP.getMP;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamPlacer.beamPlacer;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSlanter.beamSlanter;

import java.util.List;

import com.xenoage.utils.collections.ArrayUtils;
import com.xenoage.zong.core.music.Staff;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.ScoreSpacing;
import com.xenoage.zong.musiclayout.spacing.StavesSpacing;

/**
 * Creates the {@link BeamSpacing} for a beam.
 * 
 * GOON: support and test beams spanning over two staves
 * 
 * @author Andreas Wenger
 */
public class BeamSpacer {
	
	public static final BeamSpacer beamSpacer = new BeamSpacer();
	
	public BeamSpacing compute(BeamNotation beam, StavesSpacing staves,
		ScoreSpacing scoreSpacing) {
		int size = beam.chords.size();
		//get LPs of inner notes
		int[] notesLp = new int[size];
		for (int i : range(size))
			notesLp[i] = beam.chords.get(i).getInnerNoteLp();
		//get horizontal position of stems
		ColumnSpacing column = scoreSpacing.getColumn(getMP(beam.chords.get(0).element).measure);
		float[] stemsXIs = new float[size];
		for (int i : range(size)) {
			ElementSpacing cs = column.getElement(beam.chords.get(i).element);
			stemsXIs[i] = getStemXIs(cs);
		}
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
		//compute the ends of the first and last stem
		Placement offset = beamPlacer.compute(slant, notesLp, stemDir, stemsXIs,
			stemsLengthIs, 1, StaffLines.staff5Lines);
		//interpolate the other values
		List<SP> stemsEndSp = alist(size);
		for (int i : range(size))
			stemsEndSp.add(new SP(stemsXIs[i], interpolateLinear(offset.leftEndLp, offset.rightEndLp,
				ArrayUtils.getFirst(stemsXIs), ArrayUtils.getLast(stemsXIs), stemsXIs[i])));
		return new BeamSpacing(beam, stemsEndSp);
	}

	//TIDY: the same logic is elsewhere. should be moved in a ChordSpacing class
	private float getStemXIs(ElementSpacing chord) {
		ChordNotation not = (ChordNotation) chord.notation;
		return chord.getOffsetIs() + not.accidentals.widthIs + not.notes.stemOffsetIs;
	}
	
}
