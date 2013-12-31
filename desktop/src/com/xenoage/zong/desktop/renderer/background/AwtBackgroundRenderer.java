package com.xenoage.zong.desktop.renderer.background;

import java.awt.Paint;

import com.xenoage.utils.jse.color.AwtColorUtils;
import com.xenoage.zong.layout.frames.background.Background;
import com.xenoage.zong.layout.frames.background.ColorBackground;

/**
 * AWT background renderer.
 * 
 * @author Andreas Wenger
 */
public class AwtBackgroundRenderer {

	/**
	 * Returns the Paint instance of the given background.
	 */
	public static Paint getPaint(Background background) {
		switch (background.getType()) {
			case Color:
				return getPaint((ColorBackground) background);
		}
		return null;
	}

	/**
	 * Returns the Paint instance of the given background.
	 */
	public static Paint getPaint(ColorBackground background) {
		return AwtColorUtils.toAwtColor(background.getColor());
	}

}
