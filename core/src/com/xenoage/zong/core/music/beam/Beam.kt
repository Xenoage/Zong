package com.xenoage.zong.core.music.beam

import com.xenoage.utils.annotations.NonNull
import com.xenoage.utils.math.Fraction
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.WaypointPosition
import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.util.Duration
import com.xenoage.zong.core.position.MP
import com.xenoage.zong.core.position.MPContainer
import com.xenoage.zong.core.position.MPElement

import com.xenoage.utils.collections.CollectionUtils.alist
import com.xenoage.utils.collections.CollectionUtils.getFirst
import com.xenoage.utils.kernel.Range.range
import com.xenoage.utils.math.MathUtils.min
import com.xenoage.zong.core.music.Voice
import java.lang.Math.max


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
) : MPElement<Voice> {

	//cache
	private var verticalSpan: VerticalSpan? = null
	private var upperStaffIndex = -1
	private var lowerStaffIndex = -1

	val start: BeamWaypoint
		get() = waypoints.first()

	val stop: BeamWaypoint
		get() = waypoints.last()

	/** The maximum number of beam lines used in the this beam */
	val maxLinesCount: Int
		get() {
			val minDuration = waypoints.minBy { it.chord.displayedDuration }.duration
			return Duration.getFlagsCount(minDuration)
		}

	val musicElementType: MusicElementType
		get() = MusicElementType.Beam


	/**
	 * The parent of the beam is defined as the voice of the start of the beam.
	 */
	override var parent: MPContainer?
		get() = getFirst(waypoints).getChord().getParent()
		set(value: MPContainer?) {
			super.parent = value
		}

	/**
	 * The MP of the beam is the same as the MP of the first chord in the beam.
	 */
	val mp: MP
		get() = getFirst(waypoints).getChord().getMP()

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
		val measure = getFirst(waypoints).getChord().getMP().measure
		for (wp in waypoints) {
			val (_, measure1, _, beat) = wp.chord.getMP()

			//check, if all chords are in the same measure column
			if (measure1 != measure)
				throw IllegalArgumentException("A beam may only span over one measure column")

			//check, if chords are sorted by beat.
			//for grace notes, the same beat is ok.
			if (lastBeat != null) {
				val compare = beat!!.compareTo(lastBeat)
				if (false == wasLastChordGrace && compare <= 0 || wasLastChordGrace && compare < 0)
					throw IllegalArgumentException("Beamed chords must be sorted by beat")
			}
			lastBeat = beat
			wasLastChordGrace = wp.chord.isGrace
		}
	}


	fun size(): Int {
		return waypoints.size
	}


	/**
	 * Gets the position of the given waypoint.
	 */
	fun getWaypointPosition(wp: BeamWaypoint): WaypointPosition {
		return getWaypointPosition(wp.chord)
	}


	/**
	 * Gets the position of the given chord: Start, Stop or Continue.
	 */
	fun getWaypointPosition(chord: Chord): WaypointPosition {
		val index = getWaypointIndex(chord)
		return if (index == 0)
			WaypointPosition.Start
		else if (index == waypoints.size - 1)
			WaypointPosition.Stop
		else
			WaypointPosition.Continue
	}

	/**
	 * Gets the index of the given chord within the beam.
	 */
	fun getWaypointIndex(chord: Chord): Int {
		for (i in range(waypoints))
			if (chord === waypoints[i].chord)
				return i
		throw IllegalArgumentException("Given chord is not part of this beam.")
	}


	/**
	 * Gets the vertical spanning of this beam.
	 */
	fun getVerticalSpan(): VerticalSpan? {
		if (verticalSpan == null)
			computeSpan()
		return verticalSpan
	}


	/**
	 * Gets the index of the topmost staff this beam belongs to.
	 */
	fun getUpperStaffIndex(): Int {
		if (upperStaffIndex == -1)
			computeSpan()
		return upperStaffIndex
	}


	/**
	 * Gets the index of the bottommost staff this beam belongs to.
	 */
	fun getLowerStaffIndex(): Int {
		if (lowerStaffIndex == -1)
			computeSpan()
		return lowerStaffIndex
	}


	/**
	 * Replaces the given old chord with the given new one.
	 */
	fun replaceChord(oldChord: Chord, newChord: Chord) {
		for (i in range(waypoints)) {
			if (waypoints[i].chord === oldChord) {
				waypoints[i].setChord(newChord)
				computeSpan()
				return
			}
		}
		throw IllegalArgumentException("Given chord is not part of this beam")
	}


	/**
	 * Returns true, if a beam lines subdivision ends at the chord
	 * with the given index.
	 */
	fun isEndOfSubdivision(chordIndex: Int): Boolean {
		return waypoints[chordIndex].isSubdivision()
	}


	/**
	 * Gets the chord with the given index.
	 */
	fun getChord(chordIndex: Int): Chord {
		return waypoints[chordIndex].chord
	}


	/**
	 * Computes the vertical span of this beam.
	 */
	private fun computeSpan() {
		var minStaffIndex = Integer.MAX_VALUE
		var maxStaffIndex = Integer.MIN_VALUE

		//check if the beam spans over a single staff or two adjacent staves or more
		for (waypoint in waypoints) {
			val chord = waypoint.chord
			val mpChord = MP.getMP(chord)
			minStaffIndex = Math.min(minStaffIndex, mpChord!!.staff)
			maxStaffIndex = max(maxStaffIndex, mpChord!!.staff)
		}
		var verticalSpan = VerticalSpan.Other
		if (maxStaffIndex == minStaffIndex)
			verticalSpan = VerticalSpan.SingleStaff
		else if (maxStaffIndex - minStaffIndex == 1)
			verticalSpan = VerticalSpan.CrossStaff

		this.verticalSpan = verticalSpan
		this.upperStaffIndex = minStaffIndex
		this.lowerStaffIndex = maxStaffIndex
	}

	companion object {


		/**
		 * Creates a new beam consisting of the given waypoints.
		 * All waypoints must be in the same measure column and must be sorted by beat.
		 */
		fun beam(waypoints: List<BeamWaypoint>): Beam {
			val beam = Beam(waypoints)
			beam.check()
			return beam
		}

		/**
		 * Creates a new beam consisting of the given chords with no subdivisions.
		 */
		fun beamFromChords(chords: List<Chord>): Beam {
			val ret = beamFromChordsUnchecked(chords)
			ret.check()
			return ret
		}

		/**
		 * Creates a new beam consisting of the given chords with no subdivisions.
		 * For testing: No parameter checks!
		 */
		fun beamFromChordsUnchecked(chords: List<Chord>): Beam {
			val waypoints = alist(chords.size)
			for (chord in chords) {
				waypoints.add(BeamWaypoint(chord, false))
			}
			return Beam(waypoints)
		}
	}

}
