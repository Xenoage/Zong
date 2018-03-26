package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.UselessException
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.Staff


/**
 * Adds staves at the end of a score.
 * The staves are filled with empty measures, according to the number of measures in the score.
 */
class StaffAdd(
		private val score: Score,
		private val stavesCount: Int
) : UndoableCommand() {

	override fun execute() {
		if (stavesCount < 1)
			throw UselessException()
		repeat(stavesCount) {
			val staff = Staff()
			staff.score = score
			staff.addEmptyMeasures(score.measuresCount)
			score.stavesList.staves.add(staff)
		}
	}

	override fun undo() {
		//remove the added staves
		repeat(stavesCount) {
			score.stavesList.staves.removeAt(score.stavesCount - 1)
		}
	}

}
