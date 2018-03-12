package com.xenoage.zong.core.format

/**
 * Layout information for a staff.
 */
data class StaffLayout(
	/** The distance between the bottom line of the previous staff to the top line
	 * of this staff in mm. This value has no meaning for the first staff of a system.  */
	val distance: Float
) {

	companion object {
		/** Default top distance: 10 mm */
		val defaultValue = StaffLayout(10f)
	}

}