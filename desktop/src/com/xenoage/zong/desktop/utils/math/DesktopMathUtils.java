package com.xenoage.zong.desktop.utils.math;

import java.awt.geom.Rectangle2D;

import com.xenoage.utils.math.geom.Rectangle2f;

/**
 * Useful additions to the math utils for the desktop platform.
 * 
 * @author Andreas Wenger
 */
public class DesktopMathUtils {
	
	public static Rectangle2f createRectangle2f(Rectangle2D r) {
		return new Rectangle2f((float) r.getMinX(), (float) r.getMinY(), (float) r.getWidth(),
			(float) r.getHeight());
	}

}
