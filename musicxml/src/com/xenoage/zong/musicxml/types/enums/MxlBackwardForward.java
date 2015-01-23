package com.xenoage.zong.musicxml.types.enums;

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


	public static MxlBackwardForward read(XmlReader reader) {
		return Utils.read(attrName, reader.getAttribute(attrName), values());
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
