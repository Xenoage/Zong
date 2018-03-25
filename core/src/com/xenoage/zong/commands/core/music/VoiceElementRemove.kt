package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.iterators.reverseIt
import com.xenoage.zong.commands.core.music.beam.BeamRemove
import com.xenoage.zong.commands.core.music.slur.SlurRemove
import com.xenoage.zong.commands.core.music.tuplet.TupletRemove
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.slur.Slur
import com.xenoage.zong.core.music.tuplet.Tuplet


/**
 * Removes the given [VoiceElement].
 *
 * All [Slur]s, the [Beam] and the [Tuplet] belonging to this element
 * are removed, too.
 */
class VoiceElementRemove(
		private val element: VoiceElement
) : UndoableCommand() {

	//backup
	private var elementIndex = 0
	private var backupCmds = mutableListOf<Command>()

	override fun execute() {
		val voice = element.parent ?: throw IllegalStateException("element is not part of a voice")

		//remove slurs, beam and tuplet, if it is a chord
		if (element is Chord) {
			val chord = element
			//remove slurs
			for (slur in chord.slurs.reverseIt())
				executeAndRemember(SlurRemove(slur))
			//remove beam
			if (chord.beam != null)
				executeAndRemember(BeamRemove(chord.beam!!))
			//remove tuplet
			if (chord.tuplet != null)
				executeAndRemember(TupletRemove(chord.tuplet!!))
		}

		//remove element, and remember its position
		elementIndex = voice.removeElement(element)
	}

	override fun undo() {
		val voice = element.parent
		//add element
		voice!!.addElement(elementIndex, element)
		//restore slurs, beam and tuplet
		for (cmd in backupCmds.reverseIt())
			cmd.undo()
	}

	private fun executeAndRemember(cmd: Command) {
		cmd.execute()
		backupCmds.add(cmd)
	}

}
