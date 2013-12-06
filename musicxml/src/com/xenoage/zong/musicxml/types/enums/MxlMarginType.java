package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.xml.Parse.getEnumValueNamed;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.xml.EnumWithXmlNames;


/**
 * MusicXML margin-type.
 * 
 * @author Andreas Wenger
 */
public enum MxlMarginType
	implements EnumWithXmlNames
{
	
	Odd("odd"),
	Even("even"),
	Both("both");
	
	
	public static final String ATTR_NAME = "type";
	
	private final String xmlName;
	
	
	private MxlMarginType(String xmlName)
	{
		this.xmlName = xmlName;
	}
	
	
	@Override public String getXmlName()
	{
		return xmlName;
	}
	
	
	@MaybeNull public static MxlMarginType read(Element e)
	{
		return getEnumValueNamed(attribute(e, ATTR_NAME), values());
	}
	
	
	public void write(Element e)
	{
		addAttribute(e, ATTR_NAME, xmlName);
	}

}
