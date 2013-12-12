package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML font-style.
 * 
 * @author Andreas Wenger
 */
public enum MxlFontStyle {

	Normal,
	Italic;

	public static final String attrName = "font-style";


	@MaybeNull public static MxlFontStyle read(XmlReader reader) {
		return Utils.readOrNull(attrName, reader.getAttribute(attrName), values());
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
