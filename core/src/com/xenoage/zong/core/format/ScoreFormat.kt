package com.xenoage.zong.core.format

import com.xenoage.utils.Mm
import com.xenoage.utils.font.FontInfo
import com.xenoage.utils.setExtend
import com.xenoage.zong.core.format.Defaults.defaultFont
import lombok.NonNull
import kotlin.properties.Delegates.notNull

/**
 * Default formats to be used in a score.
 */
class ScoreFormat {

	/** The default space between two staff lines in mm ("Rastralgröße" in German) */
	var interlineSpace: Mm = defaultInterlineSpace

	/** The default distance between the first line of the top system to the top page margin in mm */
	var topSystemDistance: Mm = defaultTopSystemDistance

	/** The default layout information for systems.  */
	var systemLayout = SystemLayout()

	/** Default staff layout information (may also contain null).  */
	var staffLayouts: MutableList<StaffLayout?> = mutableListOf()

	/** The default layout information for staves which have no own default layout */
	var staffLayoutOther: StaffLayout = StaffLayout.defaultValue

	/** The default font used for lyrics */
	var lyricFont: FontInfo = defaultFont

	/** The style of measure numbering.  */
	var measureNumbering: MeasureNumbering = MeasureNumbering.System

	/**
	 * Gets the default layout information for the given staff, or null if undefined.
	 */
	fun getStaffLayoutOrNull(staff: Int): StaffLayout? =
			staffLayouts.getOrNull(staff)

	/**
	 * Gets the default layout information for the given staff, or
	 * the default information for all other staves if unknown.
	 * Thus, null is never returned.
	 */
	fun getStaffLayout(staff: Int): StaffLayout =
			getStaffLayoutOrNull(staff) ?: staffLayoutOther

	/**
	 * Sets the [StaffLayout] of the staff with the given index.
	 */
	fun setStaffLayout(staff: Int, staffLayout: StaffLayout) {
		staffLayouts.setExtend(staff, staffLayout, null)
	}

	companion object {
		val defaultInterlineSpace: Mm = 1.6f
		val defaultTopSystemDistance: Mm = 15f
	}

}
