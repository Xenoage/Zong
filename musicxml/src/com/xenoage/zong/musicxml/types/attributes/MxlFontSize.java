package com.xenoage.zong.musicxml.types.attributes;

import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.enums.MxlCSSFontSize;

/**
 * MusicXML font-size.
 * 
 * @author Andreas Wenger
 */
@Getter
public final class MxlFontSize {

	public static final String attrName = "font-size";

	/** Gets the font-size in points. If null, {@link #getValueCSS()} returns a non-null value. */
	@MaybeNull private final Float valuePt;
	/** The font-size as a CSS font size. If null, {@link #getValueFloat()} returns a non-null value. */
	@MaybeNull private final MxlCSSFontSize valueCSS;


	public MxlFontSize(Float valuePt) {
		this.valuePt = valuePt;
		this.valueCSS = null;
	}

	public MxlFontSize(MxlCSSFontSize valueCSS) {
		this.valuePt = null;
		this.valueCSS = valueCSS;
	}

	/**
	 * Returns true, if size is in pt, otherwise if size is in CSS, false.
	 */
	public boolean isSizePt() {
		return valuePt != null;
	}

	/**
	 * Returns the font-size from the attribute
	 * or returns null, if there is none.
	 */
	@MaybeNull public static MxlFontSize read(XmlReader reader) {
		String s = reader.getAttributeValue(attrName);
		if (s != null) {
			if (Character.isDigit(s.charAt(0))) {
				return new MxlFontSize(Float.parseFloat(s));
			}
			else {
				return new MxlFontSize(MxlCSSFontSize.read(s));
			}
		}
		else {
			return null;
		}
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, (valuePt != null ? "" + valuePt : valueCSS.getXmlName()));
	}

}
