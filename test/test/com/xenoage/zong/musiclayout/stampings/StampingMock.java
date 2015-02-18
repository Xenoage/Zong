package com.xenoage.zong.musiclayout.stampings;

import lombok.Getter;

import com.xenoage.utils.math.geom.Shape;
import com.xenoage.zong.musiclayout.stampings.Stamping;

/**
 * Mock class for a {@link Stamping}.
 *
 * @author Andreas Wenger
 */
public class StampingMock
	extends Stamping {
	
	@Getter private Level level;

	
	public StampingMock(Stamping.Level level, Shape boundingShape) {
		super(null, null, boundingShape);
		this.level = level;
	}

	@Override public StampingType getType() {
		return null;
	}

}
