package com.xenoage.zong.core.music.format;

import lombok.Data;
import lombok.experimental.Wither;


/**
 * Staff position.
 * 
 * This means a position with a horizontal coordinate in mm
 * and a vertical coordinate as a line position (LP).
 * 
 * @author Andreas Wenger
 */
@Data @Wither
public final class SP {

	/** Position on x-axis in mm, relative to the left side of the parent staff. */
	public final float xMm;
	/** Position on y-axis as a line position (LP). */
	public final float lp;


	public static SP sp(float xMm, float lp) {
		return new SP(xMm, lp);
	}

	public SP add(SP sp) {
		return sp(xMm + sp.xMm, lp + sp.lp);
	}

}
