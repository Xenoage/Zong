package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.Parse.getEnumValueNamed;
import static com.xenoage.utils.xml.XMLReader.text;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.xml.EnumWithXmlNames;



/**
 * MusicXML clef-sign.
 * 
 * @author Andreas Wenger
 */
public enum MxlClefSign
	implements EnumWithXmlNames
{
	
	G("G"),
	F("F"),
	C("C"),
	Percussion("percussion"),
	TAB("TAB"),
	None("none");
	
	
	public static final String ELEM_NAME = "sign";
	
	private final String xmlName;
	
	
	private MxlClefSign(String xmlName)
	{
		this.xmlName = xmlName;
	}
	
	
	@Override public String getXmlName()
	{
		return xmlName;
	}
	
	
	@NeverNull public static MxlClefSign read(Element e)
	{
		return throwNull(getEnumValueNamed(text(e), values()), e);
	}
	
	
	public void write(Element parent)
	{
		addElement(ELEM_NAME, xmlName, parent);
	}
	

}
