package com.xenoage.zong.musiclayout.layouter.cache.util;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.musiclayout.continued.ContinuedSlur;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import lombok.RequiredArgsConstructor;

/**
 * This class is used by the layouter to save layouting information about
 * the starting and ending point of a {@link Slur}.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class SlurCache {

	//known from the beginning
	private final ContinuedSlur continuedSlur; //use this class to save information
	private final SP defaultStartSp;
	private final StaffStamping startStaff;
	private final int startSystem;

	//set when known
	//TODO: ZONG-115: merge SP+StaffStamping+systemIndex into class and cleanup SlurStamper parameters
	private SP defaultStopSp = null;
	private StaffStamping stopStaff = null;
	private int stopSystem = -1;


	/**
	 * Creates a {@link SlurCache} instance for a new slur.
	 */
	public static SlurCache createNew(Slur slur, VSide side, SP defaultStartSp,
			StaffStamping startStaff, int startSystem) {
		return new SlurCache(new ContinuedSlur(slur, startStaff, side, 1), //1: TODO
				defaultStartSp, startStaff, startSystem);
	}

	/**
	 * Creates a {@link SlurCache} instance for a continued slur.
	 */
	public static SlurCache createContinued(ContinuedSlur continuedSlur) {
		return new SlurCache(continuedSlur, null, continuedSlur.staff, -1);
	}

	/**
	 * Gets the {@link Slur} instance this data belongs to.
	 */
	public Slur getSlur() {
		return continuedSlur.element;
	}

	/**
	 * Gets the default start position, or null,
	 * if it is unknown, because it is a continued slur.
	 */
	@MaybeNull public SP getDefaultStartSp() {
		return defaultStartSp;
	}

	/**
	 * Gets the system index, where the slur starts. If it is
	 * a continued curved line, -1 is returned.
	 */
	public int getStartSystem() {
		return startSystem;
	}

	/**
	 * Gets the default end position, or null,
	 * if it was not created up to now.
	 */
	@MaybeNull public SP getDefaultStopSp() {
		return defaultStopSp;
	}

	/**
	 * Gets the notehead stamping of the end note, or -1,
	 * if the stop notehead stamping was not created up to now.
	 */
	public int getStopSystem() {
		return stopSystem;
	}

	/**
	 * Sets the end position of the slur.
	 */
	public void setStop(SP defaultStopSp, StaffStamping stopStaff, int stopSystem) {
		this.defaultStopSp = defaultStopSp;
		this.stopStaff = stopStaff;
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
	 * Gets the staff index the beginning of this slur belongs to.
	 */
	public StaffStamping getStartStaff() {
		return startStaff;
	}

	/**
	 * Gets the staff the end of this slur belongs to, or null,
	 * if it was not created up to now.
	 */
	@MaybeNull public StaffStamping getStopStaff() {
		return stopStaff;
	}

	/**
	 * Gets the {@link ContinuedSlur} of this cache, which can be used
	 * to indicate that a continuation of this curved line is needed.
	 */
	public ContinuedSlur getContinuedCurvedLine() {
		return continuedSlur;
	}

	/**
	 * Returns true, if the start of this slur is known.
	 */
	public boolean isStartKnown() {
		return startSystem != -1;
	}

	/**
	 * Returns true, if the stop of this slur is known.
	 */
	public boolean isStopKnown() {
		return stopSystem != -1;
	}

}
