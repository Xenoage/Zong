package com.xenoage.zong.core.music.format

import com.xenoage.utils.Mm
import com.xenoage.zong.core.music.LPf


/**
 * Staff position.
 *
 * This means a position with a horizontal coordinate in mm
 * and a vertical coordinate as a line position [LPf].
 */
class SP(
	/** Position on x-axis in mm, relative to the left side of the parent staff.  */
	val xMm: Mm,
	/** Position on y-axis as a line position.  */
	val lp: LPf
) {

	operator fun plus(sp: SP): SP {
		return SP(xMm + sp.xMm, lp + sp.lp)
	}

}
