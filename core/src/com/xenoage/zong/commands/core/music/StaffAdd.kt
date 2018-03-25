package com.xenoage.zong.commands.core.music

import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.Undoability
import com.xenoage.utils.document.exceptions.UselessException
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.Staff


/**
 * Adds staves at the end of a score.
 * The staves are filled with empty measures, according to the number of measures in the score.
 *
 * @author Andreas Wenger
 */
@Untested
class StaffAdd
/**
 * Creates a [StaffAdd] command.
 * @param score          the affected score
 * @param stavesCount    the number of staves to add
 */
(//data
		private val score: Score, private val stavesCount: Int) : Command {


	override val undoability: Undoability
		get() = Undoability.Undoable


	override fun execute() {
		if (stavesCount < 1)
			throw UselessException()
		for (i in 0 until stavesCount) {
			val staff = Staff.Companion.staffMinimal()
			staff.parent = score.stavesList
			staff.addEmptyMeasures(score.measuresCount)
			score.stavesList.staves.add(staff)
		}
	}


	override fun undo() {
		//remove the added staves
		for (i in 0 until stavesCount) {
			score.stavesList.staves.removeAt(score.stavesCount - 1)
		}
	}

}
