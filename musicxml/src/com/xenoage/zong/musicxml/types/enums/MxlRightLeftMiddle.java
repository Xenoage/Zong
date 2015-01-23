package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML right-left-middle.
 * Only used in the barline element, named "location" .
 * 
 * @author Andreas Wenger
 */
public enum MxlRightLeftMiddle {

	Right,
	Left,
	Middle;

	public static final String attrName = "location";


	public static MxlRightLeftMiddle readOr(XmlReader reader, MxlRightLeftMiddle replacement) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), replacement);
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
