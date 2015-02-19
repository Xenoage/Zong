package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;

/**
 * Class for a leger line stamping.
 * 
 * Leger lines belong to a staff.
 * They have a horizontal position around which they are centered.
 * For simple chords, they are usually {@value #lengthNormalIs} spaces long,
 * and if the chord contains suspended notes, {@value #lengthSuspendedIs} is usual.
 *
 * @author Andreas Wenger
 */
@Const public final class LegerLineStamping
	extends Stamping {
	
	/** Length of a normal leger line. */ //TIDY: move in layout settings
	public static final float lengthNormalIs = 2 * 1.1f;
	/** Length of a leger line within a chord with suspended notes. */ //TIDY: move in layout settings
	public static final float lengthSuspendedIs = 3 * 1.1f;

	/** The horizontal position of this leger line in mm. */
	public final float xMm;
	/** The line position of this leger line. */
	public final int lp;
	/** The width of this leger line in IS. */
	public final float widthIs;


	public LegerLineStamping(StaffStamping parentStaff, float x, int lp, float widthIs) {
		super(parentStaff, null);
		this.xMm = x;
		this.lp = lp;
		this.widthIs = widthIs;
	}

	@Override public StampingType getType() {
		return StampingType.LegerLineStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
