package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.xml.XmlDataException.throwNull;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML start-stop.
 * 
 * @author Andreas Wenger
 */
public enum MxlStartStop
{
	
	Start,
	Stop;
	
	
	@NeverNull public static MxlStartStop read(String s, Element parent)
	{
		return throwNull(getEnumValue(s, values()), parent);
	}
	
	
	public String write()
	{
		return toString().toLowerCase();
	}
	

}
