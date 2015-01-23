package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML group-barline-value.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlGroupBarlineValue
	implements EnumWithXmlNames {

	Yes("yes"),
	No("no"),
	Mensurstrich("Mensurstrich");

	private final String xmlName;


	public static MxlGroupBarlineValue read(XmlReader reader) {
		return Utils.read("group-barline-value", reader.getText(), values());
	}

	public void write(XmlWriter writer) {
		writer.writeText(xmlName);
	}

}
