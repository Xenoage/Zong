package com.xenoage.zong.core.music.beam

import com.xenoage.utils.checkIndex
import com.xenoage.utils.math.Fraction
import com.xenoage.utils.throwEx
import com.xenoage.zong.core.music.Voice
import com.xenoage.zong.core.music.WaypointPosition
import com.xenoage.zong.core.music.WaypointPosition.*
import com.xenoage.zong.core.music.beam.Beam.VerticalSpan.Other
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.util.flagsCount
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MP.Companion.unknown
import com.xenoage.zong.core.position.MPElement
import kotlin.math.max
import kotlin.math.min


/**
 * Class for a beam that connects two or more chords.
 *
 * A beam can be placed within a single staff (the common case) or
 * cross two staves (e.g. in a piano score). When crossing two staves,
 * the beam belongs to the staff of the first chord.
 */
class Beam(
		/** The waypoints in this beam */
		val waypoints: List<BeamWaypoint>
) : MPElement {

	//cache
	private var _verticalSpan: VerticalSpan? = null
	private var _upperStaffIndex = -1
	private var _lowerStaffIndex = -1

	val start: BeamWaypoint
		get() = waypoints.first()

	val stop: BeamWaypoint
		get() = waypoints.last()

	/** The maximum number of beam lines used in the this beam */
	val maxLinesCount: Int
		get() {
			val minDuration = waypoints.asSequence().map{ it.chord.duration }.min() ?: throw IllegalStateException()
			return minDuration.flagsCount
		}

	/**
	 * The parent of the beam is defined as the voice of the start of the beam.
	 */
	override val parent: Voice?
		get() = waypoints.first().chord.parent

	/**
	 * The [MP] of the beam is the same as the [MP] of the first chord in the beam.
	 */
	override val mp: MP?
		get() = waypoints.first().chord.mp

	/** Spread of this beam within a system.  */
	enum class VerticalSpan {
		/** Beam within a single staff.  */
		SingleStaff,
		/** Beam crossing two adjacent staves.  */
		CrossStaff,
		/** Other span (not supported).  */
		Other
	}

	/**
	 * Checks the correctness of the beam:
	 * The beam must have at least one line
	 * It must have at least 2 chords, must exist in a single measure column
	 * and the chords must be sorted by beat.
	 */
	private fun check() {

		if (maxLinesCount == 0)
			throw IllegalArgumentException("A beam must have at least one line")

		if (waypoints.size < 2)
			throw IllegalArgumentException("At least two chords are needed to create a beam!")

		var lastBeat: Fraction? = null
		var wasLastChordGrace = false
		val startMeasure = mp?.measure
		for (wp in waypoints) {
			val chordMP = wp.chord.mp ?: continue //if unknown MP, this waypoint can not be checked
			val (_, wpMeasure, _, beat) = chordMP

			//check, if all chords are in the same measure column
			if (wpMeasure != startMeasure)
				throw IllegalArgumentException("A beam may only span over one measure column")

			//check, if chords are sorted by beat.
			//for grace notes, the same beat is ok.
			if (lastBeat != null && beat != null) {
				val compare = beat.compareTo(lastBeat)
				if (false == wasLastChordGrace && compare <= 0 || wasLastChordGrace && compare < 0)
					throw IllegalArgumentException("Beamed chords must be sorted by beat")
			}
			lastBeat = beat
			wasLastChordGrace = wp.chord.isGrace
		}
	}

	/** The number of chords in this beam. */
	val size: Int = waypoints.size

	/** Gets the position of the given waypoint. */
	fun getWaypointPosition(wp: BeamWaypoint): WaypointPosition =
		getWaypointPosition(wp.chord)

	/** Gets the position of the given chord. */
	fun getWaypointPosition(chord: Chord): WaypointPosition {
		return when (getWaypointIndex(chord)) {
			0 -> Start
			waypoints.size - 1 -> Stop
			else -> Continue
		}
	}

	/** Gets the index of the given chord within the beam. */
	fun getWaypointIndex(chord: Chord): Int =
			waypoints.indexOfFirst { it.chord === chord }.checkIndex { "Given chord is not part of this beam." }

	/** The vertical spanning of this beam. */
	val verticalSpan: VerticalSpan
			get() {
				if (_verticalSpan == null)
					computeSpan()
				return _verticalSpan!!
			}

	/** The index of the topmost staff this beam belongs to. */
	val getUpperStaffIndex: Int
		get() {
			if (_upperStaffIndex == -1)
				computeSpan()
			return _upperStaffIndex
		}


	/** The index of the bottommost staff this beam belongs to. */
	val getLowerStaffIndex: Int
		get() {
			if (_lowerStaffIndex == -1)
				computeSpan()
			return _lowerStaffIndex
		}

	/** Replaces the given old chord with the given new one. */
	fun replaceChord(oldChord: Chord, newChord: Chord) {
		for (i in waypoints.indices) {
			if (waypoints[i].chord === oldChord) {
				oldChord.beam = null
				waypoints[i].chord = newChord
				newChord.beam = this
				computeSpan()
				return
			}
		}
		throwEx("Given chord is not part of this beam")
	}

	/**
	 * Returns true, if a beam lines subdivision ends at the chord
	 * with the given index.
	 */
	fun isEndOfSubdivision(chordIndex: Int): Boolean =
		waypoints[chordIndex].subdivision

	/** Gets the chord with the given index. */
	fun getChord(chordIndex: Int): Chord =
		waypoints[chordIndex].chord

	/** Computes the vertical span of this beam. */
	private fun computeSpan() {
		var minStaffIndex = Integer.MAX_VALUE
		var maxStaffIndex = Integer.MIN_VALUE

		//check if the beam spans over a single staff or two adjacent staves or more
		for (waypoint in waypoints) {
			val chord = waypoint.chord
			val mpChord = chord.mp
			if (mpChord == null || mpChord.staff == unknown) { //unknown MP? then we can not compute this beam
				_verticalSpan = Other
				_upperStaffIndex = unknown
				_lowerStaffIndex = unknown
				return
			}
			minStaffIndex = min(minStaffIndex, mpChord.staff)
			maxStaffIndex = max(maxStaffIndex, mpChord.staff)
		}
		var verticalSpan = Other
		if (maxStaffIndex == minStaffIndex)
			verticalSpan = VerticalSpan.SingleStaff
		else if (maxStaffIndex - minStaffIndex == 1)
			verticalSpan = VerticalSpan.CrossStaff

		_verticalSpan = verticalSpan
		_upperStaffIndex = minStaffIndex
		_lowerStaffIndex = maxStaffIndex
	}

	companion object {

		/**
		 * Creates a new beam consisting of the given chords with no subdivisions.
		 * OBSOLETE
		 */
		fun beamFromChords(chords: List<Chord>): Beam {
			val ret = beamFromChordsUnchecked(chords)
			ret.check()
			return ret
		}

		/**
		 * Creates a new beam consisting of the given chords with no subdivisions.
		 * For testing: No parameter checks!
		 * OBSOLETE
		 */
		fun beamFromChordsUnchecked(chords: List<Chord>): Beam =
				Beam(chords.map { BeamWaypoint(it) })

	}

}
