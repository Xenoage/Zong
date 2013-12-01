package com.xenoage.zong.musiclayout.continued;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.slur.Slur;

/**
 * Continued slur or tie.
 * 
 * The {@link Slur} element is known, as well as the placement
 * (above or below staff), the global index of the staff
 * and the level of the slur (0: first slur, 1: another slur, ...
 * used for the vertical position, to avoid collisions).
 * 
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor public final class ContinuedSlur
	implements ContinuedElement {

	public final Slur slur;
	public final VSide side;
	public final int staffIndex;
	public final int level;


	@Override public Slur getMusicElement() {
		return slur;
	}

	@Override public int getStaffIndex() {
		return staffIndex;
	}

}
