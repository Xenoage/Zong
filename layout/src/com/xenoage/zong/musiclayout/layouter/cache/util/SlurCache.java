package com.xenoage.zong.musiclayout.layouter.cache.util;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.math.VSide;
import com.xenoage.zong.core.music.format.SP;
import com.xenoage.zong.core.music.slur.Slur;
import com.xenoage.zong.musiclayout.continued.ContinuedSlur;
import com.xenoage.zong.musiclayout.stampings.StaffStamping;
import lombok.RequiredArgsConstructor;
import lombok.val;

/**
 * This class is used by the layouter to save layouting information about
 * the starting and ending point of a {@link Slur}.
 *
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class SlurCache {

	private ContinuedSlur continuedSlur;

	private Slur slur;

	private SP defaultStartSp;
	private StaffStamping startStaff;
	private int startSystem = -1;

	//TODO: ZONG-115: merge SP+StaffStamping+systemIndex into class and cleanup SlurStamper parameters
	private SP defaultStopSp = null;
	private StaffStamping stopStaff = null;
	private int stopSystem = -1;


	/**
	 * Creates a {@link SlurCache} instance for a new slur.
	 */
	public static SlurCache createNew(Slur slur) {
		val cache = new SlurCache();
		cache.slur = slur;
		return cache;
	}

	/**
	 * Creates a {@link SlurCache} instance for a continued slur.
	 */
	public static SlurCache createContinued(ContinuedSlur continuedSlur) {
		val cache = new SlurCache();
		cache.slur = continuedSlur.getElement();
		cache.continuedSlur = continuedSlur;
		cache.startStaff = continuedSlur.staff;
		cache.stopStaff = continuedSlur.staff;
		return cache;
	}

	/**
	 * Gets the {@link Slur} instance this data belongs to.
	 */
	public Slur getSlur() {
		return slur;
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
	 * Sets the start position of the slur.
	 */
	public void setStart(SP defaultStartSp, StaffStamping startStaff, int startSystem) {
		this.defaultStartSp = defaultStartSp;
		this.startStaff = startStaff;
		this.startSystem = startSystem;
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
		return slur.getSide();
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
