package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML instrument.
 * 
 * @author Andreas Wenger
 */
public final class MxlInstrument
{
	
	public static final String ELEM_NAME = "instrument";
	
	@NeverNull private final String id;

	
	public MxlInstrument(String id)
	{
		this.id = id;
	}

	
	@NeverNull public String getID()
	{
		return id;
	}
	
	
	@NeverNull public static MxlInstrument read(Element e)
	{
		return new MxlInstrument(throwNull(attribute(e, "id"), e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addAttribute(e, "id", id);
	}
	

}
