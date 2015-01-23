package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML valign.
 * 
 * @author Andreas Wenger
 */
public enum MxlVAlign {

	Top,
	Middle,
	Bottom,
	Baseline;

	public static final String attrName = "valign";


	@MaybeNull public static MxlVAlign read(XmlReader reader) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), null);
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
