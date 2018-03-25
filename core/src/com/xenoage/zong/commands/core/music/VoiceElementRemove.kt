package com.xenoage.zong.commands.core.music

import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.Undoability
import com.xenoage.zong.commands.core.music.beam.BeamRemove
import com.xenoage.zong.commands.core.music.slur.SlurRemove
import com.xenoage.zong.commands.core.music.tuplet.TupletRemove
import com.xenoage.zong.core.music.Voice
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.slur.Slur
import com.xenoage.zong.core.music.tuplet.Tuplet

import java.util.ArrayList

import com.xenoage.utils.iterators.ReverseIterator.reverseIt
import com.xenoage.utils.kernel.Range.rangeReverse


/**
 * Removes the given [VoiceElement].
 *
 * All [Slur]s, the [Beam] and the [Tuplet] belonging to this element
 * are removed, too.
 *
 * @author Andreas Wenger
 */
@Untested  class VoiceElementRemove(//data
	private val element:VoiceElement):Command {
 //backup data
	private var elementIndex = 0
private var backupCmds:List<Command>? = null


override val undoability:Undoability
@Override get() =Undoability.Undoable


@Override  override fun execute() {
val voice = element.parent ?: throw IllegalStateException("element is not part of a voice")

 //remove slurs, beam and tuplet, if it is a chord
		if (element is Chord)
{
val chord = element
 //remove slurs
			for (i in rangeReverse(chord.slurs))
executeAndRemember(SlurRemove(chord.slurs[i]))
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


@Override  override fun undo() {
val voice = element.parent
 //add element
		voice!!.addElement(elementIndex, element)
 //restore slurs, beam and tuplet
		if (backupCmds != null)
{
for (cmd in Companion.reverseIt(backupCmds))
cmd.undo()
}
}


private fun executeAndRemember(cmd:Command) {
if (backupCmds == null)
backupCmds = ArrayList()
cmd.execute()
backupCmds!!.add(cmd)
}

}
