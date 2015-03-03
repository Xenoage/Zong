package com.xenoage.zong.musiclayout.notations;

import java.util.List;

import lombok.AllArgsConstructor;

import com.xenoage.zong.core.music.beam.Beam;

/**
 * Layout details of a {@link Beam}.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor
public class BeamNotation {
	
	public static final float lineHeightIs = 0.5f;
	public static final float defaultGapIs = 0.25f; //default gap between lines in interline spaces
	public static final float hookLengthIs = 1.25f; //Chlapik TODO


	/**
	 * Line waypoint in a beam.
	 */
	public enum Waypoint {
		None,
		Start,
		Stop,
		HookLeft,
		HookRight,
		StopHookRight
	}


	/** The beam element. */
	public Beam element;
	/** The maximum number of beam lines, e.g. 2 for beams which contain at maximum 16th notes. */
	public int linesCount;
	/** The lists of waypoints (inner list) for the 16th, 32th, 64th, ... line (outer list). */
	public List<List<Waypoint>> waypoints;
	/** The vertical gap (distance on y-axis) between the beam lines in IS. */
	public float gapIs;
	

	/**
	 * Returns the height of all lines of the beam including their distances in IS.
	 */
	public float getTotalHeightIs() {
		return lineHeightIs * linesCount + gapIs * (linesCount - 1);
	}
	
	/**
	 * Returns the waypoints for the given line (1 = 16th, 2 = 32th, ...).
	 */
	public List<Waypoint> getWaypoints(int line) {
		if (line == 0)
			throw new IllegalArgumentException("8th line is always continuous, no waypoints provided");
		return waypoints.get(line - 1);
	}
	
	
}
