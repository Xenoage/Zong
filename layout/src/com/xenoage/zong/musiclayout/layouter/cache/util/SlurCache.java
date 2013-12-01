package com.xenoage.zong.musiclayout.layouter.cache.util;

import javax.rmi.CORBA.Tie;

import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.musiclayout.continued.ContinuedSlur;
import com.xenoage.zong.musiclayout.stampings.NoteheadStamping;

/**
 * This class is used by the layouter to save layouting information about
 * the starting and ending point of a {@link Slur}.
 * 
 * The global index of the stave this curved line belongs to is also saved here.
 *
 * @author Andreas Wenger
 */
public class SlurCache {

	//known from the beginning
	private final ContinuedSlur continuedSlur; //use this class to save information
	private final NoteheadStamping startNotehead;
	private final float startDistanceIS;
	private final int startSystem;

	//set when known
	private NoteheadStamping stopNotehead = null;
	private float stopDistanceIS = 0;
	private int stopSystem = -1;


	/**
	 * Creates a {@link SlurCache} instance for a new slur
	 * where the start notehead and system is known.
	 */
	public static SlurCache createNew(Slur slur, VSide side, int staffIndex,
		NoteheadStamping startNotehead, float startDistanceIS, int startSystem) {
		return new SlurCache(new ContinuedSlur(slur, side, staffIndex, 1), //1: TODO
			startNotehead, startDistanceIS, startSystem);
	}

	/**
	 * Creates a {@link SlurCache} instance for a continued slur.
	 */
	public static SlurCache createContinued(ContinuedSlur continuedSlur) {
		return new SlurCache(continuedSlur, null, 0, -1);
	}

	/**
	 * Creates a {@link SlurCache} instance using
	 * the given information about the {@link Tie} or {@link Slur} and the
	 * {@link NoteheadStamping} of the start note (if known, otherwise null)
	 * together with additional distance in IS (e.g. because there are articulations
	 * or a stem) and index of the start system (if known, otherwise -1).
	 */
	private SlurCache(ContinuedSlur continuedSlur, NoteheadStamping startNotehead,
		float startDistanceIS, int startSystem) {
		this.continuedSlur = continuedSlur;
		this.startNotehead = startNotehead;
		this.startDistanceIS = startDistanceIS;
		this.startSystem = startSystem;
	}

	/**
	 * Gets the {@link Slur} instance this data belongs to.
	 */
	public Slur getSlur() {
		return continuedSlur.slur;
	}

	/**
	 * Gets the notehead stamping of the start note, or null,
	 * if it is unknown, because it is a continued slur.
	 */
	public NoteheadStamping getStartNoteheadStamping() {
		return startNotehead;
	}

	/**
	 * Gets the additional distance from the start notehead stamping,
	 * that is e.g. needed when there are articulations or stems.
	 */
	public float getStartDistanceIS() {
		return startDistanceIS;
	}

	/**
	 * Gets the system index, where the slur starts. If it is
	 * a continued curved line, -1 is returned.
	 */
	public int getStartSystem() {
		return startSystem;
	}

	/**
	 * Gets the notehead stamping of the end note, or null,
	 * if it was not created up to now.
	 */
	public NoteheadStamping getStopNoteheadStamping() {
		return stopNotehead;
	}

	/**
	 * Gets the additional distance from the end notehead stamping,
	 * that is e.g. needed when there are articulations.
	 */
	public float getStopDistanceIS() {
		return stopDistanceIS;
	}

	/**
	 * Gets the notehead stamping of the end note, or -1,
	 * if the stop notehead stamping was not created up to now.
	 */
	public int getStopSystem() {
		return stopSystem;
	}

	/**
	 * Sets the notehead stamping of the end note,
	 * (the additional distance from the end notehead stamping,
	 * that is e.g. needed when there are articulations) and its system index.
	 */
	public void setStop(NoteheadStamping stopNotehead, float stopDistanceIS, int stopSystem) {
		this.stopNotehead = stopNotehead;
		this.stopDistanceIS = stopDistanceIS;
		this.stopSystem = stopSystem;
	}

	/**
	 * Gets the placement of the slur: above or below.
	 * TODO: add more possibilities like S-curved slurs.
	 */
	public VSide getSide() {
		return continuedSlur.side;
	}

	/**
	 * Gets the global staff index this curved line belongs to.
	 */
	public int getStaffIndex() {
		return continuedSlur.staffIndex;
	}

	/**
	 * Gets the {@link ContinuedSlur} of this cache, which can be used
	 * to indicate that a continuation of this curved line is needed.
	 */
	public ContinuedSlur getContinuedCurvedLine() {
		return continuedSlur;
	}

	/**
	 * Returns true, if the start of this slur (notehead and system) is known.
	 */
	public boolean isStartKnown() {
		return startSystem != -1;
	}

	/**
	 * Returns true, if the stop of this slur (notehead and system) is known.
	 */
	public boolean isStopKnown() {
		return stopSystem != -1;
	}

}
