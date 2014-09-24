package com.xenoage.zong.webapp.symbols.path;

import com.google.gwt.canvas.dom.client.Context2d;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Wrapper for HTML5 canvas bezierCurveTo(cp1x, cp1y, cp2x, cp2y, x, y).
 * 
 * @author Andreas Wenger
 */
public class BezierCurveTo
	implements PathElement {

	private final Point2f cp1, cp2, p;


	public BezierCurveTo(Point2f cp1, Point2f cp2, Point2f p) {
		this.cp1 = cp1;
		this.cp2 = cp2;
		this.p = p;
	}

	@Override public void draw(Context2d context) {
		context.bezierCurveTo(cp1.x, cp1.y, cp2.x, cp2.y, p.x, p.y);
	}

}
