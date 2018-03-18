package com.xenoage.zong.core.music

import com.xenoage.utils.annotations.NonEmpty
import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.collections.SortedList
import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.chord.Note
import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.direction.Direction
import com.xenoage.zong.core.music.direction.DirectionContainer
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement
import com.xenoage.zong.utils.exceptions.IllegalMPException
import lombok.Getter
import lombok.Setter
import lombok.`val`

import java.util.HashMap

import com.xenoage.utils.CheckUtils.checkArgsNotNull
import com.xenoage.utils.collections.CollectionUtils.alist
import com.xenoage.utils.math._0
import com.xenoage.utils.max
import com.xenoage.zong.core.music.Voice.voice
import com.xenoage.zong.core.music.util.*
import com.xenoage.zong.core.music.util.BeatEList.beatEList
import com.xenoage.zong.core.music.util.BeatEList.emptyBeatEList
import com.xenoage.zong.core.music.util.Interval.*
import com.xenoage.zong.core.position.Beat
import com.xenoage.zong.core.position.MP.atVoice


/**
 * Measure within a single staff.
 *
 * A measure consists of one or more voices and a list of clefs, directions and and instrument changes.
 */
class Measure() : MPContainer, DirectionContainer {

	/** The list of voices (at least one).  */
	val voices = mutableListOf(Voice())

	/** The list of clefs, or null.  */
	var clefs: BeatEList<Clef>? = null

	/** The list of directions, or null. */
	var directions: BeatEList<Direction>? = null

	/** The list of instrument changes, or null.  */
	var instrumentChanges: BeatEList<InstrumentChange>? = null

	/** Back reference: the parent staff, or null if not part of a staff. */
	var parent: Staff? = null

	/**
	 * Gets the filled beats in this measure, that means, the first beat in this measure
	 * where there is no voice element following any more.
	 */
	val filledBeats: Beat
		get() = voices.max({ it.filledBeats }, _0)


	/**
	 * Gets a list of all clefs, directions and instrument changes, sorted by beat,
	 * and within beat sorted by clef, direction, and instrument change.
	 */
	/* OBSOLETE val measureElements: BeatEList<MusicElement>
		get() {
			val ret = BeatEList<MusicElement>()
			ret.addAll(clefs!!)
			ret.addAll(directions!!)
			ret.addAll(instrumentChanges!!)
			return ret
		}*/

	/** The parent score of this measure, or null, if this measure is not part of a score. */
	val score: Score?
		get() = parent?.score

	/**
	 * Adds a clef at the given beat or removes it, when null is given.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setClef(clef: Clef?, beat: Beat): Clef? {
		if (clef != null) {
			//add clef to list. create list if needed
			clef.parent = this
			var (clefs, oldClef) = clefs.set(clef, beat)
			//oldClef?.parent = null
			//return oldClef
		} else if (clefs != null) {
			//remove clef from list. delete list if not needed any more.
			val oldClef = clefs!!.remove(beat)
			if (clefs!!.size == 0)
				clefs = null
			oldClef?.parent = null
			return oldClef
		}
		return null
	}

	/**
	 * Adds a direction at the given beat. If there is already one, it is not
	 * replaced, since there may be many directions belonging to a single beat.
	 */
	fun addDirection(direction: Direction, beat: Beat) {
		direction.parent = this
		if (directions == null)
			directions = BeatEList<Direction>()
		directions!!.add(direction, beat)
	}


	/**
	 * Adds an instrument change at the given beat.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setInstrumentChange(instrumentChange: InstrumentChange?, beat: Beat): InstrumentChange? {
		if (instrumentChange != null) {
			//add instrumentChange to list. create list if needed
			instrumentChange.parent = this
			if (instrumentChanges == null)
				instrumentChanges = BeatEList<InstrumentChange>()
			val oldChange = instrumentChanges!!.set(instrumentChange, beat)
			oldChange?.parent = null
			return oldChange
		} else if (instrumentChanges != null) {
			//remove instrumentChange from list. delete list if not needed any more.
			val oldChange = instrumentChanges!!.remove(beat)
			if (instrumentChanges!!.size == 0)
				instrumentChanges = null
			oldChange?.parent = null
			return oldChange
		}
		return null
	}

	/**
	 * Collect the accidentals within this measure (backwards),
	 * beginning at the given start beat where the given key is valid, ending before or at
	 * the given beat (depending on the given interval), looking at all voices.
	 *
	 * @param beat       the maximum beat (inclusive or exclusive, depending on the interval)
	 * @param interval   where to stop looking ([Before] or [BeforeOrAt]). [At] is handled like [BeforeOrAt].
	 * @param startBeatKey  the key that is valid at the given start beat
	 * @return a map with the pitches that have accidentals (without alter)
	 * as keys and their corresponding alter values as values.
	 *
	 * GOON
	 */
	@Untested
	fun getAccidentals(beat: Fraction, interval: Interval, startBeat: Fraction, startBeatKey: Key): Map<Pitch, Int> {
		var interval = interval
		if (!(interval === Before || interval === BeforeOrAt || interval === At)) {
			throw IllegalArgumentException("Illegal interval for this method: $interval")
		}
		if (interval === At) {
			interval = BeforeOrAt
		}
		val ret = HashMap<Pitch, Int>()
		val retBeats = HashMap<Pitch, Fraction>()
		for (voice in voices) {
			var pos = startBeat
			for (e in voice.elements) {
				if (pos.compareTo(startBeat) < 0) {
					pos = pos.add(e.duration)
					continue
				}
				if (interval.isInInterval(pos, beat) !== Interval.Result.True) {
					break
				}
				if (e is Chord) {
					for ((pitch) in e.notes) {
						val pitchUnaltered = pitch.withoutAlter()
						//accidental already set?
						val oldAccAlter = ret[pitchUnaltered]
						if (oldAccAlter != null) {
							//there is already an accidental. only replace it if alter changed
							//and if it is at a later position than the already found one
							val existingBeat = retBeats[pitch]
							if (pitch.alter != oldAccAlter && pos.compareTo(existingBeat) > 0) {
								ret[pitchUnaltered] = pitch.alter
								retBeats[pitchUnaltered] = pos
							}
						} else {
							//accidental not neccessary because of key?
							if (startBeatKey.alterations[pitch.step] === pitch.alter) {
								//ok, we need no accidental here.
							} else {
								//add accidental
								ret[pitchUnaltered] = pitch.alter
								retBeats[pitchUnaltered] = pos
							}
						}
					}
				}
				pos = pos.add(e.duration)
			}
		}
		return ret
	}


