package com.xenoage.zong.core.music.slur;

import static com.xenoage.utils.CheckUtils.checkArgsNotNull;
import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.kernel.Range.range;

import java.util.ArrayList;

import lombok.Data;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.chord.Chord;


/**
 * A slur or tie connecting two notes,
 * represented by two {@link SlurWaypoint}s.
 * 
 * Formerly, this class was called "curved line", inspired by Wikipedia,
 * which states that "a slur is denoted with a curved line",
 * see http://en.wikipedia.org/wiki/Slur_(music) .
 * But "slur" seems to be a more intuitive naming, even if this
 * class represents both slurs and ties.
 * 
 * @author Andreas Wenger
 */
@Data public final class Slur
	implements MusicElement {

	/** Slur or tie. */
	private SlurType type;
	/** The waypoints of the slur (at least two). */
	private ArrayList<SlurWaypoint> waypoints;
	/** The vertical side of the slur, or null for default. */
	private VSide side;


	/**
	 * Creates a new {@link Slur}. 
	 * @param start  the left end point of the curved line
	 * @param stop   the right end point of the curved line
	 */
	public Slur(SlurType type, SlurWaypoint start, SlurWaypoint stop, VSide side) {
		checkArgsNotNull(type, start, stop);
		this.type = type;
		this.waypoints = alist(start, stop);
		this.side = side;
	}


	public Slur(SlurType type, ArrayList<SlurWaypoint> waypoints, VSide side) {
		if (waypoints.size() < 2)
			throw new IllegalArgumentException("At least two waypoints are needed to create a slur!");
		this.type = type;
		this.waypoints = waypoints;
		this.side = side;
	}


	public SlurWaypoint getStart() {
		return waypoints.get(0);
	}


	public SlurWaypoint getStop() {
		return waypoints.get(waypoints.size() - 1);
	}


	public SlurWaypoint getWaypoint(Chord chord) {
		for (SlurWaypoint wp : waypoints) {
			if (chord == wp.getChord()) {
				return wp;
			}
		}
		throw new IllegalArgumentException("Given chord is not part of this slur.");
	}


	public WaypointPosition getWaypointPosition(Chord chord) {
		if (chord == getStart().getChord()) {
			return WaypointPosition.Start;
		}
		else if (chord == getStop().getChord()) {
			return WaypointPosition.Stop;
		}
		else {
			for (int i : range(1, waypoints.size() - 2)) {
				if (chord == waypoints.get(i).getChord()) {
					return WaypointPosition.Continue;
				}
			}
			throw new IllegalArgumentException("Given chord is not part of this slur.");
		}
	}


	/**
	 * Replaces the given chord.
	 */
	public void replaceChord(Chord oldChord, Chord newChord) {
		for (int i : range(waypoints)) {
			SlurWaypoint wp = waypoints.get(i);
			if (wp.getChord() == oldChord) {
				//try to stay on the same note index, if possible
				wp.setNoteIndex(Math.min(wp.getNoteIndex(), newChord.getNotes().size()));
				wp.setChord(newChord);
				return;
			}
		}
		throw new IllegalArgumentException("Given chord is not part of this beam");
	}


}
