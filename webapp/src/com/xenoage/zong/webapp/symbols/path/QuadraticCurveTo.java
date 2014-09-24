package com.xenoage.zong.webapp.symbols.path;

import com.google.gwt.canvas.dom.client.Context2d;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Wrapper for HTML5 canvas quadraticCurveTo(cpx, cpy, x, y).
 * 
 * @author Andreas Wenger
 */
public class QuadraticCurveTo
	implements PathElement {

	private final Point2f cp, p;


	public QuadraticCurveTo(Point2f cp, Point2f p) {
		this.cp = cp;
		this.p = p;
	}

	@Override public void draw(Context2d context) {
		context.quadraticCurveTo(cp.x, cp.y, p.x, p.y);
	}

}
