package com.xenoage.utils.jse.color;

import com.xenoage.utils.color.Color;

import static com.xenoage.utils.color.Color.color;


/**
 * Useful methods for working with AWT colors.
 * 
 * @author Andreas Wenger
 */
public class AwtColorUtils {

	/**
	 * Gets the {@link Color} from the given AWT color.
	 * @param awtColor  the AWT color
	 */
	public static Color fromAwtColor(java.awt.Color awtColor) {
		return color(awtColor.getRed(), awtColor.getGreen(),
				awtColor.getBlue(), awtColor.getAlpha());
	}

	/**
	 * Gets the AWT color from the given {@link Color}.
	 */
	public static java.awt.Color toAwtColor(Color color) {
		return new java.awt.Color(color.r, color.g, color.b, color.a);
	}

}
