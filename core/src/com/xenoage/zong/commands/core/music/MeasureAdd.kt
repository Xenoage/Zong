package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.UselessException
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.Score


/**
 * Adds empty measures at the end of a score.
 */
class MeasureAdd(
		private val score: Score,
		private val measuresCount: Int
) : UndoableCommand() {

	override fun execute() {
		if (measuresCount < 1)
			throw UselessException()
		//column headers
		score.header.addEmptyMeasures(measuresCount)
		//staves
		for (staff in score.stavesList.staves)
			staff.addEmptyMeasures(measuresCount)
	}

	override fun undo() {
		//remove the added measures.
		//this is trivial, since they still have no content in this state.
		val oldSize = score.measuresCount
		repeat(measuresCount) {
			val measureIndex = score.measuresCount - 1
			//column header
			score.header.columnHeaders.removeAt(measureIndex)
			//staves
			for (staff in score.stavesList.staves) {
				staff.measures.removeAt(measureIndex)
			}
		}
	}

}
