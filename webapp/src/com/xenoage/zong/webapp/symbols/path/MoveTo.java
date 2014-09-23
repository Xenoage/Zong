package com.xenoage.zong.webapp.symbols.path;

import com.google.gwt.canvas.dom.client.Context2d;
import com.xenoage.utils.math.geom.Point2f;

import lombok.Data;

/**
 * Wrapper for HTML5 canvas moveTo(x, y).
 * 
 * @author Andreas Wenger
 */
@Data public class MoveTo
	implements PathElement {
	
	private final Point2f p;
	
	@Override public void draw(Context2d context) {
		context.moveTo(p.x, p.y);
	}

}