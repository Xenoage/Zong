package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

/**
 * MusicXML typed-text.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlTypedText {

	@NonNull private String value;
	@MaybeNull private String type;


	@NonNull public static MxlTypedText read(XmlReader reader) {
		String type = reader.getAttribute("type");
		String value = reader.getTextNotNull();
		return new MxlTypedText(value, type);
	}

	public void write(String elementName, XmlWriter writer) {
		writer.writeElementStart(elementName);
		writer.writeAttribute("type", type);
		writer.writeText(value);
		writer.writeElementEnd();
	}

}
