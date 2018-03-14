package com.xenoage.zong.core.music

import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math.`0/`
import com.xenoage.utils.math.fr
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.util.*
import com.xenoage.zong.core.music.util.FirstOrLast.*
import com.xenoage.zong.core.music.util.Interval.Result.*
import com.xenoage.zong.core.music.util.StartOrStop.*
import com.xenoage.zong.core.position.Beat
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atVoice
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement
import com.xenoage.zong.utils.exceptions.IllegalMPException
import javafx.collections.transformation.SortedList
import jdk.nashorn.internal.objects.NativeArray.reduce
import java.util.*

/**
 * Voice in a single measure within a single staff.
 *
 * A voice contains musical elements like chords and rests.
 */
class Voice(
		/** The list of rests and chords, sorted by time  */
		val elements: MutableList<VoiceElement> = mutableListOf(),
		/** Back reference: the parent measure of this voice, or null if not part of a measure. */
		var parent: Measure? = null
) : MPContainer {

	/** True, if this voice contains no elements. */
	val isEmpty: Boolean
		get() = elements.size == 0

	/**
	 * The filled beats in this voice, that means, the first beat in this
	 * voice where the is no music element following any more.
	 */
	val filledBeats: Duration
		get() = elements.fold(`0/`, { acc, e -> acc + e.duration })

	/**
	 * Gets a list of all beats used in this voice, that means
	 * all beats where at least one element with a duration greater than 0 begins
	 * or ends. Beat 0 is always used.
	 */
	val usedBeats: List<Fraction>
		get() {
			val ret = mutableListOf<Fraction>()
			var currentBeat = `0/`
			ret.add(currentBeat)
			for (e in elements) {
				if (e.duration.isGreater0) {
					currentBeat += e.duration
					ret.add(currentBeat)
				}
			}
			return ret
		}

	/**
	 * Convenience method. Gets the parent score of this voice,
	 * or null, if this voice is not part of a score.
	 */
	val score: Score?
		get() = parent?.score


	/**
	 * Gets the element with the given index, or throws an
	 * [IllegalMPException] if there is none.
	 */
	fun getElement(index: Int) =
			elements.getOrNull(index) ?: throw IllegalMPException(atVoice(index))


	/**
	 * Gets the [FirstOrLast] element, which [StartOrStop]s within
	 * the given [Interval] relative to the given element index, or null if there is none.
	 */
	fun getElement(side: FirstOrLast, border: StartOrStop, interval: Interval, elementIndex: Int): IndexE<VoiceElement>? {
		if (isEmpty)
			return null
		var pos = 0
		if (border == Start) {
			if (side == First) {
				for (e in elements) {
					val r = interval.isInInterval(pos, elementIndex)
					if (r === True)
						return IndexE(e, pos)
					else if (r === FalseHigh)
						return null //gone to far
					pos++
				}
				return null
			} else if (side == Last) {
				var ret: IndexE<VoiceElement>? = null
				for (e in elements) {
					val r = interval.isInInterval(pos, elementIndex)
					if (r === True)
						ret = IndexE(e, pos)
					pos++
				}
				return ret
			}
		} else if (border == Stop) {
			if (side == First) {
				for (e in elements) {
					val r = interval.isInInterval(pos + 1, elementIndex)
					if (r === True)
						return IndexE(e, pos)
					else if (r === FalseHigh)
						return null //gone to far
					pos++
				}
				return null
			} else if (side == Last) {
				var ret: IndexE<VoiceElement>? = null
				for (e in elements) {
					val r = interval.isInInterval(pos + 1, elementIndex)
					if (r === True)
						ret = IndexE(e, pos)
					pos++
				}
				return ret
			}
		}
		throw IllegalStateException()
	}

	/** Adds the given element at the end of this voice. */
	fun addElement(element: VoiceElement) {
		element.parent = this
		elements.add(element)
	}

	/** Adds the given element at the given position within this voice. */
	fun addElement(index: Int, element: VoiceElement) {
		element.parent = this
		elements.add(index, element)
	}

	/** Replaces the element with the given index by the given one. */
	fun replaceElement(index: Int, element: VoiceElement) {
		elements[index].parent = null
		element.parent = this
		elements[index] = element
	}

	/** Replaces the given element by the given ones. */
	fun replaceElement(oldElement: VoiceElement, vararg newElements: VoiceElement) {
		var index = elements.indexOf(oldElement)
		if (index == -1)
			throw IllegalArgumentException("Given element is not part of this voice.")
		oldElement.parent = null
		for (newElement in newElements) {
			elements.add(index, newElement)
			index++
		}
	}

	/** Removes the element with the given index. */
	fun removeElement(index: Int) {
		elements.removeAt(index).parent = null
	}

	/**
	 * Removes the given element.
	 * If found, its index is returned, otherwise -1.
	 */
	fun removeElement(element: VoiceElement): Int {
		val index = elements.indexOf(element)
		if (index > -1)
			elements.removeAt(index)
		return index
	}

	/**
	 * Gets the last used beat less than or equal the given one.
	 * If there are no elements, 0 is returned.
	 */
	fun getLastUsedBeat(maxBeat: Beat): Beat {
		var beat = `0/`
		for (e in elements) {
			val pos = beat + e.duration
			if (pos > maxBeat)
				break
			else
				beat = pos
		}
		return beat
	}

	/**
	 * Returns true, if the given beat is the starting
	 * beat of an element within this voice, beat 0,
	 * or the beat behind the last element.
	 */
	fun isBeatUsed(beat: Beat): Boolean {
		//all measures start with beat 0
		if (beat.is0)
			return true
		//is there an element at this beat?
		var curBeat = `0/`
		for (e in elements) {
			if (beat == curBeat)
				return true
			curBeat += e.duration
		}
		//first unused (empty) beat
		return beat == curBeat
	}

	/**
	 * Gets the last element at the given beat.
	 * That means, that if the beat starts with grace elements followed
	 * by a full element, the full element is returned.
	 * If no element starts at exactly the given beat, null is returned.
	 */
	fun getElementAt(beat: Beat): VoiceElement? {
		var currentBeat = `0/`
		var foundElement: VoiceElement? = null
		for (e in elements) {
			val compare = beat.compareTo(currentBeat)
			if (compare == 0)
				foundElement = e
			else if (compare < 0)
				break
			currentBeat += e.duration
		}
		return foundElement
	}


	/**
	 * Gets a list of the elements in this voice, beginning at or after
	 * the given start beat and before the given end beat.
	 * Grace elements at the start beat are ignored.
	 */
	fun getElementsInRange(startBeat: Beat, endBeat: Beat): List<VoiceElement> {
		val ret = mutableListOf<VoiceElement>()
		//collect elements
		var beat = `0/`
		for (e in elements) {
			if (beat >= endBeat)
				break
			else if (beat > startBeat || beat == startBeat && e.duration.isGreater0)
				ret.add(e)
			beat += e.duration
		}
		return ret
	}

	override fun toString() = elements.toString()

	/**
	 * Gets the start beat of the given element.
	 * If the element is not in this voice, null is returned.
	 */
	fun getBeat(element: MusicElement): Beat? {
		var beat = `0/`
		for (e in elements) {
			if (e === element)
				return beat
			beat += e.duration
		}
		return null
	}


	/**
	 * Gets the start beat of the element with the given index.
	 * If the index is greater than the number of elements or if the
	 * element is not within the voice, the beat after
	 * the last element is returned (or 0 if the voice is empty).
	 */
	fun getBeat(elementIndex: Int): Beat {
		var beat = `0/`
		for (i in elements.indices) {
			if (i >= elementIndex)
				break
			beat += elements[i].duration
		}
		return beat
	}


	/**
	 * Gets the index of the last element that starts before or at
	 * the given beat. If the given beat is after the last element,
	 * the index of the last element + 1 is returned (or 0 if the voice is empty).
	 */
	fun getElementIndex(beat: Beat): Int {
		var posI = 0
		var posB = `0/`
		for (i in elements.indices) {
			val newPosB = posB + elements[i].duration
			if (newPosB > beat)
				break
			posB = newPosB
			posI++
		}
		return posI
	}

	override fun getChildMP(element: MPElement<*>): MP? {
		var mp = parent?.getMP(this) ?: return null
		val beat = getBeat(element) ?: return null
		return mp.copy(beat = beat)
	}

}