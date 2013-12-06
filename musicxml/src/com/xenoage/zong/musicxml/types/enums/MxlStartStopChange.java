package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.xml.XmlDataException.throwNull;

import org.w3c.dom.Element;


/**
 * MusicXML start-stop-change.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStopChange
{
	
	Start,
	Stop,
	Change;
	
	
	public static MxlStartStopChange read(String s, Element parent)
	{
		return throwNull(getEnumValue(s, values()), parent);
	}
	
	
	public String write()
	{
		return toString().toLowerCase();
	}
	

}
