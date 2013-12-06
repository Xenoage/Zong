package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.attribute;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;


/**
 * MusicXML left-center-right.
 * 
 * @author Andreas Wenger
 */
public enum MxlLeftCenterRight
{
	
	Left,
	Center,
	Right;
	
	
	@MaybeNull public static MxlLeftCenterRight read(Element e, String attrName)
	{
		String s = attribute(e, attrName);
		if (s != null)
		{
			return throwNull(getEnumValue(s, values()), e);
		}
		else
		{
			return null;
		}
	}
	
	
	public void write(Element e, String attrName)
	{
		e.setAttribute(attrName, toString().toLowerCase());
	}
	

}
