package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.direction.Wedge;

/**
 * Class for a wedge (crescendo or decrescendo) stamping
 * belonging to a staff.
 * 
 * It has a vertical position (line position of the
 * center baseline of the wedge), a horizontal start
 * and end position in mm and the vertical distance at these
 * points in interline spaces.
 *
 * @author Andreas Wenger
 */
@Const public final class WedgeStamping
	extends Stamping {

	/** The line position of the (centered) baseline. */
	public final float lp;

	/** The horizontal start position in mm relative to the beginning of the staff. */
	public final float x1Mm;

	/** The horizontal end position in mm relative to the beginning of the staff. */
	public final float x2Mm;

	/** The vertical distance of the lines at the start position in IS. */
	public final float d1Is;

	/** The vertical distance of the lines at the end position in IS. */
	public final float d2Is;


	public WedgeStamping(Wedge wedge, float lp, float x1Mm, float x2Mm, float d1Is, float d2Is,
		StaffStamping parentStaff) {
		super(parentStaff, Level.Music, wedge, null);
		this.lp = lp;
		this.x1Mm = x1Mm;
		this.x2Mm = x2Mm;
		this.d1Is = d1Is;
		this.d2Is = d2Is;
	}

	@Override public StampingType getType() {
		return StampingType.WedgeStamping;
	}

}
