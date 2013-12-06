package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.Parse.getEnumValueNamed;
import static com.xenoage.utils.xml.XMLReader.attribute;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.xml.EnumWithXmlNames;


/**
 * MusicXML time-symbol.
 * 
 * @author Andreas Wenger
 */
public enum MxlTimeSymbol
	implements EnumWithXmlNames
{
	
	Common("common"),
	Cut("cut"),
	SingleNumber("single-number"),
	Normal("normal");
	
	public static final String ATTR_NAME = "symbol";
	
	private final String xmlName;
	
	
	private MxlTimeSymbol(String xmlName)
	{
		this.xmlName = xmlName;
	}
	
	
	@Override public String getXmlName()
	{
		return xmlName;
	}
	
	
	/**
	 * Returns the time-symbol from the attribute
	 * or returns null, if there is none.
	 */
	@MaybeNull public static MxlTimeSymbol read(Element e)
	{
		String s = attribute(e, ATTR_NAME);
		if (s != null)
		{
			MxlTimeSymbol ret = getEnumValueNamed(s, values());
			return throwNull(ret, e);
		}
		else
		{
			return null;
		}
	}
	
	
	public void write(Element e)
	{
		e.setAttribute(ATTR_NAME, xmlName);
	}
	

}
