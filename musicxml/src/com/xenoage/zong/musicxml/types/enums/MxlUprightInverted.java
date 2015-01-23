package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML upright-inverted.
 * 
 * @author Andreas Wenger
 */
public enum MxlUprightInverted {

	Upright,
	Inverted,
	Unknown;

	
	public static MxlUprightInverted read(XmlReader reader, String attrName) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), Unknown);
	}

	public void write(XmlWriter writer, String attrName) {
		if (this != Unknown)
			writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
