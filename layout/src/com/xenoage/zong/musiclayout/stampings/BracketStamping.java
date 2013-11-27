package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.group.BracketGroup;

/**
 * Class for a bracket stamping.
 * 
 * This element groups one or more adjacent staves with a brace or
 * a square bracket at the very beginning of the system.
 *
 * @author Andreas Wenger
 */
@Const public final class BracketStamping
	extends Stamping {

	/** The first staff stamping this bracket embraces. */
	public final StaffStamping firstStaff;

	/** The last staff stamping this bracket embraces. */
	public final StaffStamping lastStaff;

	/** The horizontal position of the bracket in mm. */
	public final float positionX;

	/** The style of the bracket. */
	public final BracketGroup.Style groupStyle;


	public BracketStamping(StaffStamping firstStaff, StaffStamping lastStaff, float positionX,
		BracketGroup.Style groupStyle) {
		super(firstStaff, Level.Music, null, null);
		this.firstStaff = firstStaff;
		this.lastStaff = lastStaff;
		this.positionX = positionX;
		this.groupStyle = groupStyle;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.BracketStamping;
	}

}
