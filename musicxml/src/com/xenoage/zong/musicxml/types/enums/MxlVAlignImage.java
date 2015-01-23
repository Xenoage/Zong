package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML valign-image.
 * Only named "valign" in the valign-image group.
 * 
 * @author Andreas Wenger
 */
public enum MxlVAlignImage {

	Top,
	Middle,
	Bottom,
	Unknown;

	public static final String attrName = "valign";


	public static MxlVAlignImage read(XmlReader reader) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), Unknown);
	}

	public void write(XmlWriter writer) {
		if (this != Unknown)
			writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
