package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML time-symbol.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlTimeSymbol
	implements EnumWithXmlNames {

	Common("common"),
	Cut("cut"),
	SingleNumber("single-number"),
	Normal("normal");

	public static final String attrName = "symbol";

	private final String xmlName;

	
	@MaybeNull public static MxlTimeSymbol read(XmlReader reader) {
		return Utils.readOr(attrName, reader.getAttribute(attrName), values(), null);
	}

	public void write(XmlWriter writer) {
		writer.writeAttribute(attrName, xmlName);
	}

}
