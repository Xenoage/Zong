package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML scaling.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlScaling {

	public static final String elemName = "scaling";

	private float millimeters;
	private float tenths;
	

	@NonNull public static MxlScaling read(XmlReader reader) {
		Float millimeters = null, tenths = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("millimeters"))
				millimeters = reader.getTextFloatNotNull();
			else if (n.equals("tenths"))
				tenths = reader.getTextFloatNotNull();
			reader.closeElement();
		}
		if (millimeters == null)
			throw reader.dataException("millimeters unknown");
		if (tenths == null)
			throw reader.dataException("tenths unknown");
		return new MxlScaling(millimeters, tenths);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeElementText("millimeters", millimeters);
		writer.writeElementText("tenths", tenths);
		writer.writeElementEnd();
	}

}
