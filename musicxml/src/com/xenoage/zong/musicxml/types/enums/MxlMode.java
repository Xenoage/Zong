package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.EnumUtils.getEnumValue;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML mode.
 * 
 * @author Andreas Wenger
 */
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


	private MxlMode(String xmlName) {
		this.xmlName = xmlName;
	}

	@Override public String getXmlName() {
		return xmlName;
	}

	@NonNull public static MxlMode read(XmlReader reader) {
		String s = reader.getText();
		MxlMode mode = getEnumValue(s, values());
		if (mode == null)
			reader.throwDataException(elemName + " = " + s);
		return mode;
	}

	public void write(XmlWriter writer) {
		writer.writeElementText(elemName, xmlName);
	}

}
