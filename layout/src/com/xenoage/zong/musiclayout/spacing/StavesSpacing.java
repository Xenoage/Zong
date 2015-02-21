package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.NullUtils.notNull;
import static com.xenoage.utils.kernel.Range.range;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.Optimized;
import com.xenoage.utils.annotations.Optimized.Reason;
import com.xenoage.zong.core.music.Staff;

/**
 * Vertical spacing of a staves in a system.
 * 
 * @author Andreas
 */
@Const @RequiredArgsConstructor @Getter
public final class StavesSpacing {

	/** All staves in the system. */
	private final List<Staff> staves;
	/** The distances between the staves in mm. (#staves-1) items. */
	private final float[] distancesMm;
	/** The default interline soace in mm. */
	private final float defaultIs;
	
	@Optimized(Reason.Performance)
	private float[] cachedYOffsetsMm = null;
	@Optimized(Reason.Performance)
	private float cachedTotalHeightMm = Float.NaN;
	
	
	/**
	 * Gets the height of the given staff in mm.
	 */
	public float getStaffHeightMm(int staff) {
		return (staves.get(staff).getLinesCount() - 1) *
			notNull(staves.get(staff).getInterlineSpace(), defaultIs);
	}
	
	/**
	 * Gets the positive distance between the previous and the given staff in mm.
	 */
	public float getStaffDistanceMm(int staff) {
		return (staff > 0 ? distancesMm[staff - 1] : 0);
	}
	
	/**
	 * Gets the vertical offset in mm of the given staff in system space.
	 * This is the positive distance between the top line of the first staff and the
	 * top line of the given staff.
	 */
	public float getStaffYOffsetMm(int staff) {
		if (cachedYOffsetsMm == null) {
			cachedYOffsetsMm = new float[staves.size()]; 
			float yMm = 0;
			for (int iStaff : range(staves)) {
				cachedYOffsetsMm[iStaff] = yMm;
				if (iStaff < staves.size() - 1)
					yMm += getStaffHeightMm(iStaff) + distancesMm[iStaff]; 
			}
		}
		return cachedYOffsetsMm[staff];
	}
	
	/**
	 * Gets the total height of all staves in mm.
	 * This is the positive distance between the top line of the top staff
	 * and the bottom line of the bottom staff.
	 */
	public float getTotalHeightMm() {
		if (Float.isNaN(cachedTotalHeightMm)) {
			int lastStaff = staves.size() - 1;
			cachedTotalHeightMm = getStaffYOffsetMm(lastStaff) + getStaffHeightMm(lastStaff);
		}
		return cachedTotalHeightMm;
	}
	
	/** 
	 * Computes and returns the y-coordinate in mm in system space
	 * of an object in the given staff on the given line position.
	 * Also non-integer values (fractions of interline spaces)
	 * are allowed.
	 */
	public float getYMm(int staff, float lp) {
		float staffLp0Mm = getStaffYOffsetMm(staff) + getStaffHeightMm(staff);
		Staff s = staves.get(staff);
		float is = notNull(s.getInterlineSpace(), defaultIs);
		return staffLp0Mm - lp * is / 2;
	}
	
	/**
	 * Computes and returns the y-coordinate of an object in the given staff 
	 * at the given vertical position in mm in system space as a line position.
	 * Also non-integer values are allowed.
	 */
	public float getYLp(int staff, float mm) {
		float staffLp0Mm = getStaffYOffsetMm(staff) + getStaffHeightMm(staff);
		float is = notNull(staves.get(staff).getInterlineSpace(), defaultIs);
		return (staffLp0Mm - mm) * 2 / is;
	}
}
