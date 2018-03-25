package com.xenoage.zong.commands.core.music

import com.xenoage.utils.document.UselessException
import com.xenoage.utils.document.command.UndoableCommand
import com.xenoage.zong.core.music.Measure
import com.xenoage.zong.core.music.Voice


/**
 * Creates a [Voice] in a [Measure].
 *
 * For each voice x in a measure, there is also a voice x-1 (for x>0).
 * So maybe more than one voice is created.
 * If already existing, a [UselessException] is thrown.
 */
class VoiceAdd(
		private val measure: Measure,
		private val voiceIndex: Int
) : UndoableCommand() {

	//backup
	private var lastExistingVoiceIndex: Int = 0

	override fun execute() {
		if (measure.voices.size > voiceIndex)
			throw UselessException()
		lastExistingVoiceIndex = measure.voices.size - 1
		for (i in (lastExistingVoiceIndex+1)..voiceIndex) {
			val voice = Voice()
			voice.setParent(measure)
			measure.voices.add(voice)
		}
	}

	override fun undo() {
		var i = voiceIndex
		while (i > lastExistingVoiceIndex) {
			measure.voices.removeAt(i)
			i++
		}
	}


}
