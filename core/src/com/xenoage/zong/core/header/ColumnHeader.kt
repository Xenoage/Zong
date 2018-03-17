package com.xenoage.zong.core.header

import com.xenoage.utils.annotations.Untested
import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math._0
import com.xenoage.utils.mutableListOfNotNull
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.format.Break
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureSide
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.barline.Barline
import com.xenoage.zong.core.music.barline.BarlineRepeat.Both
import com.xenoage.zong.core.music.direction.*
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.time.TimeSignature
import com.xenoage.zong.core.music.util.BeatEList
import com.xenoage.zong.core.music.volta.Volta
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
class ColumnHeader : DirectionContainer, MPContainer {

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
	var navigationTarget: Direction? = null

	/** The [NavigationSign] at the end of this measure, or null.  */
	var navigationOrigin: Direction? = null

	/** The other [Direction]s in this measure  */
	var otherDirections: BeatEList<Direction> = BeatEList()

	/** Back reference: parent score, or null if not part of a score.  */
	var parentScore: Score? = null

	/** Back reference: measure index, or null if not part of a score.  */
	var parentMeasureIndex: Int? = null

	/**
	 * Gets a list of all [ColumnElement]s in this column, which
	 * are assigned to a beat (middle barlines, keys and tempos).
	 */
	val columnElementsWithBeats: BeatEList<ColumnElement>
		get() {
			val ret = BeatEList<ColumnElement>()
			ret.addAll(middleBarlines)
			ret.addAll(keys)
			ret.addAll(tempos)
			return ret
		}

	/**
	 * The list of all [ColumnElement]s in this column, which
	 * are not assigned to a beat (time, start and end barline, volta, measure break).
	 */
	val columnElementsWithoutBeats: List<ColumnElement>
		get() = listOfNotNull(time, startBarline, endBarline, volta, measureBreak)

	/**
	 * The list of all [ColumnElement]s in this column.
	 */
	val columnElements: List<ColumnElement>
		get() {
			val ret = mutableListOfNotNull(time, startBarline, endBarline, volta, measureBreak)
			for (e in middleBarlines)
				ret.add(e.element)
			for (e in keys)
				ret.add(e.element)
			for (e in tempos)
				ret.add(e.element)
			return ret
		}

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
	fun setNavigationTarget(sign: NavigationSign?): Direction? {
		if (sign is DaCapo)
			throw IllegalArgumentException("DaCapo can not be a navigation target")
		return replace({ this.navigationTarget = it}, this.navigationTarget, sign as Direction?)
	}

	/**
	 * Sets the [NavigationSign] (or null to remove it) to perform after this measure,
	 * e.g. a jump indicated by "da capo", "dal segno" or "to coda".
	 * If there is already one, it is replaced and returned (otherwise null).
	 */
	fun setNavigationOrigin(sign: NavigationSign?) =
			replace({ this.navigationOrigin = it}, this.navigationOrigin, sign as Direction?)

	/**
	 * Adds the given [Direction] to this measure.
	 * For a [Tempo], use [setTempo] instead.
	 * For a [NavigationSign], use [setNavigationTarget] or [setNavigationOrigin] instead.
	 */
	fun addOtherDirection(direction: Direction, beat: Fraction) {
		if (direction.elementType in middleNotAllowedDirections)
			throw IllegalArgumentException("direction type not allowed at this position")
		direction.parent = this
		otherDirections.add(direction, beat)
	}

