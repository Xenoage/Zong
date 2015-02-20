package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;

import com.xenoage.utils.annotations.Const;
import com.xenoage.zong.musiclayout.notations.TimeNotation;

/**
 * Class for a time signature stamping.
 * It consists of a fraction, like "4/4" or "7/16".
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public final class NormalTimeStamping
	extends Stamping {

	/** The normal time signature. */
	public final TimeNotation time;
	/** The horizontal position in mm. */
	public final float xMm;
	/** The horizontal offset of the numerator in interline spaces. */
	public final float numeratorOffsetIs;
	/** The horizontal offset of the denominator in interline spaces. */
	public final float denominatorOffsetIs;
	/** The gap between the digits in interline spaces. */
	public final float digitGapIs;
	/** The parent staff. */
	public final StaffStamping parentStaff;
	

	@Override public StampingType getType() {
		return StampingType.NormalTimeStamping;
	}
	
	@Override public Level getLevel() {
		return Level.Music;
	}

}
