package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML left-center-right.
 * 
 * @author Andreas Wenger
 */
public enum MxlLeftCenterRight {

	Left,
	Center,
	Right,
	Unknown;

	
	public static MxlLeftCenterRight read(XmlReader reader, String attrName) {
		return Utils.readOr("left-center-right", reader.getAttribute(attrName), values(), Unknown);
	}

	public void write(XmlWriter writer, String attrName) {
		if (this != Unknown)
			writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
