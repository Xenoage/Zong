package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;
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
	Right;

	@MaybeNull public static MxlLeftCenterRight read(XmlReader reader, String attrName) {
		return Utils.readOrNull("left-center-right", reader.getAttributeString(attrName), values());
	}

	public void write(XmlWriter writer, String attrName) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
