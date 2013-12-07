package com.xenoage.zong.musicxml.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML mode.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter
public enum MxlMode
	implements EnumWithXmlNames {

	Major("major"),
	Minor("minor"),
	Dorian("dorian"),
	Phrygian("phrygian"),
	Lydian("lydian"),
	Mixolydian("mixolydian"),
	Aeolian("aeolian"),
	Ionian("ionian"),
	Locrian("locrian");

	public static final String elemName = "mode";

	private final String xmlName;


	@NonNull public static MxlMode read(XmlReader reader) {
		return Utils.read(elemName, reader.getText(), values());
	}

	public void write(XmlWriter writer) {
		writer.writeElementText(elemName, xmlName);
	}

}
