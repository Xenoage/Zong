package com.xenoage.zong.core.format

import com.xenoage.utils.Mm
import com.xenoage.utils.setExtend

/**
 * Layout information for a system.
 */
data class SystemLayout(
	/** The distance between the bottom line
	 * of the previous system to the top line of this system in mm.
	 * If this is the first system of a frame, this is the distance
	 * to the top margin of the frame (attention: this is different
	 * in MusicXML, where a special top-system-distance is used).  */
	var distance: Mm = defaultDistance,
	/** The distance between the left side of the staves and the left page margin */
	var marginLeft: Mm = defaultMarginLeft,
	/** The distance between the right side of the staves and the left page margin */
	var marginRight: Mm = defaultMarginRight,
	/** The layouts of the staves of the system (may also contain null).  */
	val staffLayouts: MutableList<StaffLayout?> = mutableListOf()
) {

	/**
	 * Gets layout information for the staff with the given index,
	 * or null if undefined.
	 */
	fun getStaffLayout(staffIndex: Int): StaffLayout? =
			staffLayouts?.getOrNull(staffIndex)

	/**
	 * Sets the [StaffLayout] for the at staff given index.
	 */
	fun setStaffLayout(staffIndex: Int, staffLayout: StaffLayout) {
		staffLayouts.setExtend(staffIndex, staffLayout, null)
	}

	companion object {
		val defaultDistance: Mm = 30f
		val defaultMarginLeft: Mm = 5f
		val defaultMarginRight: Mm = 5f
	}

}
