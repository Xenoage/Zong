package com.xenoage.zong.webapp.symbols.path;

import lombok.Data;

import com.google.gwt.canvas.dom.client.Context2d;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Wrapper for HTML5 canvas bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y).
 * 
 * @author Andreas Wenger
 */
@Data public class BezierCurveTo
	implements PathElement {
	
	private final Point2f cp1, cp2, p;

	@Override public void draw(Context2d context) {
		context.bezierCurveTo(cp1.x, cp1.y, cp2.x, cp2.y, p.x, p.y);
	}

}