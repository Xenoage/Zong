package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML accidental-text.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlAccidentalText
	implements EnumWithXmlNames {

	Sharp("sharp"),
	Natural("natural"),
	Flat("flat"),
	DoubleSharp("double-sharp"),
	SharpSharp("sharp-sharp"),
	FlatFlat("flat-flat"),
	NaturalSharp("natural-sharp"),
	NaturalFlat("natural-flat"),
	QuarterFlat("quarter-flat"),
	QuarterSharp("quarter-sharp"),
	ThreeQuartersFlat("three-quarters-flat"),
	ThreeQuartersSharp("three-quarters-sharp");

	private final String xmlName;


	public static MxlAccidentalText read(XmlReader reader) {
		return Utils.read("accidental-text", reader.getText(), values());
	}

	public void write(XmlWriter writer) {
		writer.writeText(xmlName);
	}

}
