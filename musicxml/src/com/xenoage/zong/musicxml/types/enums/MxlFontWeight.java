package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML font-weight.
 * 
 * @author Andreas Wenger
 */
public enum MxlFontWeight {

	Normal,
	Bold,
	Unknown;

	public static final String attrName = "font-weight";


	public static MxlFontWeight read(XmlReader reader) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), Unknown);
	}

	public void write(XmlWriter writer) {
		if (this != Unknown)
			writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
