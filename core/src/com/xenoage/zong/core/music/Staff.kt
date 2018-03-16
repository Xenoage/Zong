package com.xenoage.zong.core.music

import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.util.MPE
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.utils.exceptions.IllegalMPException
import lombok.Getter
import lombok.Setter

import java.util.ArrayList

import com.xenoage.utils.kernel.Range.rangeReverse
import com.xenoage.zong.core.music.Measure.measure
import com.xenoage.zong.core.music.util.MPE.mpE
import com.xenoage.zong.core.position.MP.Companion.atMeasure
import com.xenoage.zong.core.position.MP.atMeasure
import java.lang.Math.max


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
		val measures: MutableList<Measure>,
		/** The number of lines in this staff.  */
		var linesCount: Int,
		/** Distance between the lines in this staff, or null for default.  */
		val interlineSpace: IS?) {

	init {
		for (measure in measures)
			measure.parent = this
	}

	/** Back reference: The parent staves list, or null if not part of a score.  */
	var parent: StavesList? = null

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
		for (i in  rangeReverse(mp.element - 1, 0)) {
			val e = voice.getElement(i)
			if (!onlyChord || e is Chord)
				return mpE(e, mp.withElement(i))
		}
		//no result in this measure. loop through the preceding measures.
		for (iMeasure in rangeReverse(mp.measure - 1, 0)) {
			voice = getMeasure(iMeasure).getVoice(mp.voice)
			for (i in rangeReverse(voice.elements)) {
				val e = voice.getElement(i)
				if (!onlyChord || e is Chord)
					return mpE(e, MP.atElement(mp.staff, iMeasure, mp.voice, i))
			}
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
		measure.parent = this
		measures[index] = measure
	}


	/**
	 * Gets the [MP] of the given measure, or null if this staff is not
	 * part of a score or if the measure is not part of this staff.
	 */
	fun getMP(measure: Measure): MP? {
		val measureIndex = measures.indexOf(measure)
		if (this.parent == null || measureIndex == -1)
			return null
		val staffIndex = this.parent!!.staves.indexOf(this)
		return if (staffIndex == -1) null else atMeasure(staffIndex, measureIndex)
	}

	companion object {


		/**
		 * Creates a new [Staff].
		 */
		fun staff(linesCount: Int, interlineSpace: Float?): Staff {
			val measures = ArrayList<Measure>()
			return Staff(measures, linesCount, interlineSpace)
		}


		/**
		 * Creates a minimal staff with no content.
		 */
		fun staffMinimal(): Staff {
			return staff(5, null)
		}
	}

}
