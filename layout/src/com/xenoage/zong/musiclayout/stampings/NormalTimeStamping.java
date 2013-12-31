package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.core.music.time.Time;

/**
 * Class for a time signature stamping.
 * It consists of a fraction, like "4/4" or "7/16".
 *
 * @author Andreas Wenger
 */
@Const public final class NormalTimeStamping
	extends Stamping {

	/** The horizontal position in mm. */
	public final float positionX;

	/** The normal time signature. */
	public final Time time;

	/** The horizontal offset of the numerator in interline spaces. */
	public final float numeratorOffset;

	/** The horizontal offset of the denominator in interline spaces. */
	public final float denominatorOffset;

	/** The gap between the digits in interline spaces. */
	public final float digitGap;


	public NormalTimeStamping(Time time, float positionX, StaffStamping parentStaff,
		float numeratorOffset, float denominatorOffset, float digitGap) {
		super(parentStaff, Level.Music, null, null);
		this.time = time;
		this.positionX = positionX;
		this.numeratorOffset = numeratorOffset;
		this.denominatorOffset = denominatorOffset;
		this.digitGap = digitGap;
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.NormalTimeStamping;
	}

}
