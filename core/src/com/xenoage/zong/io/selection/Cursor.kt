package com.xenoage.zong.io.selection

import com.xenoage.utils.document.command.CommandPerformer
import com.xenoage.utils.math._0
import com.xenoage.zong.commands.core.music.*
import com.xenoage.zong.commands.core.music.slur.SlurAdd
import com.xenoage.zong.core.Score
import com.xenoage.zong.core.music.ColumnElement
import com.xenoage.zong.core.music.MeasureElement
import com.xenoage.zong.core.music.Pitch
import com.xenoage.zong.core.music.VoiceElement
import com.xenoage.zong.core.music.beam.Beam
import com.xenoage.zong.core.music.beam.BeamWaypoint
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.chord.Note
import com.xenoage.zong.core.music.slur.Slur
import com.xenoage.zong.core.music.slur.Slur.SlurType
import com.xenoage.zong.core.music.slur.SlurWaypoint
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.mp0
import com.xenoage.zong.io.score.ScoreInputOptions
import com.xenoage.zong.io.score.ScoreInputOptions.WriteMode


/**
 * Cursor for a score.
 *
 * This is the most often used selection, since it is also useful for
 * sequential input like needed when reading from a file.
 * The actions can not be undone. If undo is needed, execute commands
 * on the [CommandPerformer] from the [Score] class.
 *
 * It contains the current position within the score and provides many methods
 * to write or remove elements and move the cursor.
 *
 * If the [moving] flag is set, the cursor jumps to the end of written elements
 * instead of staying at its old position. Then it can be at a [MP] which
 * still does not exist (e.g. at the end of the score), which isn't a problem
 * since it is created as soon as needed.
 *
 * There is also the possibility to open and close beams and slurs.
 */
