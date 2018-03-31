package com.xenoage.zong.commands.core.music.slur

import com.xenoage.utils.collections.addOrNew
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.music.slur.Slur


/**
 * Adds the given [Slur].
 */
class SlurAdd(
		private val slur: Slur
) : UndoableCommand() {

	override fun execute() {
		//add slur to chords
		for (wp in slur.waypoints) {
			val chord = wp.chord
			chord.slurs = chord.slurs.addOrNew(slur)
		}
	}

	override fun undo() {
		SlurRemove(slur).execute()
	}

}
