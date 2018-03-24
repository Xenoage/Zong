package com.xenoage.zong.commands.core.music.group

import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.Undoability
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.music.StavesList
import com.xenoage.zong.core.music.group.BracketGroup


/**
 * Removes a [BracketGroup].
 */
class BracketGroupRemove(
		private val stavesList: StavesList,
		private val group: BracketGroup
) : UndoableCommand() {

	//backup
	private var groupIndex: Int = 0

	override fun execute() {
		//remember element index, since the list is sorted
		groupIndex = stavesList.bracketGroups.indexOf(group)
		if (groupIndex == -1)
			throw IllegalStateException("group is unknown")
		stavesList.bracketGroups.remove(group)
	}

	override fun undo() {
		stavesList.bracketGroups.add(groupIndex, group)
	}

}
