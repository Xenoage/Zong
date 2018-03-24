package com.xenoage.zong.core

import com.xenoage.utils.collections.SortedList
import com.xenoage.utils.document.Document
import com.xenoage.utils.document.command.CommandPerformer
import com.xenoage.utils.document.io.SupportedFormats
import com.xenoage.utils.math._0
import com.xenoage.utils.max
import com.xenoage.zong.core.format.ScoreFormat
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.header.ScoreHeader
import com.xenoage.zong.core.info.ScoreInfo
import com.xenoage.zong.core.music.*
import com.xenoage.zong.core.music.MusicContext.Companion.noAccidentals
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.clef.ClefType
import com.xenoage.zong.core.music.clef.clefTreble
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.key.TraditionalKey
import com.xenoage.zong.core.music.key.TraditionalKey.Mode
import com.xenoage.zong.core.music.util.*
import com.xenoage.zong.core.music.util.Interval.At
import com.xenoage.zong.core.music.util.Interval.BeforeOrAt
import com.xenoage.zong.core.position.Beat
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.atBeat
import com.xenoage.zong.core.position.MP.Companion.atColumnBeat
import com.xenoage.zong.core.position.MP.Companion.atMeasure
import com.xenoage.zong.core.position.MP.Companion.mpb0
import com.xenoage.zong.core.position.MP.Companion.unknown
import com.xenoage.zong.core.position.MPElement
import java.util.*


/**
 * A single piece of written music.
 */
class Score : Document {

	/** General information about the score.  */
	var info = ScoreInfo()

	/** The default formats of the score.  */
	var format = ScoreFormat()

	/** The list of elements that are normally used in all staves, like time signatures and key signatures.  */
	var header = ScoreHeader(this)

	/** The list of staves, parts and groups.  */
	var stavesList = StavesList(this)

	/** Additional meta information. This other application-dependend meta-information
	 * can be used for example to store page layout information, which is not part
	 * of the musical score in this project. */
	private val metaData = HashMap<String, Any>()

	/** Performs commands on this score and supports undo.  */
	override val commandPerformer = CommandPerformer(this)

	/** Supported formats for reading scores from files and writing them to files. */
	override var supportedFormats: SupportedFormats<Score>? = null

	/** The number of staves. */
	val stavesCount: Int
		get() = stavesList.staves.size

	/** The number of measures. */
	val measuresCount: Int
		get() = this.header.columnHeaders.size

	/** The biggest interline space of the score. */
	val maxInterlineSpace: IS
		get() = stavesList.staves.max({ it.interlineSpace }, 0f)

	/** The number of divisions of a quarter note within the whole score.
	 *  This is e.g. needed for Midi and MusicXML Export. */
	fun computeDivisions(): Int =
			stavesList.computeDivisions()

	/** Sets the given meta-data information. */
	fun setMetaData(key: String, value: Any) {
		metaData[key] = value
	}

	/** Gets the measure column header at the given measure. */
	fun getColumnHeader(measureIndex: Int): ColumnHeader =
		header.getColumnHeader(measureIndex)

	/** Gets the measure at the given [MP]. */
	fun getMeasure(mp: MP): Measure =
			stavesList.getMeasure(mp)

	/** Gets the staff with the given index. */
	fun getStaff(staffIndex: Int): Staff =
			stavesList.getStaff(staffIndex)

	/** Gets the staff at the given [MP]. */
	fun getStaff(mp: MP): Staff =
		stavesList.getStaff(mp)

	/** The voice at the given [MP]. */
	fun getVoice(mp: MP): Voice =
			stavesList.getVoice(mp)

	/** The interline space for the staff with the given index.
	 * If unknown, the default value is returned.
	 */
	fun getInterlineSpace(staffIndex: Int): IS =
			getStaff(staffIndex).interlineSpace

	/** Returns true, if the given [MP] is in a valid range, otherwise false. */
	fun isMPExisting(mp: MP): Boolean {
		if (false == (mp.staff in 0 until stavesCount && mp.measure in 0 until measuresCount &&
				mp.voice in getMeasure(mp).voices.indices))
			return false
		val measure = getMeasure(mp)
		val voice = getVoice(mp)
		if (mp.element != MP.unknown && mp.element >= voice.elements.size)
			return false
		if (mp.beat != MP.unknownBeat && (mp.beat!! < _0 || measure.filledBeats < mp.beat))
			return false
		return true
	}

	/**
	 * Gets the number of beats in the given measure column.
	 * If a time signature is defined, its beats are returned.
	 * If the time signature is unknown (senza-misura), the beats of the
	 * voice with the most beats are returned.
	 */
	fun getMeasureBeats(measureIndex: Int): Beat {
		val ret = this.header.getTimeAtOrBefore(measureIndex).type.measureBeats
		return ret ?: getMeasureFilledBeats(measureIndex)
	}

