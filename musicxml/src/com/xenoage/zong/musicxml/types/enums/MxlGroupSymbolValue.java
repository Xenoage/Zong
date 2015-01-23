package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML group-symbol-value.
 * 
 * @author Andreas Wenger
 */
public enum MxlGroupSymbolValue {

	None,
	Brace,
	Line,
	Bracket,
	Square;

	public static MxlGroupSymbolValue read(XmlReader reader) {
		return Utils.read("group-symbol-value", reader.getText(), values());
	}

	public void write(XmlWriter writer) {
		writer.writeText(toString().toLowerCase());
	}

}
