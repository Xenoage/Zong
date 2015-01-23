package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML syllabic.
 * 
 * @author Andreas Wenger
 */
public enum MxlSyllabic {

	Single,
	Begin,
	End,
	Middle;

	public static final String elemName = "syllabic";


	public static MxlSyllabic read(XmlReader reader) {
		return Utils.read(elemName, reader.getText(), values());
	}

	public void write(XmlWriter writer) {
		writer.writeElementText(elemName, toString().toLowerCase());
	}

}
