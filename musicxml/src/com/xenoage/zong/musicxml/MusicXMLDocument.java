package com.xenoage.zong.musicxml;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlReader;
import com.xenoage.utils.xml.XmlWriter;
import com.xenoage.zong.musicxml.types.MxlScorePartwise;

/**
 * MusicXML document.
 * 
 * Only partwise scores are supported.
 * 
 * @author Andreas Wenger
 */
@AllArgsConstructor @Getter @Setter
public final class MusicXMLDocument {

	private MxlScorePartwise score;


	public static MusicXMLDocument read(XmlReader reader)
		throws XmlDataException {
		String n = reader.getElementName();
		if (n.equals(MxlScorePartwise.elemName))
			return new MusicXMLDocument(MxlScorePartwise.read(reader));
		else if (n.equals("score-timewise"))
			throw new IllegalArgumentException("Timewise scores are not supported.");
		throw new IllegalArgumentException("Unknown root element: " + n);
	}
	
	
	public void write(XmlWriter writer) {
		writer.writeStartDocument();
		writer.writeDTD("!DOCTYPE score-partwise PUBLIC \"-//Recordare//DTD MusicXML 2.0 Partwise//EN\"" + 
    " \"http://www.musicxml.org/dtds/partwise.dtd\"");
		score.write(writer);
	}

}
