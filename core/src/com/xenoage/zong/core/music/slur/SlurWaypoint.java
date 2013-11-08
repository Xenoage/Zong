package com.xenoage.zong.core.music.slur;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import lombok.Data;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.format.BezierPoint;


/**
 * Waypoint for a {@link Slur}, belonging to a note of a chord, optionally with
 * formatting information.
 * 
 * The tie or slur can begin or end here,
 * or it is an intermediate point (called continue).
 * The last option is only needed to store
 * formatting information.
 *
 * @author Andreas Wenger
 */
@Data public final class SlurWaypoint {

	/** The chord belonging to this waypoint. */
	@NonNull private Chord chord;
	/** The index of the note this waypoint belongs to, or null if the whole chord is meant. */
	private Integer noteIndex;
	/** Formatting of this waypoint, or null for default layout. */
	private BezierPoint bezierPoint;

	/** Back reference: The slur this waypoint belongs to. */
	private Slur parentSlur = null;


	/**
	 * Creates a new waypoint with the given formatting.
	 */
	public SlurWaypoint(Chord chord, Integer noteIndex, BezierPoint bezierPoint) {
		checkArgsNotNull(chord);
		this.chord = chord;
		this.noteIndex = noteIndex;
		this.bezierPoint = bezierPoint;
	}

}
