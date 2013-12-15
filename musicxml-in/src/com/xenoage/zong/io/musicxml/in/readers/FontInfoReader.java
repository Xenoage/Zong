package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.graphics.font.FontInfo;
import com.xenoage.utils.graphics.font.FontStyle;
import com.xenoage.utils.pdlib.PVector;
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
public final class FontInfoReader
{
	
	
	@NeverNull public static FontInfo readFontInfo(MxlFont mxlFont, FontInfo defaultFont)
	{
		//font
		PVector<String> families = mxlFont.fontFamily;
		if (families == null)
			families = defaultFont.getFamiliesNull();
		
		//size
		Float size = defaultFont.getSizeNull();
		MxlFontSize mxlSize = mxlFont.fontSize;
		if (mxlSize != null)
		{
			if (mxlSize.isSizePt())
			{
				size = mxlSize.getValuePt();
			}
			else
			{
				size = readCSSFontSize(mxlSize.getValueCSS());
			}
		}
		
		//style and weight
		FontStyle style = defaultFont.getStyleNull();
		if (mxlFont.fontStyle != null || mxlFont.fontWeight != null)
		{
			style = FontStyle.normal;
			if (mxlFont.fontStyle == MxlFontStyle.Italic)
			{
				style = style.with(FontStyle.Italic, true);
			}
			if (mxlFont.fontWeight == MxlFontWeight.Bold)
			{
				style = style.with(FontStyle.Bold, true);
			}
		}
		
		return new FontInfo(families, size, style);
	}
	
	
	private static float readCSSFontSize(MxlCSSFontSize mxlCSSSize)
	{
		switch (mxlCSSSize)
		{
			case XXSmall: return 6f;
			case XSmall: return 8f;
			case Small: return 10f;
			case Medium: return 12f;
			case Large: return 16f;
			case XLarge: return 20f;
			case XXLarge: return 28f;
		}
		throw new IllegalArgumentException("Unknown font-size: " + mxlCSSSize);
	}
	

}
