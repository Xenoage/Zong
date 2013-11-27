package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;

/**
 * Class for a cursor stamping that belongs to a consecutive
 * range of staves (often the whole system).
 * This may be used for a playback cursor for example.
 *
 * @author Andreas Wenger
 */
@Const public final class SystemCursorStamping
	extends Stamping {

	/** The top staff stamping, where the cursor begins. */
	public final StaffStamping topStaff;

	/** The bottom staff stamping, where the cursor ends. */
	public final StaffStamping bottomStaff;

	/** The horizontal position of the cursor, relative to the left side of the top staff. */
	public final float x;


	public SystemCursorStamping(StaffStamping topStaff, StaffStamping bottomStaff, float x) {
		super(topStaff, Level.EmptySpace, null, null);
		this.topStaff = topStaff;
		this.bottomStaff = bottomStaff;
		this.x = x;
	}

	@Override public StampingType getType() {
		return StampingType.SystemCursorStamping;
	}

}
