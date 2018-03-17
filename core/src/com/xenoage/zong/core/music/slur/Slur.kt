package com.xenoage.zong.core.music.slur

import com.xenoage.utils.math.VSide
import com.xenoage.utils.math.minOrNull
import com.xenoage.utils.throwEx
import com.xenoage.zong.core.music.MusicElement
import com.xenoage.zong.core.music.MusicElementType
import com.xenoage.zong.core.music.WaypointPosition
import com.xenoage.zong.core.music.chord.Chord


/**
 * A slur or tie connecting two notes, represented by two [SlurWaypoint]s.
 *
 * Formerly, this class was called "curved line", inspired by Wikipedia,
 * which states that "a slur is denoted with a curved line",
 * see http://en.wikipedia.org/wiki/Slur_(music) .
 * But "slur" seems to be a more intuitive naming, even if this
 * class represents both slurs and ties.
 */
class Slur(
		/** Slur or tie.  */
		val type: SlurType,
		/** The waypoints of the slur (at least two).  */
		val waypoints: MutableList<SlurWaypoint>,
		/** The vertical side of the slur, or null for default.  */
		val side: VSide? = null
) : MusicElement {

	init {
		check(waypoints.size >= 2, { "At least two waypoints are needed to create a slur!" })
	}

	val start: SlurWaypoint
		get() = waypoints.first()

	val stop: SlurWaypoint
		get() = waypoints.last()

	override val elementType: MusicElementType
		get() = MusicElementType.Slur

	/**
	 * Creates a new [Slur] with the given [start] (left end point)
	 * and [end] (right end point).
	 */
	constructor(type: SlurType, start: SlurWaypoint, stop: SlurWaypoint, side: VSide?) :
			this(type, mutableListOf(start, stop), side)

	/**
	 * Gets the waypoint of the given chord, or throws an exception if the given
	 * chord is not registered as a waypoint.
	 */
	fun getWaypoint(chord: Chord): SlurWaypoint =
			waypoints.first { it.chord === chord }

	/**
	 * Gets the position (start, stop, continue) of the given chord within this
	 * slur, or throws an exception if the chord is not registered as a waypoint.
	 */
	fun getWaypointPosition(chord: Chord): WaypointPosition =
			when (waypoints.indexOfFirst { it.chord === chord }) {
				-1 -> throwEx("chord is not part of this slur")
				0 -> WaypointPosition.Start
				waypoints.lastIndex -> WaypointPosition.Stop
				else -> WaypointPosition.Continue
			}

	/**
	 * Replaces the given chord.
	 */
	fun replaceChord(oldChord: Chord, newChord: Chord) {
		val i = waypoints.indexOfFirst { it.chord === oldChord }
		if (i == -1) throwEx("chord is not part of this slur")
		val oldWaypoint = waypoints[i]
		oldWaypoint.parentSlur = null
		//try to stay on the same note index, if possible
		val newNoteIndex = minOrNull(oldWaypoint.noteIndex, newChord.notes.size)
		waypoints[i] = SlurWaypoint(newChord, newNoteIndex)
		waypoints[i].parentSlur = this
	}

	/**
	 * There are two types of slurs: The real slur ("Bindebogen" in German)
	 * and the tie ("Haltebogen" in German).
	 */
	enum class SlurType {
		/** Bindebogen. */
		Slur,
		/** Haltebogen. */
		Tie
	}

}
