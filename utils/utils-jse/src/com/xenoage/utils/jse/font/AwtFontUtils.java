package com.xenoage.utils.jse.font;

import static com.xenoage.utils.collections.CList.ilist;

import java.awt.Font;

import com.xenoage.utils.collections.IList;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.FontStyle;


/**
 * Useful methods for working with AWT fonts.
 * 
 * @author Andreas Wenger
 */
public class AwtFontUtils {

	private static final String defaultFamily = "Serif"; //should always be available in AWT


	/**
	 * Creates a new {@link FontInfo}.
	 * @param font      the AWT font
	 */
	public static FontInfo fromAwtFont(Font font) {
		IList<String> families = ilist(font.getFamily());
		FontStyle style = FontStyle.normal;
		if (font.isBold())
			style = style.with(FontStyle.Bold, true);
		if (font.isItalic())
			style = style.with(FontStyle.Italic, true);
		return new FontInfo(families, font.getSize2D(), style);
	}


	/**
	 * Gets the {@link Font} that matches best to the values of the
	 * given {@link FontInfo} object.
	 */
	public static Font toAwtFont(FontInfo fontInfo) {
		if (fontInfo == null) //TIDY
			return toAwtFont(FontInfo.defaultValue);
		
		//find an appropriate family:
		//go through all families, until a known family is found. if no family
		//is found, look for replacements. If also not found, take the base font family.
		String fontFamily = null;
		for (String family : fontInfo.getFamilies()) {
			if (FontUtils.getInstance().isFontFamilySupported(family)) {
				fontFamily = family;
				break;
			}
		}
		if (fontFamily == null) {
			for (String family : fontInfo.getFamilies()) {
				String replacement = FontReplacements.getInstance().getReplacement(family);
				if (replacement != family && FontUtils.getInstance().isFontFamilySupported(replacement)) {
					fontFamily = replacement;
					break;
				}
			}
		}
		if (fontFamily == null) {
			fontFamily = defaultFamily;
		}

		//size
		float fontSize = fontInfo.getSize();

		//style
		FontStyle style = fontInfo.getStyle();
		int fontStyle = Font.PLAIN;
		fontStyle |= (style.isSet(FontStyle.Bold) ? Font.BOLD : 0);
		fontStyle |= (style.isSet(FontStyle.Italic) ? Font.ITALIC : 0);

		return new Font(fontFamily, fontStyle, Math.round(fontSize));
	}

}
