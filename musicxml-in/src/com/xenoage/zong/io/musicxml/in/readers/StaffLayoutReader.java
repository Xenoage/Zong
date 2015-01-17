package com.xenoage.zong.io.musicxml.in.readers;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.musicxml.types.MxlStaffLayout;

/**
 * This class reads a staff-layout element into a
 * {@link StaffLayout} object.
 * 
 * @author Andreas Wenger
 */
public final class StaffLayoutReader {

	@AllArgsConstructor
	public static final class Value {
		StaffLayout staffLayout;
		/**
		 * Number of the staff.
		 * It refers to staff numbers within the part, from top to bottom on the system.
		 * Null means: for all staves (only if defined within the defaults element).
		 */
		Integer number;
	}


	/**
	 * Reads a staff-layout element.
	 */
	public static Value readStaffLayout(MxlStaffLayout mxlStaffLayout, float tenthMm) {
		StaffLayout staffLayout = StaffLayout.defaultValue;

		//staff-distance
		Float mxlStaffDistance = mxlStaffLayout.getStaffDistance();
		if (mxlStaffDistance != null) {
			staffLayout = new StaffLayout(tenthMm * mxlStaffDistance);
		}

		//number
		Integer number = mxlStaffLayout.getNumber();

		return new Value(staffLayout, number);
	}

}
