package com.xenoage.zong.core.music.beam

import com.xenoage.zong.core.music.chord.Chord


/**
 * Waypoint for a beam, belonging to a chord.
 *
 * The beam can begin or end here, or the
 * chord can be connected to it inbetween.
 *
 * The waypoint can also mark the ending of a subdivision.
 * This is a group of consecutive notes that are beamed in
 * a normal way, but then only a single beam line connects
 * the marked note with the following one. So this is only
 * applicable for 16th notes or shorter ones.
 */
class BeamWaypoint(
		/** The chord belonging to this waypoint.  */
		var chord: Chord,
		/** True, if the beam has a subdivision ending at this point. This means, that only a single beam line
		 * connects this chord to the next chord within the beam (and is such only applicable for 16th notes or shorter).  */
		var subdivision: Boolean = false
) {

	/** Back reference: The beam this waypoint belongs to.  */
	var parentBeam: Beam? = null

}
