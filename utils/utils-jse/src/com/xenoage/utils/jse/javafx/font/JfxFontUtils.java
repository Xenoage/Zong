package com.xenoage.utils.jse.javafx.font;

import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.FontStyle;

import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Useful methods for working with JavaFX fonts.
 * 
 * @author Andreas Wenger
 */
public class JfxFontUtils {
	
	public static Font toJavaFXFont(FontInfo font) {
		String family = font.getFamilies().get(0);
		FontWeight weight = (font.getStyle().isSet(FontStyle.Bold) ? FontWeight.BOLD : FontWeight.NORMAL);
		FontPosture posture = (font.getStyle().isSet(FontStyle.Italic) ? FontPosture.ITALIC : FontPosture.REGULAR);
		double size = font.getSize();
		return Font.font(family, weight, posture, size);
	}

}
