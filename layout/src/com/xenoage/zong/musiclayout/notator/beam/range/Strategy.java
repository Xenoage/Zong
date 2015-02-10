package com.xenoage.zong.musiclayout.notator.beam.range;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacing.ScoreSpacing;

/**
 * Strategy for computing the {@link StemNotation}s of beamed stems.
 * Thread-safe.
 * 
 * @author Andreas Wenger
 */
public interface Strategy {

	public void compute(Beam beam, ScoreSpacing scoreSpacing);

}