	/**
	 * Gets the filled beats for the given measure column, that
	 * means, the first beat in each column where there is no music
	 * element following any more.
	 * The given measure may be one measure after the last measure. There, only beat 0
	 * exists to mark the ending of the score.
	 */
	fun getMeasureFilledBeats(measureIndex: Int): Beat =
		if (measureIndex == measuresCount) _0 else
			stavesList.staves.max({ it.measures[measureIndex].filledBeats }, _0)

	/**
	 * Gets the used beats within the given measure column.
	 * The given measure may be one measure after the last measure. There, only beat 0
	 * exists to mark the ending of the score.
	 * @param measureIndex                  the index of the measure column
	 * @param withMeasureAndColumnElements  true, iff also the beats of column elements an
	 * measure elements should be used
	 */
	fun getMeasureUsedBeats(measureIndex: Int, withMeasureAndColumnElements: Boolean): SortedList<Beat> {
		//last measure?
		if (measureIndex == measuresCount)
			return SortedList<Beat>(false, _0)
		//add measure beats
		val columnBeats = SortedList<Beat>(false)
		for (iStaff in stavesList.staves.indices) {
			val measure = getMeasure(atMeasure(iStaff, measureIndex))
			val beats = measure.getUsedBeats(withMeasureAndColumnElements)
			columnBeats.addAll(beats)
		}
		//add column beats
		if (withMeasureAndColumnElements) {
			for ((_, beat) in getColumnHeader(measureIndex).columnElementsWithBeats)
				columnBeats.add(beat)
		}
		return columnBeats
	}

	/**
	 * Gets the last [Key] that is defined before (or at, dependent on the given [Interval]) the given
	 * [MP], also over measure boundaries. If there is none, a default C major time signature is returned.
	 */
	fun getKey(mp: MP, interval: Interval): MPE<out Key> {
		check(interval.isPrecedingOrAt, { "Illegal interval for this method" })
		check(mp.beat != null, { "Beat must be known" })
		var columnKey = this.header.getColumnHeader(mp.measure).keys.getLastBefore(interval, mp.beat!!)
		if (columnKey != null)
			return MPE(columnKey.element, mp.copy(beat = columnKey.beat))
		if (interval !== At) {
			//search in the preceding measures
			for (iMeasure in mp.measure - 1 downTo 0) {
				columnKey = this.header.getColumnHeader(iMeasure).keys.lastOrNull()
				if (columnKey != null)
					return MPE(columnKey.element, mp.copy(measure = iMeasure, beat = columnKey.beat))
			}
		}
		//no key found. return default key.
		return MPE(TraditionalKey(0, Mode.Major), mpb0)
	}

	/**
	 * Gets the accidentals at the given [MP] that are
	 * valid before or at the given beat (depending on the given interval),
	 * looking at all voices. The beat in the [MP] is required.
	 */
	fun getAccidentals(mp: MP, interval: Interval): Map<Pitch, Int> {
		check(mp.beat != MP.unknownBeat, { "beat is required" })
		//start key of the measure always counts
		val key = getKey(mp, BeforeOrAt)
		//if key change is in same measure, start at that beat. otherwise start at beat 0.
		val keyBeat = if (key.mp.measure == mp.measure) key.mp.beat else _0
		return getMeasure(mp).getAccidentals(mp.beat!!, interval, keyBeat!!, key.element)
	}

	/**
	 * Gets the last [Clef] that is defined before (or at, dependent on the given [Interval]) the given
	 * [MP], also over measure boundaries. If there is none, a default g clef is returned. The beat in the [MP] is required.
	 */
	fun getClef(mp: MP, interval: Interval): ClefType {
		check(interval.isPrecedingOrAt, { "illegal interval for this method" })
		check(mp.beat == MP.unknownBeat, { "beat is required" })
		//begin with the given measure. if there is one, return it.
		var measure = getMeasure(mp)
		if (measure.clefs != null) {
			val ret = measure.clefs.getLastBefore(interval, mp.beat!!)
			if (ret != null)
				return ret.element.type
		}
		if (interval !== At) {
			//search in the preceding measures
			for (iMeasure in mp.measure - 1 downTo 0) {
				measure = getMeasure(atMeasure(mp.staff, iMeasure))
				if (measure.clefs != null) {
					val ret = measure.clefs.lastOrNull()
					if (ret != null)
						return ret.element.type
				}
			}
		}
		//no clef found. return default clef.
		return clefTreble
	}

	/**
	 * Gets the [MusicContext] that is defined before (or at, dependent on the given [Interval]s) the given
	 * [MP], also over measure boundaries.
	 *
	 * Calling this method can be quite expensive, so call only when neccessary.
	 *
	 * @param mp                   the musical position
	 * @param clefAndKeyInterval   where to look for the last clef and key: [Before] ignores a clef and a key
	 *                             at the given position, [BeforeOrAt] and [At] (meaning the same here) include it
	 * @param accidentalsInterval  the same as for [clefAndKeyInterval], but for the accidentals.
	 *                             If null, no accidentals are collected.
	 */
	fun getMusicContext(mp: MP, clefAndKeyInterval: Interval, accidentalsInterval: Interval?): MusicContext {
		var clefAndKeyInterval = clefAndKeyInterval
		if (clefAndKeyInterval === At)
			clefAndKeyInterval = BeforeOrAt //At and BeforeOrAt mean the same in this context
		val clef = getClef(mp, clefAndKeyInterval)
		val key = getKey(mp, clefAndKeyInterval).element
		val accidentals = if (accidentalsInterval != null) getAccidentals(mp, accidentalsInterval) else noAccidentals
		return MusicContext(clef, key, accidentals, getStaff(mp).linesCount)
	}

