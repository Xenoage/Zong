package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLReader.elementText;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML score-instrument.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="solo,ensemble")
public final class MxlScoreInstrument
{
	
	public static final String ELEM_NAME = "score-instrument";
	
	@NeverNull private final String instrumentName;
	@MaybeNull private final String instrumentAbbreviation;
	@NeverNull private final String id;
	
	
	public MxlScoreInstrument(String instrumentName, String instrumentAbbreviation, String id)
	{
		this.instrumentName = instrumentName;
		this.instrumentAbbreviation = instrumentAbbreviation;
		this.id = id;
	}

	
	@NeverNull public String getInstrumentName()
	{
		return instrumentName;
	}

	
	@MaybeNull public String getInstrumentAbbreviation()
	{
		return instrumentAbbreviation;
	}

	
	@NeverNull public String getID()
	{
		return id;
	}
	
	
	@NeverNull public static MxlScoreInstrument read(Element e)
	{
		return new MxlScoreInstrument(
			throwNull(elementText(e, "instrument-name"), e),
			elementText(e, "instrument-abbreviation"),
			throwNull(attribute(e, "id"), e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addElement("instrument-name", instrumentName, e);
		addElement("instrument-abbreviation", instrumentAbbreviation, e);
		addAttribute(e, "id", id);
	}
	

}
