package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.interpolateLinear;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamPlacer.beamPlacer;
import static com.xenoage.zong.musiclayout.spacer.beam.BeamSlanter.beamSlanter;

import java.util.List;

import com.xenoage.utils.collections.ArrayUtils;
import com.xenoage.zong.core.music.StaffLines;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notation.BeamNotation;
import com.xenoage.zong.musiclayout.notation.ChordNotation;
import com.xenoage.zong.musiclayout.spacing.BeamSpacing;
import com.xenoage.zong.musiclayout.spacing.ChordSpacing;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;
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
	
	public BeamSpacing compute(BeamNotation beam, FrameSpacing frameSpacing,
		int staffLinesCount) {
		int size = beam.element.size();
		
		//get LPs of inner notes
		int[] notesLp = new int[size];
		for (int i : range(size))
			notesLp[i] = beam.chords.get(i).getInnerNoteLp();
		
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
		for (int i : range(size))
			stemsLengthIs[i] = beam.chords.get(i).stem.getLengthIs();
		
		//compute the ends of the first and last stem
		Placement offset = beamPlacer.compute(slant, notesLp, stemDir, stemsXIs,
			stemsLengthIs, 1, StaffLines.staff5Lines);
		
		//adjust the stem lengths by interpolating the other values
		for (int i : range(size)) {
			float lp = interpolateLinear(offset.leftEndLp, offset.rightEndLp,
				ArrayUtils.getFirst(stemsXIs), ArrayUtils.getLast(stemsXIs), stemsXIs[i]);
			beam.chords.get(i).stem.endLp = lp;
		}
		
		return new BeamSpacing(beam, chords);
	}
	
}
