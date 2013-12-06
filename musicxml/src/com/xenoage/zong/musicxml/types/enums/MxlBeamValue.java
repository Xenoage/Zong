package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.xml.Parse.getEnumValueNamed;
import static com.xenoage.utils.xml.XMLReader.getTextContent;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.xml.EnumWithXmlNames;


/**
 * MusicXML beam-value.
 * 
 * @author Andreas Wenger
 */
public enum MxlBeamValue
	implements EnumWithXmlNames
{
	
	Begin("begin"),
	Continue("continue"),
	End("end"),
	ForwardHook("forward hook"),
	BackwardHook("backward hook");
	
	
	private final String xmlName;
	
	
	private MxlBeamValue(String xmlName)
	{
		this.xmlName = xmlName;
	}
	
	
	@Override public String getXmlName()
	{
		return xmlName;
	}
	
	
	@MaybeNull public static MxlBeamValue read(Element e)
	{
		return getEnumValueNamed(getTextContent(e), values());
	}
	
	
	public void write(Element e)
	{
		e.setTextContent(xmlName);
	}

}