	/**
	 * Gets a list of all beats used in this measure, that means
	 * all beats where at least one element with a duration greater than 0 begins.
	 * Beat 0 is always used.
	 * @param withMeasureElements  true, iff also the beats of the measure elements should be used
	 */
	fun getUsedBeats(withMeasureElements: Boolean): SortedList<Fraction> {
		var ret: SortedList<Fraction> = SortedList<T>(false)
		//voice element beats
		for (voice in voices) {
			val voiceBeats = voice.usedBeats
			ret = ret.merge(voiceBeats, false)
		}
		//beats of directions
		if (withMeasureElements) {
			for ((_, beat) in measureElements)
				ret.add(beat)
		}
		return ret
	}


	/**
	 * Gets the voice with the given index, or throws an
	 * [IllegalMPException] if there is none.
	 */
	fun getVoice(index: Int): Voice {
		return if (index >= 0 && index <= voices.size)
			voices[index]
		else
			throw IllegalMPException(atVoice(index))
	}


	/**
	 * Gets the voice with the given index, or throws an
	 * [IllegalMPException] if there is none.
	 * Only the voice index of the given position is relevant.
	 */
	fun getVoice(mp: MP): Voice {
		val index = mp.voice
		return if (index >= 0 && index < voices.size)
			voices[index]
		else
			throw IllegalMPException(mp)
	}


	/**
	 * Sets the voice with the given index.
	 * If the voice does not exist yet, it is created.
	 */
	fun setVoice(index: Int, voice: Voice) {
		while (index >= voices.size) {
			val voiceFill = Companion.voice()
			voiceFill.parent = this
			voices.add(voiceFill)
		}
		voice.parent = this
		voices[index] = voice
	}


	/**
	 * Gets the MP of the given [Voice], or null if it is not part
	 * of this measure or this measure is not part of a score.
	 */
	fun getMP(voice: Voice): MP? {
		val voiceIndex = voices.indexOf(voice)
		if (this.parent == null || voiceIndex == -1)
			return null
		var mp = this.parent!!.getMP(this)
		mp = mp!!.withVoice(voiceIndex)
		return mp
	}


	/**
	 * Gets the MP of the given [ColumnElement], or null if it is not part
	 * of this measure or this measure is not part of a score.
	 */
	override fun getChildMP(element: MPElement): MP? {
		if (this.parent == null)
			return null
		val mp = this.parent!!.getMP(this)
		if (element is Clef)
			return getMPIn(element, clefs, mp)
		else if (element is Key)
			return getMPIn(element, privateKeys, mp)
		else if (element is Direction)
			return getMPIn(element, directions, mp)
		else if (element is InstrumentChange)
			return getMPIn(element, instrumentChanges, mp)
		return null
	}


	/**
	 * Gets the [MP] of the given element within the given list of elements,
	 * based on the given [MP] (staff, measure), or null if the list of elements
	 * is null or the element could not be found.
	 */
	private fun getMPIn(element: MPElement, elements: BeatEList<*>?, baseMP: MP?): MP? {
		if (elements == null)
			return null
		for ((element1, beat) in elements)
			if (element1 === element)
				return MP.atBeat(baseMP!!.staff, baseMP.measure, baseMP.voice, beat)
		return null
	}

	/**
	 * The list of directions, maybe empty and immutable.
	 */
	fun getDirections(): BeatEList<Direction> {
		return if (directions == null) Companion.emptyBeatEList() else directions
	}

	companion object {


		/**
		 * Creates an empty measure.
		 */
		fun measure(): Measure {
			return Measure(alist(Voice.Companion.voice()), null, null, null, null)
		}
	}


}
