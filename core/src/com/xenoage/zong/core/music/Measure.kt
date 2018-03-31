package com.xenoage.zong.core.music

import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.collections.SortedList
import com.xenoage.utils.collections.ifIndexFound
import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math._0
import com.xenoage.utils.collections.max
import com.xenoage.utils.collections.setExtendBy
import com.xenoage.utils.throwEx
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.direction.Direction
import com.xenoage.zong.core.music.direction.DirectionContainer
import com.xenoage.zong.core.music.direction.setParent
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.util.*
import com.xenoage.zong.core.music.util.Interval.*
import com.xenoage.zong.core.position.*
import com.xenoage.zong.utils.exceptions.IllegalMPException


/**
 * Measure within a single staff.
 *
 * A measure consists of one or more voices and a list of clefs, directions and and instrument changes.
 */
class Measure : MPElement, MPContainer, DirectionContainer {

	/** The list of voices (at least one).  */
	val voices = mutableListOf(Voice())

	/** The list of clefs.  */
	val clefs = BeatEList<Clef>()

	/** The list of directions, or null. */
	val directions = BeatEList<Direction>()

	/** The list of instrument changes, or null.  */
	val instrumentChanges = BeatEList<InstrumentChange>()

	/** Back reference: the parent staff, or null if not part of a staff. */
	override var parent: Staff? = null

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

	/** The sequence of all measure elements in this measure (clefs, directions, instrument changes),
	 *  but not sorted by beat. */
	val measureElements: Sequence<BeatE<out MPElement>> =
			clefs.asSequence() + directions.asSequence() + instrumentChanges.asSequence()

	/**
	 * Adds a clef at the given beat or removes it, when null is given.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setClef(clef: Clef?, beat: Beat): Clef? =
		if (clef != null)
			clefs.set(clef.setParent(this), beat).unsetParent()
		else
			clefs.remove(beat).unsetParent()

	/**
	 * Adds a direction at the given beat. If there is already one, it is not
	 * replaced, since there may be many directions belonging to a single beat.
	 */
	fun addDirection(direction: Direction, beat: Beat) {
		directions.add(direction.setParent(this), beat)
	}

	/**
	 * Adds an instrument change at the given beat.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setInstrumentChange(instrumentChange: InstrumentChange?, beat: Beat): InstrumentChange? =
		if (instrumentChange != null)
			instrumentChanges.set(instrumentChange.setParent(this), beat).unsetParent()
		else
			instrumentChanges.remove(beat).unsetParent()

	/**
	 * Adds a [MeasureElement] or [Direction] at the given beat.
	 * Dependent on the type, an old element at this beat may be removed, and
	 * is returned in this case.
	 */
	fun addMeasureElement(measureElement: MPElement, beat: Beat): MPElement? {
		when (measureElement) {
			is Clef -> return setClef(measureElement, beat)
			is InstrumentChange -> return setInstrumentChange(measureElement, beat)
			is Direction -> addDirection(measureElement, beat)
			else -> throwEx("Illegal measure element type")
		}
		return null
	}

	/**
	 * Collect the accidentals within this measure (backwards),
	 * beginning at the given start beat where the given key is valid, ending before or at
	 * the given beat (depending on the given interval), looking at all voices.
	 *
	 * @param beat          the maximum beat (inclusive or exclusive, depending on the interval)
	 * @param interval      where to stop looking ([Before] or [BeforeOrAt]). [At] is handled like [BeforeOrAt].
	 * @param startBeat     the start beat where the key is known
	 * @param startBeatKey  the key that is valid at the given start beat
	 * @return a map with the pitches that have accidentals (without alter) (as key) and
	 *         their corresponding alter values (as value).
	 */
	@Untested
	fun getAccidentals(beat: Fraction, interval: Interval, startBeat: Fraction, startBeatKey: Key): Map<Pitch, Int> {
		check(interval == Before || interval == BeforeOrAt || interval == At, { "Unsupported interval" })
		var interval = interval
		if (interval == At) interval = BeforeOrAt
		val ret = HashMap<Pitch, Int>()
		val retBeats = HashMap<Pitch, Fraction>()
		for (voice in voices) {
			var pos = startBeat
			for (e in voice.elements) {
				if (pos < startBeat) {
					pos += e.duration
					continue
				}
				if (interval.isInInterval(pos, beat) != Interval.Result.True) {
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
							if (pitch.alter != oldAccAlter && (existingBeat == null || pos > existingBeat)) {
								ret[pitchUnaltered] = pitch.alter
								retBeats[pitchUnaltered] = pos
							}
						} else {
							//accidental not neccessary because of key?
							if (startBeatKey.alterations[pitch.step.ordinal] == pitch.alter) {
								//ok, we need no accidental here.
							} else {
								//add accidental
								ret[pitchUnaltered] = pitch.alter
								retBeats[pitchUnaltered] = pos
							}
						}
					}
				}
				pos += e.duration
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
	fun getUsedBeats(withMeasureElements: Boolean): SortedList<Beat> {
		var ret = SortedList<Beat>(false)
		//voice element beats
		voices.forEach { it.usedBeats.forEach { ret.add(it) } }
		//beats of directions
		if (withMeasureElements) {
			for ((_, beat) in measureElements)
				ret.add(beat)
		}
		return ret
	}


	/** Gets the voice with the given index, or throws an [IllegalMPException] if there is none. */
	fun getVoice(index: Int): Voice =
			voices.getOrNull(index) ?: throw IllegalMPException(mp.withVoice(index))

	/**
	 * Gets the voice with the given index, or throws an [IllegalMPException] if there is none.
	 * Only the voice index of the given position is relevant.
	 */
	fun getVoice(mp: MP): Voice =
			voices.getOrNull(mp.voice) ?: throw IllegalMPException(this.mp.withVoice(mp.voice))

	/** Sets the voice with the given index. If the voice does not exist yet, it is created. */
	fun setVoice(index: Int, voice: Voice) {
		voices.setExtendBy(index, voice.setParent(this), { Voice().setParent(this) })
	}

	/**
	 * Gets the [MP] of the given [Voice], or null if it is not part of this measure.
	 */
	fun getMP(voice: Voice): MP? = getChildMP(voice)

	/*
	 * Gets the [MP] of the given element within the given list of elements,
	 * based on the given [MP] (staff, measure), or null if the list of elements
	 * is null or the element could not be found.
	 * OBSOLETE?/
	private fun getMPIn(element: MPElement, elements: BeatEList<*>?, baseMP: MP?): MP? {
		if (elements == null)
			return null
		for ((element1, beat) in elements)
			if (element1 === element)
				return MP.atBeat(baseMP!!.staff, baseMP.measure, baseMP.voice, beat)
		return null
	} */

	override fun getChildMP(child: MPElement): MP? =
			voices.indexOfFirst { it == child }.ifIndexFound { mp.withVoice(it) }

}
