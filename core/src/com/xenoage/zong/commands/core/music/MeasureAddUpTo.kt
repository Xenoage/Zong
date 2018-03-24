package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.Score


/**
 * Adds empty measures to the score until the score has the given
 * number of measures.
 */
class MeasureAddUpTo(
		private val score: Score,
		private val measuresCount: Int
) : UndoableCommand() {

	//backup
	private var cmd: MeasureAdd? = null

	override fun execute() {
		val measuresToAdd = measuresCount - score.measuresCount
		cmd = MeasureAdd(score, measuresToAdd)
		cmd!!.execute()
	}

	override fun undo() {
		cmd!!.undo()
	}

}
