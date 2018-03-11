package com.xenoage.zong.core.position

import lombok.AllArgsConstructor
import lombok.Data
import lombok.experimental.Wither

import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.MusicElement
import com.xenoage.zong.core.music.Voice

import com.xenoage.utils.math.Fraction._0
import com.xenoage.zong.core.position.Time.time


/**
 * **M**usical **P**osition within a score.
 *
 * It can contain a staff, measure, voice index, beat and element index.
 *
 * All values may be [unknown] (or [unknownBeat] for the beat).
 * The element index is only used for elements within a voice, otherwise its value is [unknown].
 */
data class MP(
	/** The staff index, or [unknown] */
	val staff: Int = 0,
	/** The measure index, or [unknown] */
	val measure: Int = 0,
	/** The voice index, or [unknown] */
	val voice: Int = 0,
	/** The beat, or [unknownBeat].  */
	val beat: Fraction? = null,
	/** The element index within the voice, or [.unknown].  */
	@Wither
	val element: Int = 0) : Comparable<MP> {


	val isMeasureUnknown: Boolean
		get() = measure == unknown


	val isStaffOrMeasureUnknown: Boolean
		get() = staff == unknown || measure == unknown


	val isStaffOrMeasureOrVoiceUnknown: Boolean
		get() = staff == unknown || measure == unknown || voice == unknown


	val isStaffOrMeasureOrVoiceOrBeatUnknown: Boolean
		get() = staff == unknown || measure == unknown || voice == unknown || beat === unknownBeat


	val isStaffOrMeasureOrVoiceOrElementUnknown: Boolean
		get() = staff == unknown || measure == unknown || voice == unknown || element == unknown

	/**
	 * Returns the measure and beat of this [MP] as a [Time].
	 */
	val time: Time
		get() = time(measure, beat)


	fun requireStaffAndMeasureAndVoiceAndElement() {
		if (isStaffOrMeasureOrVoiceOrElementUnknown)
			throw IllegalStateException("Missing MP data. Required: staff, measure, voice, element. In: " + toString())
	}


	override fun toString(): String {
		return "[" + (if (staff != unknown) "Staff = $staff, " else "") +
				(if (measure != unknown) "Measure = $measure, " else "") + (if (voice != unknown) "Voice = $voice, " else "") +
				(if (beat !== unknownBeat) "Beat = " + beat!!.getNumerator() + "/" + beat!!.getDenominator() + ", " else "") +
				(if (element != unknown) "Element = $element" else "") + "]"
	}


	/**
	 * Compares this [MP] with the given one.
	 * It is compared by staff, measure, voice, beat or element index.
	 * If the beats are known, only those are compared, and if they are unknown,
	 * only the element indices are compared.
	 * None of the other values should be unknown, otherwise the result is undefined.
	 */
	override fun compareTo(mp: MP): Int {
		//staff
		return if (staff < mp.staff)
			-1
		else if (staff > mp.staff)
			1
		else {
			//measure
			if (measure < mp.measure)
				-1
			else if (measure > mp.measure)
				1
			else {
				//voice
				if (voice < mp.voice)
					-1
				else if (voice > mp.voice)
					1
				else if (beat != null && mp.beat != null) {
					//beat
					beat!!.compareTo(mp.beat)
				} else {
					//element index
					if (element < mp.element)
						-1
					else if (element > mp.element)
						1
					else
						0
				}
			}
		}
	}


	/**
	 * Compares the time of this [MP] with the given one.
	 * It is compared by measure, and then by beat or element index.
	 * If the beats are known, only those are compared, and if they are unknown,
	 * only the element indices are compared.
	 */
	fun compareTimeTo(mp: MP): Int {
		//measure
		return if (measure < mp.measure)
			-1
		else if (measure > mp.measure)
			1
		else {
			if (beat != null && mp.beat != null) {
				//beat
				beat!!.compareTo(mp.beat)
			} else {
				//element index
				if (element < mp.element)
					-1
				else if (element > mp.element)
					1
				else
					0
			}
		}
	}


	/**
	 * Returns this [MP] but also with the [.beat] field.
	 * For this, the [.element] index must be known and the score must be given.
	 */
	fun getWithBeat(score: Score): MP {
		val voice = score.getVoice(this)
		val beat = voice.getBeat(element)
		return withBeat(beat)
	}

	companion object {

		/** Special value for an unknown beat.  */
		val unknownBeat: Fraction? = null
		/** Special value for an unknown index.  */
		val unknown = -1
		/** Unknown MP.  */
		val unknownMp = MP(unknown, unknown, unknown, unknownBeat, unknown)


		/** Musical position with all values set to 0, including beat and element index.  */
		val mp0 = MP(0, 0, 0, _0, 0)
		/** Musical position with all values set to 0, including beat ("b" in name), but element index set to [.unknown].  */
		val mpb0 = MP(0, 0, 0, _0, unknown)
		/** Musical position with all values set to 0, including element ("e" in name), but beat set to [.unknownBeat].  */
		val mpe0 = MP(0, 0, 0, unknownBeat, 0)


		/**
		 * Creates a new musical position at the given staff, measure, voice, element index and beat.
		 */
		fun mp(staff: Int, measure: Int, voice: Int, beat: Fraction, element: Int): MP {
			return MP(staff, measure, voice, beat, element)
		}


		/**
		 * Creates a new musical position with the given measure and the
		 * other values set to unknown.
		 */
		fun atMeasure(measure: Int): MP {
			return MP(unknown, measure, unknown, unknownBeat, unknown)
		}


		/**
		 * Creates a new musical position with the given staff and the
		 * other values set to unknown.
		 */
		fun atStaff(staff: Int): MP {
			return MP(staff, unknown, unknown, unknownBeat, unknown)
		}


		/**
		 * Creates a new musical position with the given staff and measure and the
		 * voice set to unknown.
		 */
		fun atMeasure(staff: Int, measure: Int): MP {
			return MP(staff, measure, unknown, unknownBeat, unknown)
		}


		/**
		 * Creates a new musical position with the given voice and the
		 * other values set to unknown.
		 */
		fun atVoice(voice: Int): MP {
			return MP(unknown, unknown, voice, unknownBeat, unknown)
		}


		/**
		 * Creates a new musical position at the given staff, measure and voice.
		 */
		fun atVoice(staff: Int, measure: Int, voice: Int): MP {
			return MP(staff, measure, voice, unknownBeat, unknown)
		}


		/**
		 * Creates a new musical position at the given beat.
		 */
		fun atBeat(beat: Fraction): MP {
			return MP(unknown, unknown, unknown, beat, unknown)
		}


		/**
		 * Creates a new musical position at the given staff, measure, voice and beat.
		 */
		fun atBeat(staff: Int, measure: Int, voice: Int, beat: Fraction): MP {
			return MP(staff, measure, voice, beat, unknown)
		}


		/**
		 * Creates a new musical position at the given element index.
		 */
		fun atElement(element: Int): MP {
			return MP(unknown, unknown, unknown, unknownBeat, element)
		}


		/**
		 * Creates a new musical position at the given staff, measure, voice and element index.
		 */
		fun atElement(staff: Int, measure: Int, voice: Int, element: Int): MP {
			return MP(staff, measure, voice, unknownBeat, element)
		}


		/**
		 * Creates a new musical position with the given measure and beat
		 * and the other values set to unknown.
		 */
		fun atColumnBeat(measure: Int, beat: Fraction): MP {
			return MP(unknown, measure, unknown, beat, unknown)
		}


		/**
		 * Gets the [MP] of the given element, or null if it is unknown.
		 */
		fun getMP(element: MPElement?): MP? {
			return element?.getMP()
		}


		/**
		 * Gets the [MP] of the given element by querying its parent, or null if it is unknown.
		 * TODO: When Java 8 can be used for the whole project, use this method as the default
		 * implementation for MPElement.getMP().
		 */
		fun getMPFromParent(element: MPElement?): MP? {
			return if (element == null || element.parent == null) null else element.parent.getChildMP(element)
		}


		/**
		 * Gets the [MP] of the given element, or null if it is no MPElement, or
		 * if its parent is null or if the parent is not part of a score.
		 */
		fun getMP(element: MusicElement): MP? {
			return if (element is MPElement)
				getMP(element)
			else
				null
		}
	}


}
