package com.xenoage.zong.io.musicxml.in.readers;

import static com.xenoage.utils.collections.CList.ilist;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.collections.IList;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.utils.font.FontStyle;
import com.xenoage.zong.musicxml.types.attributes.MxlFont;
import com.xenoage.zong.musicxml.types.attributes.MxlFontSize;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.enums.MxlCSSFontSize;
import com.xenoage.zong.musicxml.types.enums.MxlFontStyle;
import com.xenoage.zong.musicxml.types.enums.MxlFontWeight;
import com.xenoage.zong.musicxml.types.util.MxlPrintStyleContent;

/**
 * This class reads elements containing a font-group into
 * {@link FontInfo} objects.
 * 
 * @author Andreas Wenger
 */
@RequiredArgsConstructor
public class FontInfoReader {
	
	private final MxlFont mxlFont;
	private final FontInfo defaultFont;
	
	/**
	 * Reads the font from the given element, if it is a {@link MxlPrintStyleContent} element
	 * and contains font information. The returned font is based on the given default font.
	 */
	@MaybeNull public static FontInfo read(Object printStyleElement, FontInfo defaultFont) {
		if (false == printStyleElement instanceof MxlPrintStyleContent)
			return null;
		MxlPrintStyle mxlPrintStyle = ((MxlPrintStyleContent) printStyleElement).getPrintStyle();
		MxlFont mxlFont = mxlPrintStyle.getFont();
		return new FontInfoReader(mxlFont, defaultFont).read();
	}

	@MaybeNull public FontInfo read() {
		IList<String> families = readFamilies();
		Float size = readSize();
		FontStyle style = readStyle();
		return new FontInfo(families, size, style);
	}

	private FontStyle readStyle() {
		FontStyle style = defaultFont.getStyleOrNull();
		//font style
		MxlFontStyle mxlStyle = mxlFont.getFontStyle();
		if (mxlStyle != MxlFontStyle.Unknown) {
			boolean isItalic = mxlStyle == MxlFontStyle.Italic;
			style = style.with(FontStyle.Italic, isItalic);
		}
		//font weight
		MxlFontWeight mxlWeight = mxlFont.getFontWeight();
		if (mxlWeight != null) {
			boolean isBold = mxlWeight == MxlFontWeight.Bold;
			style = style.with(FontStyle.Bold, isBold);
		}
		return style;
	}

	private IList<String> readFamilies() {
		List<String> mxlFamilies = mxlFont.getFontFamily();
		IList<String> families = null;
		if (mxlFamilies.size() == 0)
			mxlFamilies = defaultFont.getFamiliesOrNull();
		if (mxlFamilies != null)
			families = ilist(mxlFamilies);
		return families;
	}
	
	private Float readSize() {
		MxlFontSize mxlSize = mxlFont.getFontSize();
		if (mxlSize.getValuePt() != null)
			return mxlSize.getValuePt();
		else if (mxlSize.getValueCSS() != null)
			return readCSSFontSize(mxlSize.getValueCSS());
		else
			return defaultFont.getSizeOrNull();
	}

	private float readCSSFontSize(MxlCSSFontSize mxlCSSSize) {
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
