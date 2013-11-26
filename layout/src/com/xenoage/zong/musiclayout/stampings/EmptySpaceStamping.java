package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * Layout element for empty space, which means the blank paper.
 *
 * @author Andreas Wenger
 */
@Const public class EmptySpaceStamping
	extends Stamping {

	public EmptySpaceStamping(Rectangle2f rectangle) {
		super(null, Level.EmptySpace, null, rectangle);
	}

	/**
	 * Gets the type of this stamping.
	 */
	@Override public StampingType getType() {
		return StampingType.EmptySpaceStamping;
	}

}
