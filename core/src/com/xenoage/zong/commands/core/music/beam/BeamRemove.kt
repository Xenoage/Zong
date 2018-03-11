package com.xenoage.zong.commands.core.music.beam

import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.Undoability
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.beam.BeamWaypoint
import com.xenoage.zong.core.music.tuplet.Tuplet


/**
 * Removes the given [Tuplet].
 *
 * @author Andreas Wenger
 */
class BeamRemove(//data
		private val beam: Beam) : Command {


	override val undoability: Undoability
		get() = Undoability.Undoable


	override fun execute() {
		for (wp in beam.waypoints) {
			wp.chord.beam = null
		}
	}


	override fun undo() {
		for (wp in beam.waypoints) {
			wp.chord.beam = beam
		}
	}

}
