package com.xenoage.zong.renderer.javafx.background;

import javafx.scene.paint.Paint;

import com.xenoage.utils.jse.javafx.color.JfxColorUtils;
import com.xenoage.zong.layout.frames.background.Background;
import com.xenoage.zong.layout.frames.background.ColorBackground;

/**
 * JavaFX background renderer.
 * 
 * @author Andreas Wenger
 */
public class JfxBackgroundRenderer {

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
		return JfxColorUtils.toJavaFXColor(background.getColor());
	}

}
