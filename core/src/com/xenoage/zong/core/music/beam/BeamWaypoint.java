package com.xenoage.zong.core.music.beam;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import lombok.Data;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.chord.Chord;


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
 *
 * @author Andreas Wenger
 */
@Data public final class BeamWaypoint {

	/** The chord belonging to this waypoint. */
	@NonNull private Chord chord;
	/** True, if the beam has a subdivision ending at this point. This means, that only a single beam line
	 * connects this chord to the next chord within the beam (and is such only applicable for 16th notes or shorter). */
	private boolean subdivision;

	/** Back reference: The beam this waypoint belongs to. */
	private Beam parentBeam = null;


	public BeamWaypoint(Chord chord, boolean subdivision) {
		checkArgsNotNull(chord);
		this.chord = chord;
		this.subdivision = subdivision;
	}

}
