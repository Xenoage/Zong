package com.xenoage.zong.musiclayout.stampings;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.annotations.Demo;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.math.geom.Point2f;
import com.xenoage.utils.math.geom.Size2f;

/**
 * A {@link TestStamping} is the border of a rectangle
 * at the given position with the given size in a given color.
 *
 * @author Andreas Wenger
 */
@Demo @Const @AllArgsConstructor @Getter
public final class TestStamping
	extends Stamping {

	/** The top-left position in mm in frame space. */
	public final Point2f positionMm;
	public final Size2f sizeMm;
	public final Color color;
	

	@Override public StampingType getType() {
		return StampingType.TestStamping;
	}

	@Override public Level getLevel() {
		return Level.Music;
	}
	
}
