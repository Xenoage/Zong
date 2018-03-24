package com.xenoage.zong.commands.core.music.beam

import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.Undoability
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.beam.BeamWaypoint
import com.xenoage.zong.core.music.tuplet.Tuplet


/**
 * Removes the given [Beam].
 */
class BeamRemove(private val beam: Beam) : UndoableCommand() {

	override fun execute() = assign(null)

	override fun undo() = assign(beam)

	private fun assign(newBeam: Beam?) =
			beam.waypoints.forEach { it.chord.beam = newBeam }

}
