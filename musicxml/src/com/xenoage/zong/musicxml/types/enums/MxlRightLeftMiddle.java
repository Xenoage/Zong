package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.attribute;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;


/**
 * MusicXML right-left-middle.
 * Only named "location" in barline element.
 * 
 * @author Andreas Wenger
 */
public enum MxlRightLeftMiddle
{
	
	Right,
	Left,
	Middle;
	
	
	public static final String ATTR_NAME = "location";
	
	
	@MaybeNull public static MxlRightLeftMiddle read(Element e)
	{
		String s = attribute(e, ATTR_NAME);
		if (s != null)
		{
			return throwNull(getEnumValue(s, values()), e);
		}
		else
		{
			return null;
		}
	}
	
	
	public void write(Element e)
	{
		e.setAttribute(ATTR_NAME, toString().toLowerCase());
	}
	

}
