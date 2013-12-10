package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.utils.StringUtils.concatenate;
import static com.xenoage.utils.collections.CollectionUtils.alist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlFontStyle;
import com.xenoage.zong.musicxml.types.enums.MxlFontWeight;

/**
 * MusicXML font.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlFont {

	/** Generated from the comma-separated list of font names. */
	@MaybeNull private List<String> fontFamily;
	@MaybeNull private MxlFontStyle fontStyle;
	@MaybeNull private MxlFontSize fontSize;
	@MaybeNull private MxlFontWeight fontWeight;


	@MaybeNull public static MxlFont read(XmlReader reader) {
		List<String> fontFamily = alist();
		String fontFamilies = reader.getAttributeString("font-family");
		if (fontFamilies != null) {
			for (String s : fontFamilies.split(",")) {
				fontFamily.add(s.trim());
			}
		}
		MxlFontStyle fontStyle = MxlFontStyle.read(reader);
		MxlFontSize fontSize = MxlFontSize.read(reader);
		MxlFontWeight fontWeight = MxlFontWeight.read(reader);
		if (fontFamily.size() > 0 || fontStyle != null || fontSize != null || fontWeight != null)
			return new MxlFont(fontFamily, fontStyle, fontSize, fontWeight);
		else
			return null;
	}

	public void write(XmlWriter writer) {
		if (fontFamily != null && fontFamily.size() > 0)
			writer.writeAttribute("font-family", concatenate(fontFamily, ","));
		if (fontStyle != null)
			fontStyle.write(writer);
		if (fontSize != null)
			fontSize.write(writer);
		if (fontWeight != null)
			fontWeight.write(writer);
	}

}
