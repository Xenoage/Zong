package com.xenoage.zong.commands.core.music.group

import com.xenoage.utils.document.UselessException
import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.iterators.reverseIt
import com.xenoage.zong.core.music.StavesList
import com.xenoage.zong.core.music.group.BarlineGroup


/**
 * Adds a [BarlineGroup] to the score.
 *
 * Since a staff may only have one barline group, existing barline groups
 * at the given positions are removed. Special case: If the given group is completely
 * within another group, a [UselessException] is thrown.
 */
class BarlineGroupAdd(
		private val stavesList: StavesList,
		private val group: BarlineGroup
) : UndoableCommand() {

	//backup
	private var backupCmds = mutableListOf<Command>()

	override fun execute() {
		//check parameters
		val startStaff = group.staves.start
		val endStaff = group.staves.stop
		if (endStaff >= stavesList.staves.size)
			throw IllegalArgumentException("staves out of range")

		//if the given group is within an existing one, ignore the new group
		//(we do not support nested barline groups)
		val groups = stavesList.barlineGroups
		for (i in groups.indices) {
			val group = groups[i]
			if (startStaff >= group.staves.start && endStaff <= group.staves.stop) {
				throw UselessException()
			}
		}

		//delete existing groups intersecting the given range
		for (i in groups.indices) {
			val group = groups[i]
			if (startStaff <= group.staves.stop && endStaff >= group.staves.start) {
				executeAndRemember(BarlineGroupRemove(stavesList, group))
			}
		}

		//add new group at the right position
		//(the barline groups are sorted by start index)
		var i = 0
		while (i < groups.size && startStaff > groups[i].staves.start)
			i++
		groups.add(i, group)
	}

	override fun undo() {
		stavesList.barlineGroups.remove(group)
		if (backupCmds != null) {
			for (cmd in backupCmds.reverseIt())
				cmd.undo()
		}
	}

	private fun executeAndRemember(cmd: Command) {
		cmd.execute()
		backupCmds.add(cmd)
	}

}
