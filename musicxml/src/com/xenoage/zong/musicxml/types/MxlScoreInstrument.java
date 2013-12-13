package com.xenoage.zong.musicxml.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.annotations.MaybeNull;
import com.xenoage.utils.annotations.NonNull;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;

/**
 * MusicXML score-instrument.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing = "solo,ensemble")
@AllArgsConstructor @Getter @Setter
public final class MxlScoreInstrument {

	public static final String elemName = "score-instrument";

	@NonNull private String instrumentName;
	@MaybeNull private String instrumentAbbreviation;
	@NonNull private String id;


	@NonNull public static MxlScoreInstrument read(XmlReader reader) {
		//attributes
		String id = reader.getAttributeNotNull("id");
		//elements
		String instrumentName = null;
		String instrumentAbbreviation = null;
		while (reader.openNextChildElement()) {
			String n = reader.getElementName();
			if (n.equals("instrument-name"))
				instrumentName = reader.getTextNotNull();
			else if (n.equals("instrument-abbreviation"))
				instrumentAbbreviation = reader.getTextNotNull();
			reader.closeElement();
		}
		if (instrumentName == null)
			throw reader.dataException("instrument-name unknown");
		return new MxlScoreInstrument(instrumentName, instrumentAbbreviation, id);
	}

	public void write(XmlWriter writer) {
		writer.writeElementStart(elemName);
		writer.writeAttribute("id", id);
		writer.writeElementText("instrument-name", instrumentName);
		writer.writeElementText("instrument-abbreviation", instrumentAbbreviation);
		writer.writeElementEnd();
	}

}
