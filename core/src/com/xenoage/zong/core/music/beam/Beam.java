package com.xenoage.zong.core.music.beam;

import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.min;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElement;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.util.InconsistentScoreException;


/**
 * Class for a beam that connects two or more chords.
 *
 * @author Andreas Wenger
 */
public final class Beam
	implements MusicElement {

	/** Spread of this beam within a staff. */
	public enum HorizontalSpan {
		SingleMeasure,
		Other;
	}

	/** Spread of this beam within a system. */
	public enum VerticalSpan {
		SingleStaff,
		TwoAdjacentStaves,
		Other;
	}

	/** The waypoints in this beam. */
	@NonNull @Getter private List<BeamWaypoint> waypoints;

	//cache
	private HorizontalSpan horizontalSpan = null;
	private VerticalSpan verticalSpan = null;
	private int upperStaffIndex = -1;
	private int lowerStaffIndex = -1;


	/**
	 * Creates a new beam consisting of the given waypoints.
	 */
	public static Beam beam(List<BeamWaypoint> waypoints) {
		if (waypoints.size() < 2) {
			throw new IllegalArgumentException("At least two chords are needed to create a beam!");
		}
		Beam beam = new Beam(waypoints);
		return beam;
	}


	/**
	 * Creates a new beam consisting of the given chords with no subdivisions.
	 */
	public static Beam beamFromChords(List<Chord> chords) {
		if (chords.size() < 2) {
			throw new InconsistentScoreException("At least two chords are needed to create a beam!");
		}
		List<BeamWaypoint> waypoints = new ArrayList<BeamWaypoint>(chords.size());
		for (Chord chord : chords) {
			waypoints.add(new BeamWaypoint(chord, false));
		}
		return new Beam(waypoints);
	}


	private Beam(List<BeamWaypoint> waypoints) {
		this.waypoints = waypoints;
	}
	
	
	public int size() {
		return waypoints.size();
	}


	public BeamWaypoint getStart() {
		return waypoints.get(0);
	}


	public BeamWaypoint getStop() {
		return waypoints.get(waypoints.size() - 1);
	}


	/**
	 * Gets the position of the given waypoint.
	 */
	public WaypointPosition getWaypointPosition(BeamWaypoint wp) {
		return getWaypointPosition(wp.getChord());
	}


	/**
	 * Gets the position of the given chord: Start, Stop or Continue.
	 */
	public WaypointPosition getWaypointPosition(Chord chord) {
		int index = getWaypointIndex(chord);
		if (index == 0)
			return WaypointPosition.Start;
		else if (index == waypoints.size() - 1)
			return WaypointPosition.Stop;
		else
			return WaypointPosition.Continue;
	}
	
	/**
	 * Gets the index of the given chord within the beam.
	 */
	public int getWaypointIndex(Chord chord) {
		for (int i : range(waypoints))
			if (chord == waypoints.get(i).getChord())
				return i;
		throw new IllegalArgumentException("Given chord is not part of this beam.");
	}


	/**
	 * Gets the horizontal spanning of this beam.
	 */
	public HorizontalSpan getHorizontalSpan() {
		if (horizontalSpan == null)
			computeSpan();
		return horizontalSpan;
	}


	/**
	 * Gets the vertical spanning of this beam.
	 */
	public VerticalSpan getVerticalSpan() {
		if (verticalSpan == null)
			computeSpan();
		return verticalSpan;
	}


	/**
	 * Gets the index of the topmost staff this beam belongs to.
	 */
	public int getUpperStaffIndex() {
		if (upperStaffIndex == -1)
			computeSpan();
		return upperStaffIndex;
	}


	/**
	 * Gets the index of the bottommost staff this beam belongs to.
	 */
	public int getLowerStaffIndex() {
		if (lowerStaffIndex == -1)
			computeSpan();
		return lowerStaffIndex;
	}


	/**
	 * Replaces the given old chord with the given new one.
	 */
	public void replaceChord(Chord oldChord, Chord newChord) {
		for (int i : range(waypoints)) {
			if (waypoints.get(i).getChord() == oldChord) {
				waypoints.get(i).setChord(newChord);
				computeSpan();
				return;
			}
		}
		throw new IllegalArgumentException("Given chord is not part of this beam");
	}


	/**
	 * Returns true, if a beam lines subdivision ends at the chord
	 * with the given index.
	 */
	public boolean isEndOfSubdivision(int chordIndex) {
		return waypoints.get(chordIndex).isSubdivision();
	}


	/**
	 * Gets the chord with the given index.
	 */
	public Chord getChord(int chordIndex) {
		return waypoints.get(chordIndex).getChord();
	}


	/**
	 * Computes the horizontal and vertical span of this beam.
	 */
	private void computeSpan() {
		//find out horizontal and vertical span
		Chord firstChord = waypoints.get(0).getChord();
		MP firstMP = MP.getMP(firstChord);
		int minStaffIndex = Integer.MAX_VALUE;
		int maxStaffIndex = Integer.MIN_VALUE;

		//check if the beam spans over a single measure (the current one)
		boolean singleMeasure = true;
		for (BeamWaypoint waypoint : waypoints) {
			Chord chord = waypoint.getChord();
			MP mpChord = MP.getMP(chord);
			if (mpChord == null) {
				throw new IllegalArgumentException("Chord is not registered in globals");
			}
			minStaffIndex = Math.min(minStaffIndex, mpChord.staff);
			maxStaffIndex = Math.max(maxStaffIndex, mpChord.staff);
			int chordMeasure = mpChord.measure;
			if (chordMeasure != firstMP.measure) {
				singleMeasure = false;
				break;
			}
		}
		HorizontalSpan horizontalSpan = (singleMeasure ? HorizontalSpan.SingleMeasure : HorizontalSpan.Other);

		//check if the beam spans over a single staff or two adjacent staves or more
		Set<Integer> staves = new HashSet<Integer>();
		for (BeamWaypoint waypoint : waypoints) {
			Chord chord = waypoint.getChord();
			staves.add(MP.getMP(chord).staff);
		}
		VerticalSpan verticalSpan = VerticalSpan.Other;
		if (staves.size() == 1) {
			verticalSpan = VerticalSpan.SingleStaff;
		}
		else if (staves.size() == 2) {
			Integer[] twoStaves = staves.toArray(new Integer[0]);
			if (Math.abs(twoStaves[0] - twoStaves[1]) <= 1) {
				verticalSpan = VerticalSpan.TwoAdjacentStaves;
			}
		}

		this.horizontalSpan = horizontalSpan;
		this.verticalSpan = verticalSpan;
		this.upperStaffIndex = minStaffIndex;
		this.lowerStaffIndex = maxStaffIndex;
	}
	
	/**
	 * Gets the maximum number of beam lines used in the this beam.
	 */
	public int getMaxLinesCount() {
		Fraction minDuration = null;
		for (BeamWaypoint waypoint : waypoints)
			minDuration = min(minDuration, waypoint.getChord().getDuration());
		return DurationInfo.getFlagsCount(minDuration);
	}

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.Beam;
	}
}
