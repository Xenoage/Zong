package com.xenoage.zong.musiclayout;

import com.xenoage.utils.annotations.Const;
import lombok.Data;
import lombok.experimental.Wither;

/**
 * A staff index and a vertical line position on this staff.
 *
 * The line position is 0 on the bottom line, 1 in the bottom space,
 * 2 on the second line from the bottom, -2 on the first bottom leger line and so on.
 *
 * The staff index can also be {@link #unknownStaff}.
 *
 * @author Andreas Wenger
 */
@Const @Data @Wither
public class SLP {

	public static final int unknownStaff = -1;

	public final int staff;
	public final float lp;


	public static SLP slp(int staff, float lp) {
		return new SLP(staff, lp);
	}

	/**
	 * Creates a {@link SLP}, when only the LP is used and the staff is not needed.
	 */
	public static SLP lp(float lp) {
		return slp(unknownStaff, lp);
	}

	/**
	 * Returns a new {@link SLP} with this staff and the given value added to this LP.
	 */
	public SLP addLp(float lp) {
		return slp(staff, this.lp + lp);
	}

}
