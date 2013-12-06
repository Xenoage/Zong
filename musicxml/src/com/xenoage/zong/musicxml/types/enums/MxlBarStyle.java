package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.Parse.getEnumValueNamed;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.xml.EnumWithXmlNames;
import com.xenoage.utils.xml.XMLReader;


/**
 * MusicXML bar-style.
 * 
 * @author Andreas Wenger
 */
public enum MxlBarStyle
	implements EnumWithXmlNames
{
	
	Regular("regular"),
	Dotted("dotted"),
	Dashed("dashed"),
	Heavy("heavy"),
	LightLight("light-light"),
	LightHeavy("light-heavy"),
	HeavyLight("heavy-light"),
	HeavyHeavy("heavy-heavy"),
	Tick("tick"),
	Short("short"),
	None("none");
	
	
	private final String xmlName;
	
	
	private MxlBarStyle(String xmlName)
	{
		this.xmlName = xmlName;
	}
	
	
	@Override public String getXmlName()
	{
		return xmlName;
	}
	
	
	/**
	 * Returns the bar-style from the text of the given element.
	 */
	@NeverNull public static MxlBarStyle read(Element e)
	{
		String s = XMLReader.text(e);
		if (s != null)
			return throwNull(getEnumValueNamed(s, values()), e);
		else
			throw invalid(e);
	}
	
	
	public void write(Element e)
	{
		e.setTextContent(xmlName);
	}
	

}
