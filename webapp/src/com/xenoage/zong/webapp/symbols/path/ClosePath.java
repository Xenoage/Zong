package com.xenoage.zong.webapp.symbols.path;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * Wrapper for HTML5 canvas closePath().
 * 
 * @author Andreas Wenger
 */
public class ClosePath
	implements PathElement {
	
	@Override public void draw(Context2d context) {
		context.closePath();
	}

}
