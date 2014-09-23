package com.xenoage.zong.webapp.symbols.path;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * Wrapper for HTML5 canvas path drawing methods.
 * 
 * @author Andreas Wenger
 */
public interface PathElement {

	/**
	 * Draws this element on the given HTML5 canvas context.
	 */
	public void draw(Context2d context);
	
}