class Cursor(
		/** The score which will be modified. */
		override val score: Score
) : ScoreSelection {

	/** The current position of the cursor. It contains both an element index and a beat.
	 * If it does not exist in the score yet, the measure and voice will be created on demand. */
	override var mp: MP = mp0
		/** Sets the position of the cursor. The [MP.element] field must be set. */
		set(mp) {
			if (mp.element == MP.unknown)
				throw IllegalArgumentException("unknown element")
			if (mp.beat == MP.unknownBeat) {
				//beat is unknown. compute it or use 0 for nonexisting MPs.
				if (score.isMPExisting(mp))
					field = mp.getWithBeat(score)
				else
					field = mp.copy(beat = _0)
			} else {
				field = mp
			}
		}

	/** True, when the cursor is moved when the write method is executed. Defaults to true. */
	var moving: Boolean = true

	/** True, when empty space should be filled with invisible rests. Defaults to false. */
	var fillWithHiddenRests = false


	private var openBeamWaypoints: MutableList<BeamWaypoint>? = null
	private var openSlurWaypoints: MutableList<SlurWaypoint>? = null
	private var openSlurType: SlurType = SlurType.Slur


	override fun write(element: ColumnElement) {
		ensureMeasureExists(mp)
		ColumnElementWrite(element, score.getColumnHeader(mp.measure), mp.beat, null).execute()
	}

	override fun write(element: ColumnElement, options: ScoreInputOptions) {
		write(element)
	}

	override fun write(pitches: Array<Pitch>, options: ScoreInputOptions) {
		val wm = options.writeMode
		if (wm == WriteMode.ChordBeforeCursor) {
			//add the given pitches to the chord before the cursor
			addPitchesToPrecedingChord(*pitches)
		} else {
			//default behaviour: write chord after cursor
			val chord = Chord(options.duration, *pitches)
			write(chord, options)
		}
	}

	/**
	 * Adds the given pitches to the chord before the cursor. If there is no
	 * chord, nothing is done.
	 */
	private fun addPitchesToPrecedingChord(vararg pitches: Pitch) {
		//find the last voice element starting before the current position
		val ive = score.getStaff(mp.staff).getVoiceElementBefore(mp, false)

		//if the target element is a chord, add the given pitches to it - TODO: use command
		if (ive != null && ive.element is Chord) {
			for (pitch in pitches)
				ive.element.addNote(Note(pitch))
		}
	}

	/**
	 * Writes the given [VoiceElement] at the current position and,
	 * if the moving flag is set, moves the cursor forward according to
	 * the duration of the element.
	 *
	 * This method overwrites the elements overlapping the current cursor
	 * position and overlapping the current cursor position plus the
	 * duration of the given element.
	 *
	 * Thus, if gaps appear before or after the written element, the corresponding
	 * elements are cut.
	 */
	override fun write(element: VoiceElement) {
		//create the voice, if needed
		ensureVoiceExists(mp)

		//create the options
		val options = VoiceElementWrite.Options()
		options.checkTimeSignature = true //always obey to time signature
		options.fillWithHiddenRests = fillWithHiddenRests //optionally, fill gaps with hidden rests

		//write the element
		VoiceElementWrite(score.getVoice(mp!!), mp!!, element, options).execute()

		//if a beam is open and it is a chord, add it
		if (openBeamWaypoints != null && element is Chord) {
			openBeamWaypoints!!.add(BeamWaypoint(element, false))
		}

		//if move flag is set, move cursor forward, also over measure boundaries
		if (moving) {
			val newBeat = mp.beat!! + element.duration
			//if this beat is behind the end of the measure, jump into the next measure
			val measureBeats = score.header.getTimeAtOrBefore(mp.measure).type.measureBeats
			if (measureBeats != null && newBeat >= measureBeats) {
				//begin new measure
				mp = MP(mp.staff, mp.measure + 1, mp.voice, _0, 0)
			} else {
				//stay in the current measure
				mp = MP(mp.staff, mp.measure, mp.voice, newBeat, mp.element + 1)
			}
		}
	}

	override fun write(element: VoiceElement, options: ScoreInputOptions) {
		write(element)
	}

	override fun write(element: MeasureElement) {
		//create the measure, if needed
		ensureMeasureExists(mp)
		//write the element
		MeasureElementWrite(element, score.getMeasure(mp), mp.beat!!).execute()
	}

	override fun write(element: MeasureElement, options: ScoreInputOptions) {
		write(element)
	}

	/** Opens a beam. All following chords will be added to it. */
	fun openBeam() {
		if (openBeamWaypoints != null)
			throw IllegalStateException("Beam is already open")
		openBeamWaypoints = mutableListOf()
	}

	/** Closes a beam and adds it to the score. */
	fun closeBeam() {
		if (openBeamWaypoints == null)
			throw IllegalStateException("No beam is open")
		val beam = Beam(openBeamWaypoints!!)
		openBeamWaypoints!!.forEach { it.chord.beam = beam }
		openBeamWaypoints = null
	}

	/** Opens a slur of the given type. All following chords will be added to it. */
	fun openSlur(type: SlurType) {
		if (openSlurWaypoints == null)
			throw IllegalStateException("Slur is already open")
		openSlurType = type
		openSlurWaypoints = mutableListOf()
	}

	/** Closes a slur and adds it to the score. */
	@Deprecated("Does not work yet. Slur waypoints are not collected in this class.")
	fun closeSlur() {
		if (openSlurWaypoints == null)
			throw IllegalStateException("No curved line is open")
		val slur = Slur(openSlurType, openSlurWaypoints!!)
		SlurAdd(slur).execute()
		openSlurWaypoints = null
	}

	/** Ensures, that the given given staff exists. If not, it is created. */
	private fun ensureStaffExists(mp: MP) {
		//create staves if needed
		if (score.stavesCount <= mp.staff)
			StaffAdd(score, mp.staff - score.stavesCount + 1).execute()
	}

	/** Ensures, that the given measure and staff exists. If not, it is created. */
	private fun ensureMeasureExists(mp: MP) {
		//create staves if needed
		ensureStaffExists(mp)
		//create measures if needed
		if (score.measuresCount <= mp.measure)
			MeasureAdd(score, mp.measure - score.measuresCount + 1).execute()
	}

	/** Ensures, that the given voice, measure and staff exists. If not, it is created. */
	private fun ensureVoiceExists(mp: MP) {
		//create measure if needed
		ensureMeasureExists(mp)
		//create voice if needed
		val measure = score.getMeasure(mp)
		if (measure.voices.size <= mp.voice)
			VoiceAdd(measure, mp.voice).execute()
	}

}
