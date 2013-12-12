package com.xenoage.zong.musicxml;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;


import org.w3c.dom.Doc
ent;
import org.w3c.dom.Element;

import com.xenoage.utils.xml.XmlDataException;
import com.xenoage.utils.xml.XmlReader;
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
		if (n.equals(MxlScorePartwise.ELEM_NAME))
			return new MusicXMLDocument(MxlScorePartwise.read(e));
		else if (n.equals("score-timewise"))
			throw new IllegalArgumentException("Timewise scores are not supported.");
		throw new IllegalArgumentException("Unknown root element: " + n);
	}

}
