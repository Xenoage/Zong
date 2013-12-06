package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.text;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;


/**
 * MusicXML stem-value.
 * 
 * @author Andreas Wenger
 */
public enum MxlStemValue
{
	
	Down,
	Up,
	Double,
	None;
	
	
	@MaybeNull public static MxlStemValue read(Element e)
	{
		String s = text(e);
		return throwNull(getEnumValue(s, values()), e);
	}
	
	
	public void write(Element e)
	{
		e.setTextContent(toString().toLowerCase());
	}

}
