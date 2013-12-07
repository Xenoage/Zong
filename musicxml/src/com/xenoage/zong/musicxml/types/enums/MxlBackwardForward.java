package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML backward-forward.
 * Only named "direction" in repeat element.
 * 
 * @author Andreas Wenger
 */
public enum MxlBackwardForward {

	Backward,
	Forward;

	public static final String attrName = "direction";


	@NonNull public static MxlBackwardForward read(XmlReader reader) {
		return Utils.read(attrName, reader.getAttributeString(attrName), values());
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
