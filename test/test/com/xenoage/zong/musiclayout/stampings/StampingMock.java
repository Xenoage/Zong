package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.math.geom.Shape;

/**
 * Mock class for a {@link Stamping}.
 *
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public class StampingMock
	extends Stamping {
	
	private Level level;
	private Shape boundingShape;


	@Override public StampingType getType() {
		return null;
	}

}
