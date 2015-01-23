package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML placement.
 * 
 * @author Andreas Wenger
 */
public enum MxlPlacement {

	Above,
	Below,
	Unknown;

	public static final String attrName = "placement";


	public static MxlPlacement read(XmlReader reader) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), Unknown);
	}

	public void write(XmlWriter writer) {
		if (this != Unknown)
			writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
