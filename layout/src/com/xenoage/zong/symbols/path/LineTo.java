package com.xenoage.zong.symbols.path;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Line segment.
 * 
 * @author Andreas Wenger
 */
@Const @Data
public final class LineTo
	implements PathElement {

	private final PathElementType type = PathElementType.LineTo;
	public final Point2f p;

	@Override public Point2f getTarget() {
		return p;
	}
	
}
