package com.xenoage.zong.webapp.symbols.path;

import lombok.Data;

import com.google.gwt.canvas.dom.client.Context2d;
import com.xenoage.utils.math.geom.Point2f;

/**
 * Wrapper for HTML5 canvas lineTo(x, y).
 * 
 * @author Andreas Wenger
 */
@Data public class LineTo
	implements PathElement {
	
	private final Point2f p;
	
	@Override public void draw(Context2d context) {
		context.lineTo(p.x, p.y);
	}

}