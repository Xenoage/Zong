package com.xenoage.zong.commands.core.music.tuplet

import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.Undoability
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.tuplet.Tuplet


/**
 * Removes the given [Tuplet].
 */
class TupletRemove(
		private val tuplet: Tuplet
) : UndoableCommand() {

	override fun execute() = apply(null)

	override fun undo() = apply(tuplet)

	private fun apply(newTuplet: Tuplet?) =
		tuplet.chords.forEach { it.tuplet = newTuplet }

}
