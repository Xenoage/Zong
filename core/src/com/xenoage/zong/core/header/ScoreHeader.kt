package com.xenoage.zong.core.header

import com.xenoage.utils.setExtend
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.format.StaffLayout
import com.xenoage.zong.core.format.SystemLayout
import com.xenoage.zong.core.music.layout.SystemBreak
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.utils.exceptions.IllegalMPException


/**
 * This class contains general information about the musical data in a score.
 *
 * It contains a list of elements that are used in all staves.
 * Such elements are key signatures, time signatures and tempo changes for example.
 *
 * There is also layout information, like system and page breaks and system and staff distances.
 */
class ScoreHeader(
		/** The parent score. */
		val parent: Score
) {

	/** List of system layouts. May contain null elements for standardly layouted systems. */
	var systemLayouts: MutableList<SystemLayout?> = mutableListOf()

	/** List of column headers. Must have the same size as the number of measures in the score. */
	var columnHeaders: MutableList<ColumnHeader?> = mutableListOf()

	/**
	 * Gets layout information for the staff with the given index
	 * within the system with the given index, or null if undefined.
	 */
	fun getStaffLayout(systemIndex: Int, staffIndex: Int): StaffLayout? =
		getSystemLayout(systemIndex)?.getStaffLayout(staffIndex)

	/**
	 * Gets layout information for the system with the given index,
	 * or null if undefined.
	 */
	fun getSystemLayout(systemIndex: Int): SystemLayout? =
			systemLayouts.getOrNull(systemIndex)

	/**
	 * Gets the system index of the measure with the given index.
	 * Only forced system breaks are considered.
	 */
	fun getSystemIndex(measureIndex: Int): Int {
		var ret = 0
		for (i in 0..measureIndex) {
			val br = getColumnHeader(i).measureBreak
			if (br != null && br.systemBreak === SystemBreak.NewSystem)
				ret++
		}
		return ret
	}

	/**
	 * Sets the [SystemLayout] with the given index.
	 */
	fun setSystemLayout(systemIndex: Int, systemLayout: SystemLayout) =
			systemLayouts.setExtend(systemIndex, systemLayout, null)

	/**
	 * Gets the [ColumnHeader] at the given index, or null if there is none.
	 */
	fun getColumnHeader(measureIndex: Int): ColumnHeader =
			columnHeaders.getOrNull(measureIndex) ?: throw IllegalMPException(MP.atMeasure(measureIndex))

	/**
	 * Sets the [ColumnHeader] for the measure column with the given index.
	 */
	fun setColumnHeader(columnHeader: ColumnHeader, measureIndex: Int) {
		columnHeaders[measureIndex] = columnHeader
	}

	/**
	 * Adds the given number of empty measure columns.
	 */
	fun addEmptyMeasures(measuresCount: Int) {
		for (i in 0 until measuresCount)
			columnHeaders.add(ColumnHeader(parent))
	}

	/**
	 * Gets the last [TimeSignature] that is defined at or before the measure
	 * with the given index. If there is none, [TimeSignature.implicitSenzaMisura] returned.
	 * @param measureIndex  the index of the measure. May also be 1 measure after the last measure.
	 */
	fun getTimeAtOrBefore(measureIndex: Int): TimeSignature {
		var measureIndex = measureIndex
		if (measureIndex == columnHeaders.size)
			measureIndex--
		//search for last time
		for (iMeasure in measureIndex downTo 0) {
			val column = columnHeaders[iMeasure]
			val time = column?.time
			if (time != null)
				return time
		}
		//no key found. return default time.
		return TimeSignature.implicitSenzaMisura
	}

}
