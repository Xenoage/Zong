package com.xenoage.zong.musiclayout.notator.chord.stem.beam.direction;

import com.xenoage.zong.core.Score;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Strategy for computing the {@link StemDirection} of beamed stems.
 * Thread-safe.
 * 
 * @author Andreas Wenger
 */
public interface Strategy {

	StemDirection[] compute(Beam beam, Score score);

}
