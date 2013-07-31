package com.xenoage.zong.core.music.format;

import lombok.Data;


/**
 * Staff position.
 * 
 * This means a position with a horizontal coordinate in mm
 * and a vertical coordinate as a line position (LP).
 * 
 * @author Andreas Wenger
 */
@Data public final class SP {

	/** Position on x-axis in mm. */
	public final float xMm;
	/** Position on y-axis as a line position (LP). */
	public final float lp;


	public static SP sp(float xMm, float lp) {
		return new SP(xMm, lp);
	}

}
