package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML stem-value.
 * 
 * @author Andreas Wenger
 */
public enum MxlStemValue {

	Down,
	Up,
	Double,
	None;

	
	public static MxlStemValue read(XmlReader reader) {
		return Utils.read("stem-value", reader.getText(), values());
	}

	public void write(XmlWriter writer) {
		writer.writeText(toString().toLowerCase());
	}

}
