package com.xenoage.utils.gwt.color;

import com.google.gwt.canvas.dom.client.CssColor;
import com.xenoage.utils.color.Color;

/**
 * Useful methods for working with GWT colors.
 * 
 * @author Andreas Wenger
 */
public class GwtColorUtils {

	public static final CssColor black = createColor(Color.black);


	/**
	 * Gets the GWT color from the given {@link Color}.
	 */
	public static CssColor createColor(Color color) {
		return CssColor.make("rgba(" + color.r + "," + color.g + "," + color.b + "," + color.a + ")");
	}

}
