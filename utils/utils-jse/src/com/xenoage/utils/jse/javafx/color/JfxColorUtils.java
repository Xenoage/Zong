package com.xenoage.utils.jse.javafx.color;

import static com.xenoage.utils.color.Color.color;

import com.xenoage.utils.color.Color;


/**
 * Useful methods for working with JavaFX colors.
 * 
 * @author Andreas Wenger
 */
public class JfxColorUtils {

	public static Color fromJavaFXColor(javafx.scene.paint.Color jfxColor) {
		return color(to255(jfxColor.getRed()), to255(jfxColor.getGreen()),
			to255(jfxColor.getBlue()), to255(jfxColor.getOpacity()));
	}

	public static javafx.scene.paint.Color toJavaFXColor(Color color) {
		return new javafx.scene.paint.Color(to1(color.r), to1(color.g),
			to1(color.b), to1(color.a));
	}
	
	private static int to255(double v1) {
		return (int) Math.round(v1 * 255);
	}
	
	private static double to1(int v255) {
		return v255 / 255.0;
	}

}
