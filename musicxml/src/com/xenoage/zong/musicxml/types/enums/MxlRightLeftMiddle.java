package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML right-left-middle.
 * Only named "location" in barline element.
 * 
 * @author Andreas Wenger
 */
public enum MxlRightLeftMiddle {

	Right,
	Left,
	Middle;

	public static final String attrName = "location";


	@MaybeNull public static MxlRightLeftMiddle read(XmlReader reader) {
		return Utils.readOrNull(attrName, reader.getAttribute(attrName), values());
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
