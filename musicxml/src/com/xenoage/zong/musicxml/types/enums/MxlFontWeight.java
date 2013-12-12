package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML font-weight.
 * 
 * @author Andreas Wenger
 */
public enum MxlFontWeight {

	Normal,
	Bold;

	public static final String attrName = "font-weight";


	@MaybeNull public static MxlFontWeight read(XmlReader reader) {
		return Utils.readOrNull(attrName, reader.getAttribute(attrName), values());
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
