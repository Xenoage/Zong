package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML placement.
 * 
 * @author Andreas Wenger
 */
public enum MxlPlacement {

	Above,
	Below;

	public static final String attrName = "placement";


	@MaybeNull public static MxlPlacement read(XmlReader reader) {
		return Utils.readOrNull(attrName, reader.getAttribute(attrName), values());
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
