package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * Layout element for empty space, which means the blank paper.
 *
 * @author Andreas Wenger
 */
@Const @AllArgsConstructor
public class EmptySpaceStamping
	extends Stamping {
	
	@Getter public final Rectangle2f boundingShape;


	@Override public StampingType getType() {
		return StampingType.EmptySpaceStamping;
	}

	@Override public Level getLevel() {
		return Level.EmptySpace;
	}
	
}
