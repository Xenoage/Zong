package com.xenoage.zong.core

import com.xenoage.utils.annotations.NonNull
import com.xenoage.utils.collections.SortedList
import com.xenoage.utils.document.Document
import com.xenoage.utils.document.command.CommandPerformer
import com.xenoage.utils.document.io.SupportedFormats
import com.xenoage.utils.math.Fraction
import com.xenoage.utils.math.MathUtils
import com.xenoage.zong.core.format.ScoreFormat
import com.xenoage.zong.core.header.ColumnHeader
import com.xenoage.zong.core.header.ScoreHeader
import com.xenoage.zong.core.info.ScoreInfo
import com.xenoage.zong.core.music.*
import com.xenoage.zong.core.music.clef.Clef
import com.xenoage.zong.core.music.clef.ClefType
import com.xenoage.zong.core.music.key.Key
import com.xenoage.zong.core.music.key.TraditionalKey
import com.xenoage.zong.core.music.key.TraditionalKey.Mode
import com.xenoage.zong.core.music.util.*
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.utils.exceptions.IllegalMPException
import lombok.Getter
import lombok.Setter
import lombok.`val`

import java.util.HashMap

import com.xenoage.utils.kernel.Range.range
import com.xenoage.utils.kernel.Range.rangeReverse
import com.xenoage.utils.math.Fraction._0
import com.xenoage.utils.math.Fraction.fr
import com.xenoage.utils.max
import com.xenoage.zong.core.header.ScoreHeader.scoreHeader
import com.xenoage.zong.core.music.MusicContext.noAccidentals
import com.xenoage.zong.core.music.util.BeatE.selectLatest
import com.xenoage.zong.core.music.util.Interval.At
import com.xenoage.zong.core.music.util.Interval.BeforeOrAt
import com.xenoage.zong.core.music.util.MPE.mpE
import com.xenoage.zong.core.position.MP.atMeasure
import com.xenoage.zong.core.position.MP.Companion.mpb0


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
		get() = this.stavesList.staves.size


	/** The number of measures. */
	val measuresCount: Int
		get() = this.header.columnHeaders.size

	/** The biggest interline space of the score. */
	val maxIS: IS
		get() = stavesList.staves.max({ it.interlineSpace }, 0f)

	/**
	 * Returns the number of divisions of a quarter note within the whole score.
	 * This is e.g. needed for Midi and MusicXML Export.
	 */
	val divisions: Int
		get() {
			var actualdivision = 4
			for (staff in this.stavesList.staves) {
				for (measure in staff.measures) {
					for (voice in measure.voices) {
						for (e in voice.elements) {
							actualdivision = MathUtils.INSTANCE.lcm(actualdivision, e.duration.denominator)
						}
					}
				}
			}
			return actualdivision / 4
		}

	init {
		this.stavesList.setScore(this)
	}


	/**
	 * Sets the given meta-data information.
	 */
	fun setMetaData(key: String, value: Any) {
		metaData[key] = value
	}


	/**
	 * Gets the measure column header at the given measure.
	 */
	fun getColumnHeader(measureIndex: Int): ColumnHeader {
		return this.header.getColumnHeader(measureIndex)
	}


	/**
	 * Gets the measure at the given [BMP].
	 */
	fun getMeasure(mp: MP): Measure {
		return this.stavesList.getMeasure(mp)
	}


	/**
	 * Gets the staff with the given index.
	 */
	fun getStaff(staffIndex: Int): Staff {
		return this.stavesList.getStaff(staffIndex)
	}


	/**
	 * Gets the staff at the given [MP].
	 */
	fun getStaff(mp: MP): Staff {
		return this.stavesList.getStaff(mp)
	}


	/**
	 * Gets the voice at the given [MP].
	 */
	fun getVoice(mp: MP): Voice {
		return this.stavesList.getVoice(mp)
	}


	/**
	 * Gets the interline space for the staff with the given index.
	 * If unknown, the default value is returned.
	 */
	fun getInterlineSpace(staffIndex: Int): Float {
		val `is` = getStaff(staffIndex).interlineSpace
		return `is` ?: this.format.interlineSpace
	}


	/**
	 * Returns true, if the given [MP] is in a valid range, otherwise false.
	 */
	fun isMPExisting(mp: MP): Boolean {
		try {
			if (mp.staff < 0 || mp.staff >= stavesCount || mp.measure < 0 || mp.measure >= measuresCount ||
					mp.voice < 0 || mp.voice >= getMeasure(mp).voices.size)
				return false
			val voice = getVoice(mp)
			if (mp.element != MP.unknown && voice.elements.size <= mp.element)
				return false
			return if (mp.beat != MP.unknownBeat && (mp.beat!!.compareTo(Companion.get_0()) < 0 || mp.beat.compareTo(voice.parent!!.filledBeats) > 0)) false else true
		} catch (ex: IllegalMPException) {
			return false
		}

	}


	/**
	 * Gets the number of beats in the given measure column.
	 * If a time signature is defined, its beats are returned.
	 * If the time signature is unknown (senza-misura), the beats of the
	 * voice with the most beats are returned.
	 */
	fun getMeasureBeats(measureIndex: Int): Fraction {
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
	fun getMeasureFilledBeats(measureIndex: Int): Fraction {
		if (measureIndex == measuresCount)
			return Companion.get_0()
		var maxBeat = Fraction.get_0()
		for (staff in this.stavesList.staves) {
			val beat = staff.measures[measureIndex].filledBeats
			if (beat.compareTo(maxBeat) > 0)
				maxBeat = beat
		}
		return maxBeat
	}


	/**
	 * Gets the used beats within the given measure column.
	 * The given measure may be one measure after the last measure. There, only beat 0
	 * exists to mark the ending of the score.
	 * @param measureIndex                  the index of the measure column
	 * @param withMeasureAndColumnElements  true, iff also the beats of column elements and
	 * measure elements should be used
	 */
	fun getMeasureUsedBeats(measureIndex: Int, withMeasureAndColumnElements: Boolean): SortedList<Fraction> {
		//last measure?
		if (measureIndex == measuresCount)
			return SortedList<T>(arrayOf<Fraction>(Companion.get_0()), false)
		//add measure beats
		var columnBeats: SortedList<Fraction> = SortedList<T>(false)
		for (iStaff in range(stavesCount)) {
			val measure = getMeasure(Companion.atMeasure(iStaff, measureIndex))
			val beats = measure.getUsedBeats(withMeasureAndColumnElements)
			columnBeats = columnBeats.merge(beats, false)
		}
		//add column beats
		if (withMeasureAndColumnElements) {
			for ((_, beat) in getColumnHeader(measureIndex).columnElementsWithBeats)
				columnBeats.add(beat)
		}
		return columnBeats
	}


	/**
	 * Gets the last [Key] that is defined before (or at,
	 * dependent on the given [Interval]) the given
	 * [MP], also over measure boundaries. If there is
	 * none, a default C major time signature is returned.
	 * Private keys (in measure) override public keys (in measure column header).
	 */
	fun getKey(mp: MP, interval: Interval): MPE<out Key> {
		if (!interval.isPrecedingOrAt) {
			throw IllegalArgumentException("Illegal interval for this method")
		}
		//begin with the given measure. if there is one, return it.
		var columnKey = this.header.getColumnHeader(mp.measure).keys.getLastBefore(interval, mp.beat)
		var measureKey: BeatE<Key>? = null
		if (getMeasure(mp).getPrivateKeys() != null)
			measureKey = getMeasure(mp).getPrivateKeys().getLastBefore(interval, mp.beat)
		var ret = Companion.selectLatest(columnKey, measureKey)
		if (ret != null)
			return Companion.mpE(ret!!.element, mp.withBeat(ret!!.beat))
		if (interval !== At) {
			//search in the preceding measures
			for (iMeasure in mp.measure - 1 downTo 0) {
				columnKey = this.header.getColumnHeader(iMeasure).keys.getLast()
				val privateKeys = getMeasure(Companion.atMeasure(mp.staff, iMeasure)).getPrivateKeys()
				measureKey = if (privateKeys != null) privateKeys!!.getLast() else null
				ret = Companion.selectLatest(columnKey, measureKey)
				if (ret != null)
					return Companion.mpE(ret!!.element, mp.withMeasure(iMeasure).withBeat(ret!!.beat))
			}
		}
		//no key found. return default key.
		return Companion.mpE(TraditionalKey(0, Mode.Major), Companion.getMpb0())
	}


	/**
	 * Gets the accidentals at the given [MP] that are
	 * valid before or at the given beat (depending on the given interval),
	 * looking at all voices. The beat in the [MP] is required.
	 */
	fun getAccidentals(mp: MP, interval: Interval): Map<Pitch, Int> {
		if (mp.beat == MP.unknownBeat)
			throw IllegalArgumentException("beat is required")
		//start key of the measure always counts
		val key = getKey(mp, BeforeOrAt)
		//if key change is in same measure, start at that beat. otherwise start at beat 0.
		val keyBeat = if (key.mp.measure == mp.measure) key.mp.beat else Companion.get_0()
		val measure = getMeasure(mp)
		return measure.getAccidentals(mp.beat!!, interval, keyBeat!!, key.element)
	}


	/**
	 * Gets the last [Clef] that is defined before (or at,
	 * dependent on the given [Interval]) the given
	 * [MP], also over measure boundaries. If there is
	 * none, a default g clef is returned. The beat in the [MP] is required.
	 */
	fun getClef(mp: MP, interval: Interval): ClefType {
		if (!interval.isPrecedingOrAt)
			throw IllegalArgumentException("Illegal interval for this method")
		if (mp.beat == MP.unknownBeat)
			throw IllegalArgumentException("beat is required")
		//begin with the given measure. if there is one, return it.
		var measure = getMeasure(mp)
		var ret: BeatE<Clef>? = null
		if (measure.clefs != null) {
			ret = measure.clefs.getLastBefore(interval, mp.beat)
			if (ret != null)
				return ret.element.type
		}
		if (interval !== At) {
			//search in the preceding measures
			for (iMeasure in rangeReverse(mp.measure - 1, 0)) {
				measure = getMeasure(Companion.atMeasure(mp.staff, iMeasure))
				if (measure.clefs != null) {
					ret = measure.clefs.getLast()
					if (ret != null)
						return ret.element.type
				}
			}
		}
		//no clef found. return default clef.
		return ClefType.Companion.getClefTreble()
	}


	/**
	 * Gets the [MusicContext] that is defined before (or at,
	 * dependent on the given [Interval]s) the given
	 * [BMP], also over measure boundaries.
	 *
	 * Calling this method can be quite expensive, so call only when neccessary.
	 *
	 * @param mp                   the musical position
	 * @param clefAndKeyInterval   where to look for the last clef and key:
	 * [Interval.Before] ignores a clef and a key
	 * at the given position, [Interval.BeforeOrAt]
	 * and [Interval.At] (meaning the same here)
	 * include it
	 * @param accidentalsInterval  the same as for clefAndKeyInterval, but for the accidentals.
	 * If null, no accidentals are collected.
	 */
	fun getMusicContext(mp: MP,
	                    clefAndKeyInterval: Interval, accidentalsInterval: Interval?): MusicContext {
		var clefAndKeyInterval = clefAndKeyInterval
		if (clefAndKeyInterval === At)
			clefAndKeyInterval = BeforeOrAt //At and BeforeOrAt mean the same in this context
		val clef = getClef(mp, clefAndKeyInterval)
		val key = getKey(mp, clefAndKeyInterval).element
		var accidentals = noAccidentals
		if (accidentalsInterval != null)
			accidentals = getAccidentals(mp, accidentalsInterval)
		return MusicContext(clef, key, accidentals, getStaff(mp).linesCount)
	}


	/**
	 * Gets the measures of the column with the given index.
	 */
	fun getColumn(measureIndex: Int): Column {
		val ret = Column(stavesCount)
		for (staff in this.stavesList.staves) {
			ret.add(staff.getMeasure(measureIndex))
		}
		return ret
	}


	/**
	 * Gets the interline space of the staff with the given index.
	 * If unspecified, the default value of the score is returned.
	 */
	fun getInterlineSpace(mp: MP): Float {
		val staffIndex = mp.staff
		if (staffIndex >= 0 && staffIndex < stavesCount) {
			val custom = getStaff(staffIndex).interlineSpace
			if (custom != null) {
				return custom
			}
		}
		return this.format.interlineSpace
	}

	/**
	 * Gets the gap in beats between the end of the left element and
	 * the start of the right element. If it can not be determined, because
	 * the musical position of one of the elements is unknown, null is returned.
	 */
	fun getGapBetween(left: VoiceElement, right: VoiceElement): Fraction? {
		var mpLeft = MP.getMP(left) ?: return null
		mpLeft = mpLeft!!.withBeat(mpLeft!!.beat!!.add(left.duration))
		val mpRight = MP.getMP(right) ?: return null
		if (mpRight!!.measure == mpLeft!!.measure) {
			//simple case: same measure. just subtract beats
			return mpRight!!.beat!!.sub(mpLeft!!.beat)
		} else if (mpRight!!.measure > mpLeft!!.measure) {
			//right element is in a following measure. add the following:
			//- beats between end of left element and its measure end
			//- beats in the following measures (until the measure which contains the right element)
			//- start beat of the right element in its measure
			var gap = getMeasureFilledBeats(mpLeft!!.measure).sub(mpLeft!!.beat)
			for (iMeasure in range(mpLeft!!.measure + 1, mpRight!!.measure - 1))
				gap = gap.add(getMeasureFilledBeats(iMeasure))
			gap = gap.add(mpRight!!.beat)
			return gap
		} else {
			//right element is not really at the right, but actually before the left element
			//add the following and sign with minus:
			//- betweens between the start of the right element and the end of its measure
			//- beats in the following measures (until the measure which contains the left element)
			//- end beat of the left element in its measure
			var gap = getMeasureFilledBeats(mpRight!!.measure).sub(mpRight!!.beat)
			for (iMeasure in range(mpRight!!.measure + 1, mpLeft!!.measure - 1))
				gap = gap.add(getMeasureFilledBeats(iMeasure))
			gap = gap.add(mpLeft!!.beat)
			gap = gap.mult(Companion.fr(-1))
			return gap
		}
	}

}
