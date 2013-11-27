package com.xenoage.zong.musiclayout.stampings;

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
@Demo @Const public final class TestStamping
	extends Stamping {

	public final Point2f position;
	public final Size2f size;
	public final Color color;


	public TestStamping(Point2f position, Size2f size, Color color) {
		super(null, Level.Music, null, null);
		this.position = position;
		this.size = size;
		this.color = color;
	}

	@Override public StampingType getType() {
		return StampingType.TestStamping;
	}

}
