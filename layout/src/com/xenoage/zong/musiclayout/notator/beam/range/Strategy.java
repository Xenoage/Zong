package com.xenoage.zong.musiclayout.notator.beam.range;

import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.musiclayout.notations.BeamNotation;
import com.xenoage.zong.musiclayout.notations.ChordNotation;
import com.xenoage.zong.musiclayout.notations.chord.StemNotation;
import com.xenoage.zong.musiclayout.spacing.ScoreSpacing;

/**
 * Strategy for computing the {@link BeamNotation} of a beam.
 * 
 * The {@link BeamNotation} is set to all {@link ChordNotation}s
 * and the lengths of the beamed stems (in {@link StemNotation}) may be changed.
 * 
 * Thread-safe.
 * 
 * @author Andreas Wenger
 */
public interface Strategy {

	public BeamNotation compute(Beam beam, ScoreSpacing scoreSpacing);

}
