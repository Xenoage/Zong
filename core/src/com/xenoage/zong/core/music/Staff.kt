package com.xenoage.zong.core.music

import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.Measure.measure
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.util.MPE
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atMeasure
import com.xenoage.zong.core.position.MP.Companion.unknown
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.utils.exceptions.IllegalMPException


/**
 * Staff of any size.
 *
 * A vocal staff that is visible throughout the whole score is an instance of this class as well
 * as a small ossia staff that is only displayed over a single measure.
 *
 * Staves are divided into measures.
 */
class Staff (
		/** The measures from the beginning to the ending of the score (even the invisible ones).  */
		val measures: MutableList<Measure> = mutableListOf(),
		/** The number of lines in this staff.  */
		var linesCount: Int = 5,
		/** Distance between the lines in this staff, or null for default.  */
		val interlineSpace: IS?
) : MPContainer {

	init {
		for (measure in measures)
			measure.parent = this
	}

	var parentScore: Score? = null

	var parent: MPContainer?
		get() = throw UnsupportedOperationException()
		set(_) = throw UnsupportedOperationException()

	/**
	 * The number of voices in this staff.
	 * This is the number of voices in the measure with the most number of voices.
	 */
	val voicesCount: Int
		get() = measures.asSequence().map { it.voices.size }.max() ?: 0

	/** The parent score of this staff, or null, if this element is not part of a score. */
	val score: Score?
		get() = this.parent?.score

	/** Adds the given number of empty measures at the end of the staff. */
	fun addEmptyMeasures(measuresCount: Int) {
		for (i in 0 until measuresCount) {
			val m = measure()
			m.parent = this
			measures.add(m)
		}
	}

	/**
	 * Gets the measure with the given index, or throws an
	 * [IllegalMPException] if there is none.
	 */
	fun getMeasure(index: Int): Measure =
			measures.getOrNull(index) ?: throw IllegalMPException(atMeasure(index))

	/**
	 * Gets the measure with the given index, or throws an
	 * [IllegalMPException] if there is none.
	 * Only the measure index of the given [MP] is relevant.
	 */
	fun getMeasure(mp: MP): Measure =
		measures.getOrNull(mp.measure) ?: throw IllegalMPException(mp)

	/**
	 * Gets the voice within the measure at the given position, or throws an
	 * [IllegalMPException] if there is none.
	 * Only the measure index and voice index of the given position are relevant.
	 */
	fun getVoice(mp: MP): Voice =
		getMeasure(mp).getVoice(mp)

	/**
	 * Gets the [VoiceElement] before the given position (also over measure
	 * boundaries) together with its index in the voice,
	 * or null, if there is none (begin of score or everything is empty)
	 * @param mp         the position after the voice element, with element index
	 * @param onlyChord  if true, rests are ignored, and only a chord (or null) is returned
	 */
	fun getVoiceElementBefore(mp: MP, onlyChord: Boolean): MPE<VoiceElement>? {
		mp.requireStaffAndMeasureAndVoiceAndElement()
		//find the last voice element ending at or before the current beat
		//in the given measure
		var voice = getVoice(mp)
		var i = voice.elements.indexOfLast { !onlyChord || it is Chord }
		if (i != unknown)
			return MPE(voice.elements[i], mp.copy(element = i))
		//no result in this measure. loop through the preceding measures.
		for (iMeasure in mp.measure-1 downTo 0) {
			voice = getMeasure(iMeasure).getVoice(mp.voice)
			i = voice.elements.indexOfLast { !onlyChord || it is Chord }
			if (i != unknown)
				return MPE(voice.elements[i], MP.atElement(mp.staff, iMeasure, mp.voice, i))
		}
		//nothing found
		return null
	}

	/**
	 * Sets the measure with the given index.
	 * If out of current range, empty measures up to the given one are created.
	 */
	fun setMeasure(index: Int, measure: Measure) {
		while (index >= measures.size) {
			val m = measure()
			m.parent = this
			measures.add(m)
		}
		measures[index].parent = null
		measure.parent = this
		measures[index] = measure
	}

	/**
	 * Gets the [MP] of the given measure, or null if this staff is not
	 * part of a score or if the measure is not part of this staff.
	 */
	fun getMP(measure: Measure): MP? {
		val measureIndex = measures.indexOf(measure)
		val parent = this.parent
		if (parent == null || measureIndex == unknown)
			return null
		val staffIndex = parent.staves.indexOf(this)
		return if (staffIndex == unknown) null else atMeasure(staffIndex, measureIndex)
	}

}
