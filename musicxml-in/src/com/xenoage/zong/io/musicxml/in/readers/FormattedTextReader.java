package com.xenoage.zong.io.musicxml.in.readers;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.color.Color;
import com.xenoage.utils.font.FontInfo;
import com.xenoage.zong.core.text.Alignment;
import com.xenoage.zong.core.text.FormattedText;
import com.xenoage.zong.core.text.FormattedTextStyle;
import com.xenoage.zong.core.text.Superscript;
import com.xenoage.zong.io.musicxml.Equivalents;
import com.xenoage.zong.musicxml.types.MxlFormattedText;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;

/**
 * Reads MusicXML elements with formatting information to a {@link FormattedText}.
 * 
 * @author Andreas Wenger
 */
public class FormattedTextReader
	implements TextReader {

	@Override public FormattedText readText(MxlFormattedText mxlText) {
		String text = mxlText.getValue().trim();
		FormattedTextStyle style = readStyle(mxlText);
		Alignment alignment = readAlignment(mxlText);
		return FormattedText.fText(text, style, alignment);
	}
	
	@NonNull public FormattedTextStyle readStyle(MxlFormattedText mxlText) {
		MxlPrintStyle mxlPrintStyle = getPrintStyle(mxlText);
		FontInfo font = FontInfoReader.readFontInfo(mxlPrintStyle.getFont(), FontInfo.defaultValue);
		Color color = readColor(mxlPrintStyle);
		return new FormattedTextStyle(font, color, Superscript.Normal);
	}
	
	@NonNull public Color readColor(MxlPrintStyle printStyle) {
		if (printStyle.getColor() == null)
			return FormattedTextStyle.defaultColor;
		return printStyle.getColor().getValue();
	}
	
	public Alignment readAlignment(MxlFormattedText mxlText) {
		return Equivalents.alignments.get1(mxlText.getJustify());
	}
	
	@NonNull public MxlPrintStyle getPrintStyle(MxlFormattedText mxlText) {
		MxlPrintStyle mxlPrintStyle = mxlText.getPrintStyle();
		return mxlPrintStyle != null ? mxlPrintStyle : MxlPrintStyle.empty;
	}

}
