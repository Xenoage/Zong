package com.xenoage.zong.core.util

import com.xenoage.utils.math._0
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.util.BeatE
import com.xenoage.zong.core.position.MP.Companion.atBeat
import com.xenoage.zong.core.position.MP.Companion.mp0
import com.xenoage.zong.core.position.MP.Companion.unknown
import com.xenoage.zong.core.position.MP.Companion.unknownMp
import com.xenoage.zong.core.position.MPElement
import java.util.*

/**
 * An iterator over all [MeasureElement]s in a score.
 *
 * Iterates over the staves, measures and its measure elements.
 * The elements within a measure may be unsorted by beat.
 */
class MeasureElementIterator(
		private val score: Score
) : Iterator<MPElement>, Iterable<MPElement> {

	var mp = unknownMp
		private set
	private var elements: Iterator<BeatE<out MPElement>>? = null


	init {
		elements = score.getMeasure(mp0).measureElements.iterator()
		findNext()
	}

	override fun hasNext(): Boolean {
		return elements != null
	}

	override fun next(): MPElement {
		if (!hasNext())
			throw NoSuchElementException()
		val element = elements!!.next()
		mp = mp.copy(beat = element.beat)
		findNext()
		return element.element
	}

	private fun findNext() {
		while (true) {
			if (elements!!.hasNext()) {
				//next element within measure exists
				break
			} else {
				if (mp.measure + 1 < score.measuresCount) {
					//next measure within staff
					mp = atBeat(mp.staff, mp.measure + 1, unknown, _0)
				} else if (mp.staff + 1 < score.stavesCount) {
					//next staff
					mp = atBeat(mp.staff + 1, 0, unknown, _0)
				} else {
					//finished
					elements = null
					break
				}
				elements = score.getMeasure(mp).measureElements.iterator()
			}
		}
	}

	override fun iterator(): Iterator<MPElement> {
		return this
	}

}
