package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML beam-value.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlBeamValue
	implements EnumWithXmlNames {

	Begin("begin"),
	Continue("continue"),
	End("end"),
	ForwardHook("forward hook"),
	BackwardHook("backward hook");

	private final String xmlName;


	public static MxlBeamValue read(XmlReader reader) {
		return Utils.read("beam-value", reader.getText(), values());
	}

	public void write(XmlWriter writer) {
		writer.writeText(xmlName);
	}

}
