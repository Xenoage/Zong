package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.CheckUtils.checkNotNull;
import static com.xenoage.utils.EnumUtils.getEnumValue;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML font-weight.
 * 
 * @author Andreas Wenger
 */
public enum MxlFontWeight {

	Normal,
	Bold;

	public static final String attrName = "font-weight";


	@MaybeNull public static MxlFontWeight read(XmlReader reader) {
		String s = reader.getAttributeString(attrName);
		if (s != null) {
			return checkNotNull(getEnumValue(s, values()), "unknown " + attrName);
		}
		else {
			return null;
		}
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
