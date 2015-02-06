package com.xenoage.zong.musiclayout.spacing;

import static com.xenoage.utils.collections.ArrayUtils.sum;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The horizontal and vertical spacing of a system.
 * 
 * It contains the indices of the first and last measure of the system, the widths of all
 * measure columns, the width of the system (which may be longer than the width used
 * by the measures), the distances between the staves and the vertical offset of the system.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class SystemSpacing {

	/** The index of the first measure of the system. */
	public int startMeasureIndex;
	/** The index of the last measure of the system. */
	public int endMeasureIndex;

	/** The list of the spacings of the measure columns in this system. */
	public List<ColumnSpacing> columnSpacings;

	/** The left margin of the system in mm. */
	public float marginLeftMm;
	/** The right margin of the system in mm. */
	public float marginRightMm;

	/** The width of the system (without the horizontal offset).
	 * It may be longer than the used width, e.g. to create empty staves.
	 * To get the used width, call {@link #getUsedWidth()}. */
	public float widthMm;

	/** The heights of the staves in mm. (#staves-1) items. */
	public float[] staffHeightsMm;
	/** The distances between the staves in mm. (#staves-2) items. */
	public float[] staffDistancesMm;

	/** The vertical offset of the system in mm, relative to the top. */
	public float offsetYMm;


	/**
	 * Gets the height of the staff with the given index.
	 */
	public float getStaffHeight(int index) {
		return staffHeightsMm[index];
	}

	/**
	 * Gets the distance between the previous and the given staff.
	 */
	public float getStaffDistance(int index) {
		return (index > 0 ? staffDistancesMm[index - 1] : 0);
	}

	/**
	 * Gets the total height of this system in mm.
	 */
	public float getHeight() {
		return sum(staffHeightsMm) + sum(staffDistancesMm);
	}

	/**
	 * Gets the used width of the system.
	 */
	public float getUsedWidth() {
		float usedWidth = 0;
		for (ColumnSpacing mcs : columnSpacings)
			usedWidth += mcs.getWidthMm();
		return usedWidth;
	}

}
