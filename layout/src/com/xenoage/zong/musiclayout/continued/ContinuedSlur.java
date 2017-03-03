package com.xenoage.zong.musiclayout.continued;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
@Const @AllArgsConstructor
public final class ContinuedSlur
	implements ContinuedElement {

	@Getter public final Slur element;
	public final StaffStamping staff;
	public final int level; //TODO ZONG-117: currently unused

}
