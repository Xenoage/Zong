package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CList.ilist;

import java.util.List;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.FontStyle;
import com.xenoage.zong.musicxml.types.attributes.MxlFont;
import com.xenoage.zong.musicxml.types.attributes.MxlFontSize;
import com.xenoage.zong.musicxml.types.enums.MxlCSSFontSize;
import com.xenoage.zong.musicxml.types.enums.MxlFontStyle;
import com.xenoage.zong.musicxml.types.enums.MxlFontWeight;

/**
 * This class reads elements containing a font-group into
 * {@link FontInfo} objects.
 * 
 * @author Andreas Wenger
 */
public final class FontInfoReader {

	@MaybeNull public static FontInfo readFontInfo(MxlFont mxlFont, FontInfo defaultFont) {
		if (mxlFont == null)
			return null;
		
		//font
		List<String> mxlFamilies = mxlFont.getFontFamily();
		IList<String> families = null;
		if (mxlFamilies.size() == 0)
			mxlFamilies = defaultFont.getFamiliesOrNull();
		if (mxlFamilies != null)
			families = ilist(mxlFamilies);

		//size
		Float size = defaultFont.getSizeOrNull();
		MxlFontSize mxlSize = mxlFont.getFontSize();
		if (mxlSize != null) {
			if (mxlSize.isSizePt()) {
				size = mxlSize.getValuePt();
			}
			else {
				size = readCSSFontSize(mxlSize.getValueCSS());
			}
		}

		//style and weight
		FontStyle style = defaultFont.getStyleOrNull();
		if (mxlFont.getFontStyle() != null || mxlFont.getFontWeight() != null) {
			style = FontStyle.normal;
			if (mxlFont.getFontStyle() == MxlFontStyle.Italic) {
				style = style.with(FontStyle.Italic, true);
			}
			if (mxlFont.getFontWeight() == MxlFontWeight.Bold) {
				style = style.with(FontStyle.Bold, true);
			}
		}

		return new FontInfo(families, size, style);
	}

	private static float readCSSFontSize(MxlCSSFontSize mxlCSSSize) {
		switch (mxlCSSSize) {
			case XXSmall:
				return 6f;
			case XSmall:
				return 8f;
			case Small:
				return 10f;
			case Medium:
				return 12f;
			case Large:
				return 16f;
			case XLarge:
				return 20f;
			case XXLarge:
				return 28f;
		}
		throw new IllegalArgumentException("Unknown font-size: " + mxlCSSSize);
	}

}
