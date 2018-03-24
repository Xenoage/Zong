package com.xenoage.zong.commands.core.music.group

import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.music.StavesList
import com.xenoage.zong.core.music.group.BarlineGroup


/**
 * Removes a [BarlineGroup].
 */
class BarlineGroupRemove(
		private val stavesList: StavesList,
		private val group: BarlineGroup
) : UndoableCommand() {

	//backup
	private var groupIndex: Int = 0

	override fun execute() {
		//remember element index, since the list is sorted
		groupIndex = stavesList.barlineGroups.indexOf(group)
		if (groupIndex == -1)
			throw IllegalStateException("group is unknown")
		stavesList.barlineGroups.remove(group)
	}

	override fun undo() {
		stavesList.barlineGroups.add(groupIndex, group)
	}

}
