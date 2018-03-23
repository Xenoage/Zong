package com.xenoage.zong.core.util

import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.position.MP
import lombok.Getter
import java.util.NoSuchElementException

import com.xenoage.zong.core.position.MP.Companion.mp0

/**
 * An iterator over all [Beam]s in a score.
 */
class BeamIterator(score: Score) : Iterator<Beam>, Iterable<Beam> {

	private val it: VoiceElementIterator
	@Getter
	var mp: MP? = mp0
		private set
	private var nextMp: MP? = null
	private var nextBeam: Beam? = null


	init {
		it = VoiceElementIterator(score)
		findNext()
	}

	override fun hasNext(): Boolean {
		return nextBeam != null
	}

	override fun next(): Beam? {
		if (!hasNext())
			throw NoSuchElementException()
		val ret = nextBeam
		mp = nextMp
		findNext()
		return ret
	}

	private fun findNext() {
		while (it.hasNext()) {
			val e = it.next()
			if (e is Chord) {
				val beam = e.beam
				//return beam when its beginning was found
				if (beam != null && beam.start.chord == e) {
					nextMp = it.mp
					nextBeam = beam
					return
				}
			}
		}
		nextMp = null
		nextBeam = null
	}

	override fun iterator(): Iterator<Beam> {
		return this
	}

	override fun remove() {
		throw UnsupportedOperationException()
	}
}
