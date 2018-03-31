package com.xenoage.zong.core.header

import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math._0
import com.xenoage.utils.throwEx
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.format.Break
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureSide
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.barline.BarlineRepeat.Both
import com.xenoage.zong.core.music.direction.DaCapo
import com.xenoage.zong.core.music.direction.NavigationSign
import com.xenoage.zong.core.music.direction.Tempo
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.util.*
import com.xenoage.zong.core.music.volta.Volta
import com.xenoage.zong.core.position.Beat
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atColumnBeat
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement

/**
 * A [ColumnHeader] stores information that
 * is used for the whole measure column,
 * i.e. key signature and time signature.
 *
 * The start and end barline as well as middle barlines
 * are saved here, and a volta if it begins in this
 * measure.
 *
 * There is also a list of tempo directions for this
 * measure and layout break information.
 */
class ColumnHeader(
		/** Back reference to the parent score.  */
		val parentScore: Score
) : MPContainer {

	/** The time signature at the beginning of this measure */
	var time: TimeSignature? = null; private set

	/** The barline at the beginning of this measure */
	var startBarline: Barline? = null

	/** The barline at the end of this measure */
	var endBarline: Barline? = null

	/** The barlines in the middle of the measure */
	var middleBarlines: BeatEList<Barline> = BeatEList()

	/** The volta that begins at this measure */
	var volta: Volta? = null

	/** The key signature changes in this measure */
	var keys: BeatEList<Key> = BeatEList()

	/** The tempo changes in this measure */
	var tempos: BeatEList<Tempo> = BeatEList()

	/** The [Break] after this measure, or null.  */
	var measureBreak: Break? = null

	/** The [NavigationSign] at the beginning of this measure, or null */
	var navigationTarget: NavigationSign? = null

	/** The [NavigationSign] at the end of this measure, or null.  */
	var navigationOrigin: NavigationSign? = null

	/** Back reference: measure index, or -1 if not part of a score.  */
	val parentMeasureIndex: Int =
			parentScore.header.columnHeaders.indexOf(this)

	/**
	 * A sequence of all [ColumnElement]s in this column, which
	 * are assigned to a beat (middle barlines, keys and tempos).
	 */
	val columnElementsWithBeats: Sequence<BeatE<out ColumnElement>>
		get() = middleBarlines.asSequence() + keys.asSequence() + tempos.asSequence()

	/**
	 * A sequence of all [ColumnElement]s in this column, which
	 * are not assigned to a beat (time, start and end barline, volta, measure break).
	 */
	val columnElementsWithoutBeats: Sequence<out ColumnElement>
		get() = sequenceOf(time, startBarline, endBarline, volta, measureBreak).filterNotNull()

	/**
	 * Sets the time signature, or null if unset.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setTime(time: TimeSignature?) =
			replace({ this.time = it }, this.time, time)

	/**
	 * Sets the barline at the beginning of this measure, or null if unset.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setStartBarline(startBarline: Barline?) =
			replace({ this.startBarline = it}, this.startBarline, checkStartBarline(startBarline))

	/**
	 * Sets the barline at the end of this measure, or null if unset.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setEndBarline(endBarline: Barline?) =
			replace({ this.endBarline = it}, this.endBarline, checkEndBarline(endBarline))

	/**
	 * Sets a barline in the middle of the measure (or null to remove it).
	 * If there is already one at the given beat, it is replaced and returned (otherwise null).
	 */
	fun setMiddleBarline(middleBarline: Barline?, beat: Fraction) =
			replace(middleBarlines, beat, middleBarline)

	/**
	 * Sets the volta beginning at this measure, or null if unset.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setVolta(volta: Volta) =
			replace({ this.volta = it }, this.volta, volta)

	/**
	 * Sets a key in this measure (or null to remove it).
	 * If there is already one at the given beat, it is replaced and returned (otherwise null).
	 */
	fun setKey(key: Key?, beat: Fraction) =
			replace(this.keys, beat, key)

	/**
	 * Sets a tempo in this measure (or null to remove it)
	 * If there is already one at the given beat, it is replaced and returned (otherwise null).
	 */
	fun setTempo(tempo: Tempo?, beat: Fraction) =
			replace(this.tempos, beat, tempo)

	/**
	 * Sets the [Break] after this measure, or null if there is none.
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setBreak(measureBreak: Break?) =
			replace({ this.measureBreak = measureBreak}, this.measureBreak, measureBreak)

	/**
	 * Sets the [NavigationSign] (or null to remove it) at the beginning of a measure
	 * into which can be jumped, e.g. a jump indicated by "segno" or "coda".
	 * If there is already one, it is replaced and returned (otherwise null).
	 * Setting a "capo" is not possible here.
	 */
	fun setNavigationTarget(sign: NavigationSign?): NavigationSign? {
		if (sign is DaCapo)
			throw IllegalArgumentException("DaCapo can not be a navigation target")
		return replace({ this.navigationTarget = it}, this.navigationTarget, sign)
	}

	/**
	 * Sets the [NavigationSign] (or null to remove it) to perform after this measure,
	 * e.g. a jump indicated by "da capo", "dal segno" or "to coda".
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setNavigationOrigin(sign: NavigationSign?) =
			replace({ this.navigationOrigin = it}, this.navigationOrigin, sign)

	/**
	 * Checks the given start barline.
	 */
	private fun checkStartBarline(startBarline: Barline?): Barline? {
		//both side repeat is not allowed
		if (startBarline != null && startBarline.repeat === Both)
			throw IllegalArgumentException("${Both} is not supported for a start barline.")
		return startBarline
	}

	/**
	 * Checks the given end barline.
	 */
	private fun checkEndBarline(endBarline: Barline?): Barline? {
		//both side repeat is not allowed
		if (endBarline != null && endBarline.repeat === Both)
			throw IllegalArgumentException("${Both} is not supported for an end barline.")
		return endBarline
	}

	/**
	 * Sets the given [ColumnElement] at the given beat.
	 *
	 * If there is already another element of this type,
	 * it is replaced and returned (otherwise null).
	 *
	 * @param element  the element to set
	 * @param beat     the beat where to add the element. Only needed for
	 *                 key, tempo, middle barlines and navigation signs
	 * @param side     Only needed for barlines
	 */
	@Untested
	fun setColumnElement(element: ColumnElement, beat: Fraction? = null,
	                     side: MeasureSide? = null): ColumnElement? {
		return when (element) {
			is Barline -> {
				//left or right barline
				if (side == MeasureSide.Left)
					setStartBarline(element)
				else if (side == MeasureSide.Right)
					setEndBarline(element)
				else //middle barline
					setMiddleBarline(element, checkNotNull(beat))
			}
			is Break -> setBreak(element)
			is Key -> setKey(element, checkNotNull(beat))
			is Tempo -> setTempo(element, checkNotNull(beat))
			is TimeSignature -> setTime(element)
			is Volta -> setVolta(element)
			is NavigationSign -> {
				//write at beginning of measure (jump target), when beat is 0,
				//otherwise write to end of measure (jump origin)
				if (checkNotNull(beat).is0)
					setNavigationTarget(element)
				else
					setNavigationOrigin(element)
			}
			else -> throwEx()
		}
	}

	/**
	 * Removes the given [ColumnElement].
	 */
	@Untested
	fun removeColumnElement(element: ColumnElement) {
		element.parent = null
		when (element) {
			is Barline -> {
				//left or right barline
				if (element === startBarline)
					startBarline = null
				else if (element === endBarline)
					endBarline = null
				else
					middleBarlines.remove(element) //middle barline
			}
			is Break -> measureBreak = null
			is Key -> keys.remove(element)
			is Tempo -> tempos.remove(element)
			is TimeSignature -> time = null
			is Volta -> volta = null
			else -> throwEx()
		}
	}

	/**
	 * Replaces the given [ColumnElement] with the other given one.
	 */
	@Untested
	fun <T : ColumnElement> replaceColumnElement(oldElement: T, newElement: T) {
		when (newElement) {
			is Barline -> {
				//left or right barline
				if (oldElement === startBarline)
					setStartBarline(newElement)
				else if (oldElement === endBarline)
					setEndBarline(newElement)
				else {
					//middle barline
					for (middleBarline in middleBarlines) {
						if (middleBarline.element === oldElement) {
							setMiddleBarline(newElement, middleBarline.beat)
							return
						}
					}
					throwEx("Old barline not part of this column")
				}
			}
			is Break -> setBreak(newElement)
			is Key -> {
				for (key in keys) {
					if (key.element === oldElement) {
						setKey(newElement, key.beat)
						return
					}
				}
				throwEx("Old key not part of this column")
			}
			is Tempo -> {
				for (tempo in tempos) {
					if (tempo.element === oldElement) {
						setTempo(newElement as Tempo, tempo.beat)
						return
					}
				}
				throwEx("Old tempo not part of this column")
			}
			is TimeSignature -> setTime(newElement)
			is Volta -> setVolta(newElement)
			else -> throwEx()
		}
	}

	/**
	 * Gets the [MP] of the given [ColumnElement], or null if it is not part
	 * of this column or this column is not part of a score.
	 */
	override fun getChildMP(element: MPElement): MP? {
		val score = parentScore
		val measureIndex = parentMeasureIndex
		if (score === null || measureIndex === null)
			return null
		//elements at the beginning of the measure
		if (time === element || startBarline === element || volta === element)
			return atColumnBeat(measureIndex, _0)
		//elements at the end of the measure
		else if (endBarline === element || measureBreak === element)
			return atColumnBeat(measureIndex, score.getMeasureBeats(measureIndex))
		//elements in the middle of the measure
		else if (element is Barline)
			return getMPIn(element, middleBarlines)
		else if (element is Key)
			return getMPIn(element, keys)
		else if (element is Tempo)
			return getMPIn(element, tempos)
		return null
	}

	/**
	 * Gets the [MP] of the given element within the given list of elements,
	 * or null if the list of elements is null or the element could not be found.
	 */
	private fun getMPIn(element: MPElement, elements: BeatEList<*>?): MP? =
			parentMeasureIndex?.let { measure ->
				elements?.find { it.element === element }?.let { atColumnBeat(measure, it.beat) } }

	/**
	 * Gets the [MeasureSide] of the given element in this column. This applies only to
	 * start and end barlines. For all other elements, null is returned.
	 */
	fun getSide(element: ColumnElement) = when {
		element === startBarline ->	MeasureSide.Left
		element === endBarline -> MeasureSide.Right
		else ->	null
	}

	/**
	 * Calls the given function to update the given property, and updates the parent
	 * of the given old and new property value to null and this instance. The old value is returned.
	 */
	private fun <T : ColumnElement> replace(propertyUpdate: (T?) -> Unit, oldValue: T?, newValue: T?): T? {
		propertyUpdate(newValue)
		newValue?.parent = this
		oldValue?.parent = null
		return oldValue
	}

	/**
	 * For the given list, find the element at the given beat. If one is found, replace it by the
	 * the given new value (or removes it if the new value is null) and sets the parent of the old value to null.
	 * The parent of the new value is set to this instance. The old value is returned.
	 */
	private fun <T : ColumnElement> replace(list: BeatEList<T>, beat: Beat, newValue: T?): T? {
		var oldValue: T? = if (newValue != null) list.set(newValue, beat) else list.remove(beat)
		newValue?.parent = this
		oldValue?.parent = null
		return oldValue
	}

}