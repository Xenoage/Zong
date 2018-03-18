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
import com.xenoage.zong.core.music.util.BeatE
import com.xenoage.zong.core.music.util.BeatEList
import com.xenoage.zong.core.music.util.Interval
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
class Measure(
		/** The list of voices (at least one).  */
		val voices: MutableList<Voice>
) : MPContainer, DirectionContainer {

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
	 * Gets a list of all [MeasureElement],s sorted by beat,
	 * and within beat sorted by clef, key, directions, instrument change.
	 */
	val measureElements: BeatEList<MeasureElement>
		get() {
			val ret = Companion.beatEList()
			ret.addAll(clefs!!)
			ret.addAll(privateKeys!!)
			ret.addAll(directions!!)
			ret.addAll(instrumentChanges!!)
			return ret
		}


	/**
	 * Convenience method. Gets the parent score of this voice,
	 * or null, if this element is not part of a score.
	 */
	val score: Score?
		get() = if (this.parent != null) this.parent!!.score else null


	init {
		checkArgsNotNull(voices)
		if (voices.size == 0)
			throw IllegalArgumentException("A measure must have at least one voice")
		for (voice in voices)
			voice.parent = this
		this.clefs = clefs
		this.privateKeys = privateKeys
		this.instrumentChanges = instrumentChanges
	}


	/**
	 * Adds a clef at the given beat or removes it, when null is given.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setClef(clef: Clef?, beat: Fraction): Clef? {
		if (clef != null) {
			//add clef to list. create list if needed
			clef.parent = this
			if (clefs == null)
				clefs = Companion.beatEList()
			return clefs!!.set(clef, beat)
		} else if (clefs != null) {
			//remove clef from list. delete list if not needed any more.
			val ret = clefs!!.remove(beat)
			if (clefs!!.size() === 0)
				clefs = null
			return ret
		}
		return null
	}


	/**
	 * Adds a key at the given beat. If there is already one, it is replaced
	 * and returned (otherwise null).
	 */
	@Untested
	fun setKey(key: Key?, beat: Fraction): Key? {
		if (key != null) {
			//add key to list. create list if needed
			key.parent = this
			if (privateKeys == null)
				privateKeys = Companion.beatEList()
			return privateKeys!!.set(key, beat)
		} else if (privateKeys != null) {
			//remove key from list. delete list if not needed any more.
			val ret = privateKeys!!.remove(beat)
			if (privateKeys!!.size() === 0)
				privateKeys = null
			return ret
		}
		return null
	}


	/**
	 * Adds a direction at the given beat. If there is already one, it is not
	 * replaced, since there may be many directions belonging to a single beat.
	 */
	@Untested
	fun addDirection(direction: Direction, beat: Fraction) {
		direction.parent = this
		if (directions == null)
			directions = Companion.beatEList()
		directions!!.add(direction, beat)
	}


	/**
	 * Adds an instrument change at the given beat.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	@Untested
	fun setInstrumentChange(instrumentChange: InstrumentChange?, beat: Fraction): InstrumentChange? {
		if (instrumentChange != null) {
			//add instrumentChange to list. create list if needed
			instrumentChange.setParent(this)
			if (instrumentChanges == null)
				instrumentChanges = Companion.beatEList()
			return instrumentChanges!!.set(instrumentChange, beat)
		} else if (instrumentChanges != null) {
			//remove instrumentChange from list. delete list if not needed any more.
			val ret = instrumentChanges!!.remove(beat)
			if (instrumentChanges!!.size() === 0)
				instrumentChanges = null
			return ret
		}
		return null
	}


	/**
	 * Adds the given [MeasureElement] at the given beat. Dependent on its type,
	 * it may replace elements of the same type, which is then returned (otherwise null).
	 * See the documentation for the methods working with specific [MeasureElement]s.
	 */
	@Untested
	fun addMeasureElement(element: MeasureElement, beat: Fraction): MeasureElement? {
		if (element is Clef)
			return setClef(element, beat)
		else if (element is Key)
			return setKey(element as Key, beat)
		else if (element is Direction) {
			addDirection(element as Direction, beat)
			return null
		} else return if (element is InstrumentChange)
			setInstrumentChange(element, beat)
		else
			throw IllegalArgumentException("Unknown MeasureElement subclass: " + element.javaClass.name)
	}


	/**
	 * Removes the given [MeasureElement].
	 */
	@Untested
	fun removeMeasureElement(element: MeasureElement) {
		if (element is Clef)
			clefs!!.remove(element)
		else if (element is Key)
			privateKeys!!.remove(element as Key)
		else if (element is Direction)
			directions!!.remove(element as Direction)
		else if (element is InstrumentChange)
			instrumentChanges!!.remove(element)
		else
			throw IllegalArgumentException("Unknown MeasureElement subclass: " + element.javaClass.name)
	}


	/**
	 * Replaces the given [MeasureElement] at the given beat with the other given one.
	 */
	@Untested
	fun <T : MeasureElement> replaceMeasureElement(oldElement: T, newElement: T, beat: Fraction) {
		if (oldElement is Direction) {
			directions!!.remove(oldElement as Direction)
			directions!!.add(newElement as Direction, beat)
		} else {
			//all other cases are like addMeasureElement
			addMeasureElement(newElement, beat)
		}
	}


	/**
	 * Collect the accidentals within this measure (backwards),
	 * beginning at the given start beat where the given key is valid, ending before or at
	 * the given beat (depending on the given interval), looking at all voices.
	 * The private keys of this measure are ignored. They must be queried before and
	 * used for the last two parameters.
	 *
	 * @param beat       the maximum beat (inclusive if exclusive, depending on the interval)
	 * @param interval   where to stop looking ([Interval.Before] or
	 * [Interval.BeforeOrAt]). [Interval.At] is
	 * handled like [Interval.BeforeOrAt].
	 * @param startBeat  the beat where to start collecting accidentals (if there are
	 * no private keys in this measure before the given beat, this
	 * is always 0).
	 * @param startBeatKey  the key that is valid at the given start beat
	 * @return a map with the pitches that have accidentals (without alter)
	 * as keys and their corresponding alter values as values.
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
