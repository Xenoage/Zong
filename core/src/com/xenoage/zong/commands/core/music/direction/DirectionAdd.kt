package com.xenoage.zong.commands.core.music.direction

import com.xenoage.utils.addOrNew
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.removeOrEmpty
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.direction.Direction


/**
 * Adds the given [Direction] to the given [Chord].
 */
class DirectionAdd(
		private val direction: Direction,
		private val chord: Chord
) : UndoableCommand() {

	override fun execute() {
		chord.directions = chord.directions.addOrNew(direction)
	}

	override fun undo() {
		chord.directions = chord.directions.removeOrEmpty(direction)
	}

}
