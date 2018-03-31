package com.xenoage.utils.gwt.font;

import com.xenoage.utils.font.FontInfo;

import java.util.List;

import static com.xenoage.utils.font.FontStyle.Bold;
import static com.xenoage.utils.font.FontStyle.Italic;
import static com.xenoage.utils.kernel.Range.range;

/**
 * Useful methods for working with fonts in GWT.
 *
 * @author Andreas Wenger
 */
public class GwtFontUtils {

	/**
	 * Returns a CSS font string from the given {@link FontInfo}.
	 * Supports italic, bold, font size and families.
	 */
	public static String getCssFont(FontInfo font) {
		String ret = "";
		if (font.getStyle().isSet(Italic))
			ret += "italic ";
		if (font.getStyle().isSet(Bold))
			ret += "bold ";
		ret += font.getSize() + "pt ";
		List<String> families = font.getFamilies();
		for (int i : range(families)) {
			String family = families.get(i);
			if (family.contains(" "))
				family = "\"" + family + "\"";
			ret += family;
			if (i < families.size() - 1)
				ret += ",";
		}
		return ret;
	}

}
