package com.xenoage.zong.core.music.beam;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.math.Fraction;
import com.xenoage.zong.core.music.MusicElementType;
import com.xenoage.zong.core.music.WaypointPosition;
import com.xenoage.zong.core.music.chord.Chord;
import com.xenoage.zong.core.music.util.DurationInfo;
import com.xenoage.zong.core.position.MP;
import com.xenoage.zong.core.position.MPContainer;
import com.xenoage.zong.core.position.MPElement;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.xenoage.utils.collections.CollectionUtils.alist;
import static com.xenoage.utils.collections.CollectionUtils.getFirst;
import static com.xenoage.utils.kernel.Range.range;
import static com.xenoage.utils.math.MathUtils.min;
import static java.lang.Math.max;


/**
 * Class for a beam that connects two or more chords.
 *
 * @author Andreas Wenger
 */
public final class Beam
	implements MPElement {

	/** Spread of this beam within a system. */
	public enum VerticalSpan {
		SingleStaff,
		TwoAdjacentStaves,
		Other;
	}

	/** The waypoints in this beam. */
	@NonNull @Getter private List<BeamWaypoint> waypoints;

	//cache
	private VerticalSpan verticalSpan = null;
	private int upperStaffIndex = -1;
	private int lowerStaffIndex = -1;


	/**
	 * Creates a new beam consisting of the given waypoints.
	 * All waypoints must be in the same measure column and must be sorted by beat.
	 */
	public static Beam beam(List<BeamWaypoint> waypoints) {
		Beam beam = new Beam(waypoints);
		beam.check();
		return beam;
	}

	/**
	 * Creates a new beam consisting of the given chords with no subdivisions.
	 */
	public static Beam beamFromChords(List<Chord> chords) {
		Beam ret = beamFromChordsNoCheck(chords);
		ret.check();
		return ret;
	}

	/**
	 * Creates a new beam consisting of the given chords with no subdivisions.
	 * For testing: No parameter checks! TODO: DEPENDENCYINJECTION
	 */
	public static Beam beamFromChordsNoCheck(List<Chord> chords) { //TIDY
		List<BeamWaypoint> waypoints = alist(chords.size());
		for (Chord chord : chords) {
			waypoints.add(new BeamWaypoint(chord, false));
		}
		return new Beam(waypoints);
	}

	private Beam(List<BeamWaypoint> waypoints) {
		this.waypoints = waypoints;
	}

	/**
	 * Checks the correctness of the beam:
	 * The beam must have at least one line
	 * It must have at least 2 chords, must exist in a single measure column
	 * and the chords must be sorted by beat.
	 */
	private void check() {

		if (getMaxLinesCount() == 0)
			throw new IllegalArgumentException("A beam must have at least one line");

		if (waypoints.size() < 2)
			throw new IllegalArgumentException("At least two chords are needed to create a beam!");

		Fraction lastBeat = null;
		boolean wasLastChordGrace = false;
		int measure = getFirst(waypoints).getChord().getMP().measure;
		for (BeamWaypoint wp : waypoints) {
			MP mp = wp.getChord().getMP();

			//check, if all chords are in the same measure column
			if (mp.measure != measure)
				throw new IllegalArgumentException("A beam may only span over one measure column");

			//check, if chords are sorted by beat.
			//for grace notes, the same beat is ok.
			if (lastBeat != null) {
				int compare = mp.beat.compareTo(lastBeat);
				if ((false == wasLastChordGrace && compare <= 0) ||
						(wasLastChordGrace && compare < 0))
					throw new IllegalArgumentException("Beamed chords must be sorted by beat");
			}
			lastBeat = mp.beat;
			wasLastChordGrace = wp.getChord().isGrace();
		}
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
	 * Computes the vertical span of this beam.
	 */
	private void computeSpan() {
		int minStaffIndex = Integer.MAX_VALUE;
		int maxStaffIndex = Integer.MIN_VALUE;

		//check if the beam spans over a single staff or two adjacent staves or more
		Set<Integer> staves = new HashSet<Integer>();
		for (BeamWaypoint waypoint : waypoints) {
			Chord chord = waypoint.getChord();
			MP mpChord = MP.getMP(chord);
			minStaffIndex = Math.min(minStaffIndex, mpChord.staff);
			maxStaffIndex = max(maxStaffIndex, mpChord.staff);
			staves.add(mpChord.staff);
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
			minDuration = min(minDuration, waypoint.getChord().getDisplayedDuration());
		return DurationInfo.getFlagsCount(minDuration);
	}

	@Override public MusicElementType getMusicElementType() {
		return MusicElementType.Beam;
	}


	/**
	 * The parent of the beam is defined as the voice of the start of the beam.
	 */
	@Override public MPContainer getParent() {
		return getFirst(waypoints).getChord().getParent();
	}

	/**
	 * The MP of the beam is the same as the MP of the first chord in the beam.
	 */
	@Override public MP getMP() {
		return getFirst(waypoints).getChord().getMP();
	}

}
