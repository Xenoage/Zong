package com.xenoage.zong.commands.core.music.beam

import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.Undoability
import com.xenoage.utils.document.command.Undoability.Undoable
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.forEachReversed
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.beam.BeamWaypoint

/**
 * Adds the given [Beam].
 *
 * If the contained chords already are beamed, those beams
 * are removed.
 */
class BeamAdd(private val beam: Beam) : UndoableCommand("BeamAdd") {

	//backup
	private var backupCmds = mutableListOf<Command>()


	override fun execute() {
		//remove existing beams
		beam.waypoints.filter { it.chord.beam != null }.forEach {
			executeAndRemember(BeamRemove(it.chord.beam))
		}
		//add beam to chords
		beam.waypoints.forEach {
			it.chord.beam = beam
		}
	}


	override fun undo() {
		//remove beam
		BeamRemove(beam).execute()
		//restore old beams
		backupCmds?.forEachReversed { it.undo() }
	}


	private fun executeAndRemember(cmd: Command) {
		cmd.execute()
		backupCmds.add(cmd)
	}

}
