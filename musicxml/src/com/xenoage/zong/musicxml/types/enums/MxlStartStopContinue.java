package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.EnumUtils.getEnumValue;

import com.xenoage.utils.xml.XmlReader;

/**
 * MusicXML start-stop-continue.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStopContinue {

	Start,
	Stop,
	Continue;

	public static MxlStartStopContinue read(String s, XmlReader reader) {
		if (s == null)
			reader.throwDataException();
		return getEnumValue(s, values());
	}

	public String write() {
		return toString().toLowerCase();
	}

}
