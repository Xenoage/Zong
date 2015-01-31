package com.xenoage.zong.musiclayout.notator.beam.direction;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;
import com.xenoage.zong.musiclayout.notations.ChordNotation;

/**
 * Strategy for computing the direction of a beamed stem.
 * Thread-safe.
 * 
 * @author Andreas Wenger
 */
public abstract class Strategy {

	/**
	 * Returns better {@link StemDirection}s of the given chord within the given beam
	 * in a staff with the given number of lines.
	 */
	public abstract StemDirection[] compute(Beam beam, ChordNotation[] chordsNot, int linesCount);

}
