package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML clef-sign.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlClefSign
	implements EnumWithXmlNames {

	G("G"),
	F("F"),
	C("C"),
	Percussion("percussion"),
	TAB("TAB"),
	None("none");

	public static final String elemName = "sign";

	private final String xmlName;


	public static MxlClefSign read(XmlReader reader) {
		return Utils.read(elemName, reader.getText(), values());
	}

	public void write(XmlWriter writer) {
		writer.writeElementText(elemName, xmlName);
	}

}
