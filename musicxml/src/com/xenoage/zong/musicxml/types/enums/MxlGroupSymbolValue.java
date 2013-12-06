package com.xenoage.zong.musicxml.types.enums;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.text;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML group-symbol-value.
 * 
 * @author Andreas Wenger
 */
public enum MxlGroupSymbolValue
{
	
	None,
	Brace,
	Line,
	Bracket;
	
	
	@NeverNull public static MxlGroupSymbolValue read(Element e)
	{
		String s = text(e);
		if (s != null)
		{
			return throwNull(getEnumValue(s, values()), e);
		}
		else
		{
			throw invalid(e);
		}
	}
	
	
	public void write(Element e)
	{
		e.setTextContent(toString().toLowerCase());
	}
	

}
