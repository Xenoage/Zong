package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.utils.StringUtils.concatenate;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.MaybeEmpty;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlFontStyle;
import com.xenoage.zong.musicxml.types.enums.MxlFontWeight;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML font.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
@IncompleteMusicXML(partly="font-family") //font-family: enum for font names like "handwritten, cursive, fantasy"
public final class MxlFont {

	/** Generated from the comma-separated list of font names. */
	@MaybeEmpty private final List<String> fontFamily;
	private MxlFontStyle fontStyle;
	private final MxlFontSize fontSize;
	private final MxlFontWeight fontWeight;

	public static final MxlFont noFont = new MxlFont(Collections.<String>emptyList(), MxlFontStyle.Unknown,
		MxlFontSize.noFontSize, MxlFontWeight.Unknown);
	

	public static MxlFont read(XmlReader reader) {
		List<String> fontFamily = alist();
		String fontFamilies = reader.getAttribute("font-family");
		if (fontFamilies != null) {
			for (String s : fontFamilies.split(",")) {
				fontFamily.add(s.trim());
			}
		}
		MxlFontStyle fontStyle = MxlFontStyle.read(reader);
		MxlFontSize fontSize = MxlFontSize.read(reader);
		MxlFontWeight fontWeight = MxlFontWeight.read(reader);
		if (fontFamily.size() > 0 || fontStyle != MxlFontStyle.Unknown ||
			fontSize != MxlFontSize.noFontSize || fontWeight != MxlFontWeight.Unknown)
			return new MxlFont(fontFamily, fontStyle, fontSize, fontWeight);
		else
			return noFont;
	}

	public void write(XmlWriter writer) {
		if (this != noFont) {
			if (fontFamily.size() > 0)
				writer.writeAttribute("font-family", concatenate(fontFamily, ","));
			fontStyle.write(writer);
			fontSize.write(writer);
			fontWeight.write(writer);
		}
	}

}
