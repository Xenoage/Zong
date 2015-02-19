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
		super(null, rectangle);
	}

	@Override public StampingType getType() {
		return StampingType.EmptySpaceStamping;
	}

	@Override public Level getLevel() {
		return Level.EmptySpace;
	}
	
}
