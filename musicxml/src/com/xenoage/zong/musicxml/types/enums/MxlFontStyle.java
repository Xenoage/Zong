package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML font-style.
 * 
 * @author Andreas Wenger
 */
public enum MxlFontStyle {

	Normal,
	Italic,
	Unknown;

	public static final String attrName = "font-style";


	public static MxlFontStyle read(XmlReader reader) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), Unknown);
	}

	public void write(XmlWriter writer) {
		if (this != Unknown)
			writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
