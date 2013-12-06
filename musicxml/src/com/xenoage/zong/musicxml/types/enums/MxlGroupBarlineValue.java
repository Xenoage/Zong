package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.text;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.Parse;


/**
 * MusicXML group-barline-value.
 * 
 * @author Andreas Wenger
 */
public enum MxlGroupBarlineValue
	implements EnumWithXmlNames
{
	
	Yes("yes"),
	No("no"),
	Mensurstrich("Mensurstrich");
	
	
	private final String xmlName;
	
	
	private MxlGroupBarlineValue(String xmlName)
	{
		this.xmlName = xmlName;
	}
	
	
	@Override public String getXmlName()
	{
		return xmlName;
	}
	
	
	@NeverNull public static MxlGroupBarlineValue read(Element e)
	{
		String s = text(e);
		if (s != null)
		{
			return throwNull(Parse.getEnumValueNamed(s, values()), e);
		}
		else
		{
			throw invalid(e);
		}
	}
	
	
	public void write(Element e)
	{
		e.setTextContent(xmlName);
	}
	

}
