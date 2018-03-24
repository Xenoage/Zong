package com.xenoage.zong.commands.core.music.group

import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.music.StavesList
import com.xenoage.zong.core.music.group.BarlineGroup
import com.xenoage.zong.core.music.group.BracketGroup


/**
 * Adds a [BarlineGroup] to the score.
 */
class BracketGroupAdd(
		private val stavesList: StavesList,
		private val group: BracketGroup
) : UndoableCommand() {

	override fun execute() {
		//check parameters
		val startStaff = group.staves.start
		val endStaff = group.staves.stop
		if (endStaff >= stavesList.staves.size)
			throw IllegalArgumentException("staves out of range")

		//add new group at the right position
		//(the barline groups are sorted by start index)
		val groups = stavesList.bracketGroups
		var i = 0
		while (i < groups.size && startStaff > groups[i].staves.start)
			i++
		groups.add(i, group)
	}

	override fun undo() {
		stavesList.bracketGroups.remove(group)
	}

}
