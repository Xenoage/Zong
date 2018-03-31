package com.xenoage.zong.commands.core.music.slur

import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.collections.removeOrEmpty
import com.xenoage.zong.core.music.slur.Slur


/**
 * Removes the given [Slur].
 */
class SlurRemove(
		private val slur: Slur
) : UndoableCommand() {

	override fun execute() {
		//remove slur from chords
		for (wp in slur.waypoints) {
			val chord = wp.chord
			chord.slurs = chord.slurs.removeOrEmpty(slur)
		}
	}

	override fun undo() {
		SlurAdd(slur).execute()
	}

}
