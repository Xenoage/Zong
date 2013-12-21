package com.xenoage.zong.core.format;

import static com.xenoage.utils.collections.CollectionUtils.setExtend;

import java.util.List;

import lombok.Data;

import com.xenoage.utils.annotations.MaybeNull;

/**
 * Layout information for a system.
 *
 * @author Andreas Wenger
 */
@Data
public final class SystemLayout {

	/** The distance between the bottom line
	 * of the previous system to the top line of this system in mm.
	 * If this is the first system of a frame, this is the distance
	 * to the top margin of the frame (attention: this is different
	 * in MusicXML, where a special top-system-distance is used). */
	private float distance = 30f;
	/** The distance between the left side of the staves and the left page margin. */
	private float marginLeft = 5f;
	/** The distance between the right side of the staves and the left page margin. */
	private float marginRight = 5f;
	/** The layouts of the staves of the system (may also be or contain null). */
	@MaybeNull public List<StaffLayout> staffLayouts = null;


	/**
	 * Gets layout information for the staff with the given index,
	 * or null if undefined.
	 */
	public StaffLayout getStaffLayout(int staffIndex) {
		if (staffLayouts != null && staffIndex >= 0 && staffIndex < staffLayouts.size())
			return staffLayouts.get(staffIndex);
		else
			return null;
	}

	/**
	 * Sets the {@link StaffLayout} for the at staff given index.
	 */
	public void setStaffLayout(int staffIndex, StaffLayout staffLayout) {
		staffLayouts = setExtend(staffLayouts, staffIndex, staffLayout, null);
	}

}
