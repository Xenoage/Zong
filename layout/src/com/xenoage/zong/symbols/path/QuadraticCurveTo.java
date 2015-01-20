package com.xenoage.zong.symbols.path;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Quadratic Bezier curve segment.
 * 
 * @author Andreas Wenger
 */
@Const @Data
public class QuadraticCurveTo
	implements PathElement {

	private final PathElementType type = PathElementType.QuadraticCurveTo;
	public final Point2f cp, p;

	@Override public Point2f getTarget() {
		return p;
	}
	
}
