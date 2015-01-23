package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML bar-style.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter public enum MxlBarStyle
	implements EnumWithXmlNames {

	Regular("regular"),
	Dotted("dotted"),
	Dashed("dashed"),
	Heavy("heavy"),
	LightLight("light-light"),
	LightHeavy("light-heavy"),
	HeavyLight("heavy-light"),
	HeavyHeavy("heavy-heavy"),
	Tick("tick"),
	Short("short"),
	None("none");

	private final String xmlName;


	public static MxlBarStyle read(XmlReader reader) {
		String text = reader.getText();
		if (text != null)
			return Utils.read("bar-style", text, values());
		else //tolerate missing text
			return Regular;
	}

	public void write(XmlWriter writer) {
		writer.writeText(xmlName);
	}

}
