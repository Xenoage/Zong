package com.xenoage.zong.symbols.path;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Cubic Bezier curve segment.
 * 
 * @author Andreas Wenger
 */
@Const @Data
public final class CubicCurveTo
	implements PathElement {

	private final PathElementType type = PathElementType.CubicCurveTo;
	public final Point2f cp1, cp2, p;

	@Override public Point2f getTarget() {
		return p;
	}
	
}
