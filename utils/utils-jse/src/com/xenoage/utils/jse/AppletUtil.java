package com.xenoage.utils.jse;

import java.applet.Applet;
import java.awt.Container;
import java.awt.Frame;

/**
 * Useful methods for working with applets.
 * 
 * @author Andreas Wenger
 */
public class AppletUtil {

	/**
	 * Gets the parent {@link Frame} of the given applet, or null if none
	 * can be found.
	 */
	public static Frame getParentFrame(Applet applet) {
		Container c = applet;
		while (c != null) {
			if (c instanceof Frame)
				return (Frame) c;
			c = c.getParent();
		}
		return null;
	}

}