	/**
	 * Removes the given [Direction] from this measure.
	 * If found, it is returned (otherwise null).
	 * For a [Tempo], use [setTempo] with a null value instead.
	 */
	fun removeOtherDirection(direction: Direction): Direction? {
		val ret = otherDirections.remove(direction)
		ret?.parent = null
		return ret
	}

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
	 * If there is already another element of this type, it is replaced and returned (otherwise null),
	 * except for [Direction]s (except [Tempo]).
	 *
	 * @param element  the element to set
	 * @param beat     the beat where to add the element. Only needed for
	 *                 key, tempo, middle barlines and other directions
	 * @param side     Only needed for barlines
	 */
	@Untested
	fun setColumnElement(element: ColumnElement, beat: Fraction? = null,
	                     side: MeasureSide? = null): ColumnElement? {
		return when (element.elementType) {
			MusicElementType.Barline -> {
				val barline = element as Barline
				//left or right barline
				if (side == MeasureSide.Left)
					setStartBarline(barline)
				else if (side == MeasureSide.Right)
					setEndBarline(barline)
				else //middle barline
					setMiddleBarline(barline, checkNotNull(beat))
			}
			MusicElementType.Break -> {
				setBreak(element as Break)
			}
			MusicElementType.TraditionalKey -> {
				setKey(element as Key, checkNotNull(beat))
			}
			MusicElementType.Tempo -> {
				setTempo(element as Tempo, checkNotNull(beat))
			}
			MusicElementType.Time -> {
				setTime(element as TimeSignature)
			}
			MusicElementType.Volta -> {
				setVolta(element as Volta)
			}
			MusicElementType.Coda, MusicElementType.DaCapo, MusicElementType.Segno -> {
				//write at beginning of measure (jump target), when beat is 0,
				//otherwise write to end of measure (jump origin)
				if (checkNotNull(beat).is0)
					setNavigationTarget(element as NavigationSign)
				else
					setNavigationOrigin(element as NavigationSign)
			}
			else -> {
				if (element is Direction) {
					addOtherDirection(element, checkNotNull(beat))
					return null
				}
				else {
					throw IllegalArgumentException("Unknown element class for $element")
				}
			}
		}
	}

	/**
	 * Removes the given [ColumnElement].
	 */
	@Untested
	fun removeColumnElement(element: ColumnElement) {
		element.parent = null
		if (element is Barline) {
			//left or right barline
			if (element === startBarline)
				startBarline = null
			else if (element === endBarline)
				endBarline = null
			else
				middleBarlines.remove(element) //middle barline
		} else if (element is Break) {
			measureBreak = null
		} else if (element is Key) {
			keys.remove(element)
		} else if (element is Tempo) {
			tempos.remove(element)
		} else if (element is TimeSignature) {
			time = null
		} else if (element is Volta) {
			volta = null
		} else if (element is Direction) {
			otherDirections.remove(element)
		} else {
			throw IllegalStateException()
		}
	}

	/**
	 * Replaces the given [ColumnElement] with the other given one.
	 */
	@Untested
	fun <T : ColumnElement> replaceColumnElement(oldElement: T, newElement: T) {
		if (newElement is Barline) {
			val newBarline = newElement as Barline
			//left or right barline
			if (oldElement === startBarline)
				setStartBarline(newBarline)
			else if (oldElement === endBarline)
				setEndBarline(newBarline)
			else {
				//middle barline
				for (middleBarline in middleBarlines) {
					if (middleBarline.element === oldElement) {
						setMiddleBarline(newBarline, middleBarline.beat)
						return
					}
				}
				throw IllegalArgumentException("Old barline not part of this column")
			}
		} else if (newElement is Break) {
			setBreak(newElement as Break)
		} else if (newElement is Key) {
			for (key in keys) {
				if (key.element === oldElement) {
					setKey(newElement as Key, key.beat)
					return
				}
			}
			throw IllegalArgumentException("Old key not part of this column")
		} else if (newElement is Tempo) {
			for (tempo in tempos) {
				if (tempo.element === oldElement) {
					setTempo(newElement as Tempo, tempo.beat)
					return
				}
			}
			throw IllegalArgumentException("Old tempo not part of this column")
		} else if (newElement is TimeSignature) {
			setTime(newElement as TimeSignature)
		} else if (newElement is Volta) {
			setVolta(newElement as Volta)
		} else {
			throw IllegalStateException()
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
	private fun <T : ColumnElement> replace(list: BeatEList<T>, beat: Fraction, newValue: T?): T? {
		var oldValue: T? = if (newValue != null) list.set(newValue, beat) else list.remove(beat)
		newValue?.parent = this
		oldValue?.parent = null
		return oldValue
	}

	companion object {
		private val middleNotAllowedDirections = hashSetOf(
				MusicElementType.Tempo, MusicElementType.Coda, MusicElementType.DaCapo, MusicElementType.Segno)
	}

}
