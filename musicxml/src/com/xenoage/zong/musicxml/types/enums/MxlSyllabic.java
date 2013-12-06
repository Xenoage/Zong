package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.text;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML syllabic.
 * 
 * @author Andreas Wenger
 */
public enum MxlSyllabic
{
	
	Single,
	Begin,
	End,
	Middle;
	
	
	public static final String ELEM_NAME = "syllabic";
	
	
	@NeverNull public static MxlSyllabic read(Element e)
	{
		return throwNull(getEnumValue(text(e), values()), e);
	}
	
	
	public void write(Element parent)
	{
		addElement(ELEM_NAME, toString().toLowerCase(), parent);
	}
	

}
