package com.xenoage.zong.musicxml.types.enums;

import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlWriter;

/**
 * Reader and writer methods for MusicXML's yes-no,
 * since yes-no is represented by boolean values in this project.
 * 
 * @author Andreas Wenger
 */
public enum MxlYesNo {
	
	Yes,
	No,
	Unknown;

	
	public static MxlYesNo readRequired(String s) {
		if (s.equals("yes"))
			return Yes;
		else if (s.equals("no"))
			return No;
		throw new XmlDataException("yes-no = " + s);
	}

	public static MxlYesNo read(String s) {
		if (s == null)
			return Unknown;
		else
			return readRequired(s);
	}

	public void write(XmlWriter writer, String attrName) {
		if (this != Unknown)
			writer.writeAttribute(attrName, toString().toLowerCase());
	}

}
