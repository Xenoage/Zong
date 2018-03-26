package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.command.Command
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.utils.iterators.reverseIt
import com.xenoage.zong.core.music.Voice
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.rest.Rest
import com.xenoage.zong.core.position.Beat
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atElement
import com.xenoage.zong.utils.exceptions.MeasureFullException

/**
 * Replaces the [VoiceElement]s between the given [MP]
 * and the given duration (if any) by the given [VoiceElement].
 * If the end position is within an element, that element is removed too.
 * All affected elements (slurs, beams, ...) will be removed.
 */
class VoiceElementWrite(
	/** The affected voice */
	private val voice: Voice,
	/** The position where to write the element.
	 * If a beat is given: If there is empty space before this beat,
	 * it is filled by rests. If this beat is within an element, not this position
	 * but the start position of the element is used.
	 * If an element index is given: The element is written
	 * at the start beat of the existing element with this index, or, if it does not exist,
	 * after the last element in this voice. */
	private val startMP: MP,
	/** The element to write. */
	private val element: VoiceElement,
	/** The options for writing. */
	private val options: Options = Options()
) : UndoableCommand() {

	//backup
	private var backupCmds = mutableListOf<Command>()

	/** Options for writing. */
	class Options(
		/** True, when an exception should be thrown when the element
		 * is too long for the current time signature. If this is true, the
		 * given voice must be part of a score.
		 */
		var checkTimeSignature: Boolean = false,
		/**
		 * True, if additional rests, written before the given element,
		 * should be set to invisible.
		 */
		var fillWithHiddenRests: Boolean = false
	)

	override fun execute() {
		//determine start mp and element index
		var startBeat: Beat
		val elementIndex: Int
		if (startMP.element != MP.unknown) {
			//start at indexed element
			elementIndex = startMP.element
			startBeat = voice.getBeat(elementIndex)
		} else if (startMP.beat != MP.unknownBeat) {
			//start at given beat
			val filledBeats = voice.filledBeats
			startBeat = startMP.beat!!
			val emptySpace = startBeat - filledBeats
			if (emptySpace.isGreater0) {
				//add rest between start beat and filled beats, if needed
				//TODO: split rests into reasonable parts
				val rest = Rest(emptySpace)
				rest.isHidden = options.fillWithHiddenRests
				executeAndRemember(VoiceElementWrite(voice, atElement(voice.elements.size), rest))
				elementIndex = voice.elements.size
			} else {
				elementIndex = voice.getElementIndex(startBeat)
			}
			//update start beat (may be within an element before)
			startBeat = voice.getBeat(elementIndex)
		} else {
			throw IllegalArgumentException("element index or beat must be given")
		}

		//affected range (start and end beat)
		val endBeat = startBeat + element.duration

		//optionally check time signature
		if (options.checkTimeSignature) {
			val score = voice.score ?: throw IllegalStateException("parent score is required")
			val time = score.header.getTimeAtOrBefore(startMP.measure)
			val duration = time.type.measureBeats
			if (duration != null && endBeat.compareTo(duration) > 0)
				throw MeasureFullException(startMP, element.duration)
		}

		//remove elements within the range
		var posBeat = startBeat
		var lastRemoveIndex = -1
		var i = elementIndex
		while (i < voice.elements.size && posBeat < endBeat) {
			//we are still not at the end beat. remove element
			val e = voice.elements[i]
			posBeat += e.duration
			lastRemoveIndex = i
			i++
		}
		for (i in elementIndex downTo lastRemoveIndex)
			executeAndRemember(VoiceElementRemove(voice.elements[i]))

		//insert new element
		voice.addElement(elementIndex, element)
	}

	override fun undo() {
		voice.removeElement(element)
		for (cmd in backupCmds.reverseIt())
			cmd.undo()
	}

	private fun executeAndRemember(cmd: Command) {
		cmd.execute()
		backupCmds.add(cmd)
	}

}
