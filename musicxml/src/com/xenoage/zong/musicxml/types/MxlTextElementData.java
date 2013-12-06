package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.text;

import org.w3c.dom.Element;


/**
 * MusicXML text-element-data.
 * 
 * Only the actual text content is used, and format
 * information is ignored. 
 * 
 * @author Andreas Wenger
 */
public final class MxlTextElementData
{

	private final String value;

	
	public MxlTextElementData(String value)
	{
		this.value = value;
	}

	
	public String getValue()
	{
		return value;
	}
	
	
	public static MxlTextElementData read(Element e)
	{
		return new MxlTextElementData(text(e));
	}
	
	
	public void write(Element e)
	{
		e.setTextContent(value);
	}
	

}
