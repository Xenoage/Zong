package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.EnumUtils.getEnumValue;

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
		String s = reader.getAttributeString(attrName);
		MxlPlacement ret = getEnumValue(s, values());
		if (ret == null)
			reader.throwDataException(attrName + " = " + s);
		return ret;
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
