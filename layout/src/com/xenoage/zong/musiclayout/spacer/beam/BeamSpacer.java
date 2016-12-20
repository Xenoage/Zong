package com.xenoage.zong.musiclayout.spacer.beam;

import com.xenoage.utils.collections.ArrayUtils;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.notation.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.ChordSpacing;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
import com.xenoage.zong.musiclayout.spacing.FrameSpacing;

import java.util.List;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamPlacer.beamPlacer;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSlanter.beamSlanter;
import static java.lang.Math.abs;

/**
 * Creates the {@link BeamSpacing} for a beam.
 * 
 * TODO (ZONG-93): support and test beams spanning over two staves
 * TODO: support beams spanning over more than a measure
 * 
 * @author Andreas Wenger
 */
public class BeamSpacer {
	
	public static final BeamSpacer beamSpacer = new BeamSpacer();
	
	public BeamSpacing compute(BeamNotation beam, FrameSpacing frameSpacing,
		int staffLinesCount) {
		int size = beam.element.size();
		
		//get LPs of stem-side notes
		int[] notesLp = new int[size];
		for (int i : range(size))
			notesLp[i] = beam.chords.get(i).getStemSideNoteLp();
		
		//get chord spacings
		ColumnSpacing column = frameSpacing.getColumn(beam.element.getMP().measure);
		List<ChordSpacing> chords = alist(size);
		float[] stemsXIs = new float[size];
		for (int i : range(size)) {
			ChordSpacing cs = (ChordSpacing) column.getElement(beam.chords.get(i).element);
			chords.add(cs);
			stemsXIs[i] = cs.getStemXIs();
		}
		
		//compute slant
		ChordNotation firstChord = getFirst(beam.chords);
		StemDirection stemDir = firstChord.stemDirection; //TODO: stem dirs can be different
		Slant slant = beamSlanter.compute(notesLp, stemDir, stemsXIs, staffLinesCount);
		
		//get lengths of stems
		float[] stemsLengthIs = new float[size];
		for (int i : range(size)) {
			ChordNotation chord = beam.chords.get(i);
			if (chord.stem != null)
				stemsLengthIs[i] = abs(chord.stem.endLp - chord.getStemSideNoteLp()) / 2;
			else
				stemsLengthIs[i] = 3; //if there is no stem (should not happen very often)
		}
			
		//compute the ends of the first and last stem
		Placement offset = beamPlacer.compute(slant, notesLp, stemDir, stemsXIs,
			stemsLengthIs, 1, StaffLines.staff5Lines);
		
		//adjust the stem lengths by interpolating the other values
		for (int i : range(size)) {
			float lp = interpolateLinear(offset.leftEndLp, offset.rightEndLp,
				ArrayUtils.getFirst(stemsXIs), ArrayUtils.getLast(stemsXIs), stemsXIs[i]);
			StemNotation stem = beam.chords.get(i).stem;
			if (stem != null) //it could be possible that there is no stem
				stem.endLp = lp;
		}
		
		return new BeamSpacing(beam, chords);
	}
	
}
