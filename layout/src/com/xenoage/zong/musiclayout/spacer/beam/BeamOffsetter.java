package com.xenoage.zong.musiclayout.spacer.beam;

import com.xenoage.zong.core.music.chord.StemDirection;


public class BeamOffsetter {
	
	public static final BeamOffsetter beamOffsetter = new BeamOffsetter();
	

	/**
	 * Computes the {@link Offset} of a beam within a single staff.
	 * @param notesLp   for each chord of the beam, the LP of the inner note (note at the stem side)
	 * @param stemDir   the stem direction for the whole beam
	 * @param stemsXIs  for each chord of the beam, the horizontal offset of its stem in IS
	 * @param beamLinesCount   the maximum number of beam lines, e.g. 1 for 8th notes or 2 for 16th
	 * @param staffLinesCount  the number of staff lines, e.g. 5
	 */
	public Offset computeForOneStaff(int[] notesLp, StemDirection stemDir,
		float[] stemsXIs, int beamLinesCount, int staffLinesCount) {
		return new Offset(0, 0);
	}
	
	
	
	
}
