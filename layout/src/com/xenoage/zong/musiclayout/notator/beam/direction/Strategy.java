package com.xenoage.zong.musiclayout.notator.beam.direction;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Strategy for computing the direction of a beamed stem.
 * Thread-safe.
 * 
 * @author Andreas Wenger
 */
public abstract class Strategy {

	public abstract StemDirection[] compute(Beam beam, Score score);

}
