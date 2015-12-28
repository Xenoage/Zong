package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamPlacer.beamPlacer;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSlanter.beamSlanter;
import static com.xenoage.zong.musiclayout.spacing.ChordSpacing.getStemXIs;

import java.util.List;

import com.xenoage.utils.collections.ArrayUtils;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.Notations;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.ElementSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;

/**
 * Creates the {@link BeamSpacing} for a beam.
 * 
 * GOON: support and test beams spanning over two staves
 * 
 * @author Andreas Wenger
 */
public class BeamSpacer {
	
	public static final BeamSpacer beamSpacer = new BeamSpacer();
	
	public BeamSpacing compute(Beam beam, Notations notations, FrameSpacing frameSpacing,
		int staffLinesCount) {
		int size = beam.size();
		BeamNotation beamNot = (BeamNotation) notations.get(beam);
		List<ChordNotation> chords = notations.getBeamChords(beam);
		//get LPs of inner notes
		int[] notesLp = new int[size];
		for (int i : range(size))
			notesLp[i] = chords.get(i).getInnerNoteLp();
		//get horizontal position of stems
		ColumnSpacing column = frameSpacing.getColumn(beam.getMP().measure);
		float[] stemsXIs = new float[size];
		for (int i : range(size)) {
			ElementSpacing cs = column.getElement(chords.get(i).element);
			stemsXIs[i] = getStemXIs(cs);
		}
		//compute slant
		ChordNotation firstChord = getFirst(chords);
		StemDirection stemDir = firstChord.stemDirection; //TODO: stem dirs can be different
		Slant slant = beamSlanter.compute(notesLp, stemDir, stemsXIs, staffLinesCount);
		//get lengths of stems
		float[] stemsLengthIs = new float[size];
		for (int i : range(size))
			stemsLengthIs[i] = chords.get(i).stem.getLengthIs();
		//compute the ends of the first and last stem
		Placement offset = beamPlacer.compute(slant, notesLp, stemDir, stemsXIs,
			stemsLengthIs, 1, StaffLines.staff5Lines);
		//interpolate the other values
		List<SP> stemsEndSp = alist(size);
		List<StemDirection> stemsDirection = alist(size);
		for (int i : range(size)) {
			stemsEndSp.add(new SP(stemsXIs[i], interpolateLinear(offset.leftEndLp, offset.rightEndLp,
				ArrayUtils.getFirst(stemsXIs), ArrayUtils.getLast(stemsXIs), stemsXIs[i])));
			stemsDirection.add(chords.get(i).stemDirection);
		}
		return new BeamSpacing(beamNot, stemsEndSp, stemsDirection);
	}
	
}
