package com.xenoage.zong.core.format;

import lombok.Data;
import lombok.experimental.Wither;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.IList;

/**
 * Layout information for a system.
 *
 * @author Andreas Wenger
 */
@Const @Data @Wither public final class SystemLayout {

	/** The distance between the bottom line
	 * of the previous system to the top line of this system in mm.
	 * If this is the first system of a frame, this is the distance
	 * to the top margin of the frame (attention: this is different
	 * in MusicXML, where a special top-system-distance is used). */
	private final float distance;
	/** The distance between the left side of the staves and the left page margin. */
	private final float marginLeft;
	/** The distance between the right side of the staves and the left page margin. */
	private final float marginRight;
	/** The layouts of the staves of the system (may also be or contain null). */
	@MaybeNull public final IList<StaffLayout> staffLayouts;

	/** Default system layout. */
	public static final SystemLayout defaultValue = new SystemLayout(30f, 5f, 5f, null);


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

}
