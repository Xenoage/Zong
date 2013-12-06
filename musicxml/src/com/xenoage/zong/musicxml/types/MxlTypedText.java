package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLReader.text;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML typed-text.
 * 
 * @author Andreas Wenger
 */
public final class MxlTypedText
{
	
	@NeverNull private final String value;
	@MaybeNull private final String type;
	
	
	public MxlTypedText(String value, String type)
	{
		this.value = value;
		this.type = type;
	}

	
	@NeverNull public String getValue()
	{
		return value;
	}

	
	@MaybeNull public String getType()
	{
		return type;
	}
	
	
	@NeverNull public static MxlTypedText read(Element e)
	{
		return new MxlTypedText(text(e), attribute(e, "type"));
	}
	
	
	public void write(String elementName, Element parent)
	{
		Element e = addElement(elementName, parent);
		e.setTextContent(value);
		addAttribute(e, "type", type);
	}
	

}
