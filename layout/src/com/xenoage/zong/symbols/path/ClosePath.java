package com.xenoage.zong.symbols.path;

import lombok.Data;

import com.xenoage.utils.annotations.Const;
import com.xenoage.utils.math.geom.Point2f;


/**
 * Closes the path.
 * 
 * @author Andreas Wenger
 */
@Const @Data
public final class ClosePath
	implements PathElement {

	private final PathElementType type = PathElementType.ClosePath;

	@Override public Point2f getTarget() {
		return null;
	}
	
}
