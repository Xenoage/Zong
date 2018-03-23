package com.xenoage.zong.core.util

import com.xenoage.utils.math._0
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.mp0
import com.xenoage.zong.core.position.MP.Companion.unknownMp
import java.util.*

/**
 * An iterator over all [VoiceElement]s in a score.
 *
 * Iterates over the staves, measures, voices and its voice elements.
 */
class VoiceElementIterator(
		private val score: Score
) : Iterator<VoiceElement>, Iterable<VoiceElement> {

	var mp = unknownMp

	private var elements: List<VoiceElement>? = null
	private var nextMp: MP? = mp0


	init {
		elements = score.getVoice(nextMp!!).elements
		findNext()
	}

	override fun hasNext(): Boolean {
		return nextMp != null
	}

	override fun next(): VoiceElement {
		if (!hasNext())
			throw NoSuchElementException()
		mp = nextMp!!
		val element = elements!![mp.element]
		nextMp = mp.copy(element = nextMp!!.element + 1, beat = mp.beat!! + element.duration)
		findNext()
		return element
	}

	private fun findNext() {
		while (true) {
			if (nextMp!!.element < elements!!.size) {
				//next element within voice exists
				break
			} else {
				if (nextMp!!.voice + 1 < score.getMeasure(nextMp!!).voices.size) {
					//next measure within staff
					nextMp = MP(nextMp!!.staff, nextMp!!.measure, nextMp!!.voice + 1, _0, 0)
				} else if (nextMp!!.measure + 1 < score.measuresCount) {
					//next measure within staff
					nextMp = MP(nextMp!!.staff, nextMp!!.measure + 1, 0, _0, 0)
				} else if (nextMp!!.staff + 1 < score.stavesCount) {
					//next staff
					nextMp = MP(nextMp!!.staff + 1, 0, 0, _0, 0)
				} else {
					//finished
					nextMp = null
					break
				}
				elements = score.getVoice(nextMp!!).elements
			}
		}
	}

	override fun iterator(): Iterator<VoiceElement> {
		return this
	}

}
