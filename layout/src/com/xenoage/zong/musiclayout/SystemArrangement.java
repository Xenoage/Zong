package com.xenoage.zong.musiclayout;

import static com.xenoage.utils.collections.ArrayUtils.sum;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.collections.IList;
import com.xenoage.zong.musiclayout.spacing.ColumnSpacing;

/**
 * A system arrangement is the horizontal and
 * vertical spacing of a system.
 * 
 * It contains the indices of the first and last
 * measure of the system, the widths of all
 * measure columns, the width of the system
 * (which may be longer than the width used
 * by the measures), the distances between the
 * staves and the vertical offset of the system.
 *
 * @author Andreas Wenger
 */
@Const @Getter public final class SystemArrangement {

	/** The index of the first measure of the system. */
	public final int startMeasureIndex;
	/** The index of the last measure of the system. */
	public final int endMeasureIndex;

	/** The list of the layouts of the measure columns in this system.
	 * This will often contain references to the measure column spacings
	 * that were computed before, but it can also store new measure column spacings
	 * that were created because for example a leading spacing was added.
	 */
	public final IList<ColumnSpacing> columnSpacings;

	/** The left margin of the system in mm. */
	public final float marginLeft;
	/** The right margin of the system in mm. */
	public final float marginRight;

	/** The width of the system (without the horizontal offset).
	 * It may be longer than the used width, e.g. to create empty staves.
	 * To get the used width, call {@link #getUsedWidth()}. */
	public final float width;

	/** The heights of the staves in mm. (#staves-1) items. */
	public final float[] staffHeights; //
	/** The distances between the staves in mm. (#staves-2) items. */
	public final float[] staffDistances;

	/** The vertical offset of the system in mm, relative to the top. */
	public final float offsetY;

	//cache
	private transient float height = Float.NaN;
	private transient float usedWidth = Float.NaN;


	//LOMBOK
	public SystemArrangement(int startMeasureIndex, int endMeasureIndex,
		IList<ColumnSpacing> columnSpacings, float marginLeft, float marginRight, float width,
		float[] staffHeights, float[] staffDistances, float offsetY) {
		if (staffHeights.length != staffDistances.length + 1) {
			throw new IllegalArgumentException("There must be one more staff height that staff distance");
		}
		this.startMeasureIndex = startMeasureIndex;
		this.endMeasureIndex = endMeasureIndex;
		this.columnSpacings = columnSpacings;
		this.marginLeft = marginLeft;
		this.marginRight = marginRight;
		this.width = width;
		this.staffHeights = staffHeights;
		this.staffDistances = staffDistances;
		this.offsetY = offsetY;
	}

	/**
	 * Gets the height of the staff with the given index.
	 */
	public float getStaffHeight(int index) {
		return staffHeights[index];
	}

	/**
	 * Gets the distance between the previous and the given staff.
	 */
	public float getStaffDistance(int index) {
		return (index > 0 ? staffDistances[index - 1] : 0);
	}

	/**
	 * Gets the total height of this system in mm.
	 */
	public float getHeight() {
		if (Float.isNaN(height))
			height = sum(staffHeights) + sum(staffDistances);
		return height;
	}

	/**
	 * Gets the used width of the system.
	 */
	public float getUsedWidth() {
		if (Float.isNaN(usedWidth)) {
			usedWidth = 0;
			for (ColumnSpacing mcs : columnSpacings) {
				usedWidth += mcs.getWidth();
			}
		}
		return usedWidth;
	}

	public SystemArrangement withWidth(float width) {
		return new SystemArrangement(startMeasureIndex, endMeasureIndex, columnSpacings,
			marginLeft, marginRight, width, staffHeights, staffDistances, offsetY);
	}
	
	public SystemArrangement withSpacings(IList<ColumnSpacing> columnSpacings, float width) {
		return new SystemArrangement(startMeasureIndex, endMeasureIndex, columnSpacings,
			marginLeft, marginRight, width, staffHeights, staffDistances, offsetY);
	}
	
	public SystemArrangement withOffsetY(float offsetY) {
		return new SystemArrangement(startMeasureIndex, endMeasureIndex, columnSpacings,
			marginLeft, marginRight, width, staffHeights, staffDistances, offsetY);
	}
}
