package com.xenoage.zong.musicxml.types;

import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * MusicXML instrument.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MxlInstrument {

	public static final String elemName = "instrument";

	@NonNull private final String id;


	@NonNull public static MxlInstrument read(XmlReader reader) {
		return new MxlInstrument(reader.getAttributeNotNull("id"));
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("id", id);
		writer.writeElementEnd();
	}

}
