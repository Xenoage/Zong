package com.xenoage.zong.core.music.slur

import com.xenoage.zong.core.music.chord.Chord
import com.xenoage.zong.core.music.format.BezierPoint


/**
 * Waypoint for a [Slur], belonging to a note of a chord, optionally with
 * formatting information.
 *
 * The tie or slur can begin or end here, or it is an intermediate point (called continue).
 * The last option is only needed to store formatting information.
 */
class SlurWaypoint(
		/** The chord belonging to this waypoint. */
		val chord: Chord,
		/** The index of the note this waypoint belongs to, or null if the whole chord is meant.  */
		val noteIndex: Int? = null,
		/** Formatting of this waypoint, or null for default layout. */
		val bezierPoint: BezierPoint? = null
) {

	/** Back reference: The slur this waypoint belongs to. */
	var parentSlur: Slur? = null

}
