package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML upright-inverted.
 * 
 * @author Andreas Wenger
 */
public enum MxlUprightInverted {

	Upright,
	Inverted;

	@MaybeNull public static MxlUprightInverted read(XmlReader reader, String attrName) {
		return Utils.readOrNull(attrName, reader.getAttribute(attrName), values());
	}

	public void write(XmlWriter writer, String attrName) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
