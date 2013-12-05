package com.xenoage.zong.musiclayout.notations.beam;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.notations.chord.StemAlignment;

/**
 * Contains the layouting details of a {@link Beam}.
 * 
 * @author Uli Teschemacher
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter public final class BeamStemAlignments {

	/** The alignments of the stems stems of all chords. */
	public final StemAlignment[] stemAlignments;
	/** The width (vertical) in IS for all beam lines. */
	public final float beamLineWidth;
	/** The vertical distance between the beam lines in IS (the real gap between them). */
	public final float beamLineDistance;
	/** The number of beam lines, e.g. 2 for 16th notes. */
	public final int beamLinesCount;

	/**
	 * Returns the added up width of all lines of the beam including their distances
	 */
	public float getTotalWidth() {
		return beamLineWidth * beamLinesCount + beamLineDistance * (beamLinesCount - 1);
	}
}
