package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.Parse.getEnumValueNamed;
import static com.xenoage.utils.xml.XMLReader.text;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.xml.EnumWithXmlNames;



/**
 * MusicXML mode.
 * 
 * @author Andreas Wenger
 */
public enum MxlMode
	implements EnumWithXmlNames
{
	
	Major("major"),
	Minor("minor"),
	Dorian("dorian"),
	Phrygian("phrygian"),
	Lydian("lydian"),
	Mixolydian("mixolydian"),
	Aeolian("aeolian"),
	Ionian("ionian"),
	Locrian("locrian");
	
	
	public static final String ELEM_NAME = "mode";
	
	private final String xmlName;
	
	
	private MxlMode(String xmlName)
	{
		this.xmlName = xmlName;
	}
	
	
	@Override public String getXmlName()
	{
		return xmlName;
	}
	
	
	@NeverNull public static MxlMode read(Element e)
	{
		return throwNull(getEnumValueNamed(text(e), values()), e);
	}
	
	
	public void write(Element parent)
	{
		addElement(ELEM_NAME, xmlName, parent);
	}
	

}
