package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.chord.Chord;

/**
 * Class for a leger line stamping.
 * 
 * Leger lines belong to a staff. They have
 * a horizontal position around which they
 * are centered. They are 2 spaces long.
 *
 * @author Andreas Wenger
 */
@Const public final class LegerLineStamping
	extends Stamping {

	/** The horizontal position of this leger line in mm. */
	public final float x;

	/** The line position of this leger line. */
	public final int lp;


	public LegerLineStamping(StaffStamping parentStaff, Chord chord, float x, int lp) {
		super(parentStaff, Level.Music, chord, null);
		this.x = x;
		this.lp = lp;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.LegerLineStamping;
	}

}