	/** Gets the measures of the column with the given index. */
	fun getColumn(measureIndex: Int): Column =
			stavesList.staves.map { it.getMeasure(measureIndex) }

	/**
	 * Gets the interline space of the staff with the given index.
	 * If unspecified, the default value of the score is returned.
	 */
	fun getInterlineSpace(mp: MP): IS =
			getStaff(mp.staff).interlineSpace

	/**
	 * Gets the gap in beats between the end of the left element and
	 * the start of the right element. If it can not be determined, because
	 * the musical position of one of the elements is unknown, null is returned.
	 */
	fun getGapBetween(left: VoiceElement, right: VoiceElement): Duration? {
		var mpLeft = left.mp ?: return null
		mpLeft = mpLeft.copy(beat = mpLeft.beat!! + left.duration)
		val mpRight = right.mp ?: return null
		if (mpRight.measure == mpLeft.measure) {
			//simple case: same measure. just subtract beats
			return mpRight.beat!! - mpLeft.beat!!
		} else if (mpRight.measure > mpLeft.measure) {
			//right element is in a following measure. add the following:
			//- beats between end of left element and its measure end
			//- beats in the following measures (until the measure which contains the right element)
			//- start beat of the right element in its measure
			var gap = getMeasureFilledBeats(mpLeft.measure) - mpLeft.beat!!
			for (iMeasure in (mpLeft.measure+1 .. mpRight.measure-1))
				gap += getMeasureFilledBeats(iMeasure)
			gap += mpRight.beat!!
			return gap
		} else {
			//right element is not really at the right, but actually before the left element
			//add the following and sign with minus:
			//- betweens between the start of the right element and the end of its measure
			//- beats in the following measures (until the measure which contains the left element)
			//- end beat of the left element in its measure
			var gap = getMeasureFilledBeats(mpRight.measure) - mpRight.beat!!
			for (iMeasure in (mpRight.measure+1 .. mpLeft.measure-1))
				gap += getMeasureFilledBeats(iMeasure)
			gap += mpLeft.beat!!
			gap *= -1
			return gap
		}
	}

	/**
	 * A sequence with all [ColumnElement]s in a score.
	 * The elements within a column may be unsorted by beat, but first the elements with known beats are
	 * returned, then the elements without beats.
	 */
	fun getAllColumnElements(): Sequence<MPE<ColumnElement>> =
			header.columnHeaders.indices.asSequence().flatMap { columnIndex ->
				val column = header.columnHeaders[columnIndex]
				column.columnElementsWithBeats.map { MPE(it.element, atColumnBeat(columnIndex, it.beat)) } +
					column.columnElementsWithoutBeats.map { MPE(it, atMeasure(columnIndex)) }
			}

	/**
	 * A sequence with all measure elements in a score, by staff and measure.
	 * The elements within a measure may be unsorted by beat.
	 */
	fun getAllMeasureElements(): Sequence<MPE<MPElement>> =
			stavesList.staves.indices.asSequence().flatMap { staffIndex ->
				val staff = stavesList.staves[staffIndex]
				staff.measures.indices.asSequence().flatMap { measureIndex ->
					val measure = staff.measures[measureIndex]
					measure.measureElements.map { MPE(it.element, atBeat(staffIndex, measureIndex, unknown, it.beat)) }
				}
			}

	/**
	 * A sequence with all [VoiceElement]s in a score,
	 * by staff, measure, voice and beat.
	 */
	fun getAllVoiceElements(): Sequence<MPE<VoiceElement>> =stavesList.staves.indices.asSequence().flatMap { staffIndex ->
		val staff = stavesList.staves[staffIndex]
		staff.measures.indices.asSequence().flatMap { measureIndex ->
			val measure = staff.measures[measureIndex]
			measure.voices.indices.asSequence().flatMap { voiceIndex ->
				val voice = measure.voices[voiceIndex]
				voice.elementsWithBeats.mapIndexed { elementIndex, e ->
					MPE(e.element, MP(staffIndex, measureIndex, voiceIndex, e.beat, elementIndex)) }
				}
			}
		}

	/**
	 * A sequence with all [Beam]s in a score,
	 * by staff, measure, voice and beat (regarding the start chord).
	 */
	fun getAllBeams(): Sequence<MPE<Beam>> =
			getAllVoiceElements().filter { it.element is Chord && it.element.beam != null}.map {
				MPE((it.element as Chord).beam!!, it.mp)
			}

}
