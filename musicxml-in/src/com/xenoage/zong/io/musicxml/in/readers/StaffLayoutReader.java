package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.zong.core.format.StaffLayout;
import com.xenoage.zong.musicxml.types.MxlStaffLayout;

/**
 * This class reads a staff-layout element into a
 * {@link StaffLayout} object.
 * 
 * @author Andreas Wenger
 */
public final class StaffLayoutReader {

	public static final class Value {

		public final StaffLayout staffLayout;
		/**
		 * Number of the staff.
		 * It refers to staff numbers within the part, from top to bottom on the system.
		 * Null means: for all staves (only if defined within the defaults element).
		 */
		public final Integer number;


		public Value(StaffLayout staffLayout, Integer number) {
			this.staffLayout = staffLayout;
			this.number = number;
		}
	}


	/**
	 * Reads a staff-layout element.
	 */
	public static Value readStaffLayout(MxlStaffLayout mxlStaffLayout, float tenthMm) {
		StaffLayout staffLayout = StaffLayout.defaultValue;

		//staff-distance
		Float mxlStaffDistance = mxlStaffLayout.getStaffDistance();
		if (mxlStaffDistance != null) {
			staffLayout = staffLayout.withDistance(tenthMm * mxlStaffDistance);
		}

		//number
		Integer number = mxlStaffLayout.getNumber();

		return new Value(staffLayout, number);
	}

}
