package com.xenoage.zong.musiclayout.spacer.beam;

import static com.xenoage.utils.collections.ArrayUtils.getFirst;
import static com.xenoage.utils.collections.ArrayUtils.getLast;
import static com.xenoage.utils.collections.ArrayUtils.max;
import static com.xenoage.utils.collections.ArrayUtils.min;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.zong.core.music.chord.StemDirection.Down;
import static com.xenoage.zong.core.music.chord.StemDirection.Up;

import java.util.List;

import com.xenoage.utils.collections.ArrayUtils;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notator.beam.lines.BeamRules;


public class BeamSlanter {
	
	public static final BeamSlanter beamSlanter = new BeamSlanter();
	

	/**
	 * Computes the {@link Slant} of a beam within a single staff.
	 * @param notesLp   for each chord of the beam, the LP of the inner note (note at the stem side)
	 * @param stemDir   the stem direction for the whole beam
	 * @param stemsXIs  for each chord of the beam, the horizontal offset of its stem in IS
	 * @param beamLinesCount   the maximum number of beam lines, e.g. 1 for 8th notes or 2 for 16th
	 * @param staffLinesCount  the number of staff lines, e.g. 5
	 */
	public Slant computeForOneStaff(int[] notesLp, StemDirection stemDir,
		float[] stemsXIs, int beamLinesCount, int staffLinesCount) {
		return new Slant(0, 0);
	}
	
	
	
	
}
