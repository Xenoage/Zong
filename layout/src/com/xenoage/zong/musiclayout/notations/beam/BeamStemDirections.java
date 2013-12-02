package com.xenoage.zong.musiclayout.notations.beam;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.beam.Beam;
import com.xenoage.zong.core.music.chord.StemDirection;

/**
 * Contains the directions of the stems of a single {@link Beam}.
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor @Getter public final class BeamStemDirections {

	public final StemDirection[] stemDirections;

}
