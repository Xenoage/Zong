package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML margin-type.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlMarginType
	implements EnumWithXmlNames {

	Odd("odd"),
	Even("even"),
	Both("both");

	public static final String attrName = "type";

	private final String xmlName;


	@MaybeNull public static MxlMarginType read(XmlReader reader) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), null);
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, xmlName);
	}

}
