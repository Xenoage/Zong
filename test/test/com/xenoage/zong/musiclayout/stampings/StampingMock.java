package com.xenoage.zong.musiclayout.stampings;

import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * Mock class for a {@link Stamping}.
 *
 * @author Andreas Wenger
 */
public class StampingMock
	extends Stamping {

	public StampingMock(Stamping.Level level, Shape boundingShape) {
		super(null, level, null, boundingShape);
	}

	@Override public StampingType getType() {
		return null;
	}

}
