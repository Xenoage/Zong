package com.xenoage.zong.musiclayout.notation.chord;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.annotation.ArticulationType;

/**
 * The vertical position and horizontal offset of a single
 * element within a chord.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public class ArticulationDisplacement {

	public final int yLp;
	public final float xIs;
	public final ArticulationType articulation;

}
