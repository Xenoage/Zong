package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlFont;


/**
 * MusicXML lyric-font.
 * 
 * @author Andreas Wenger
 */
public final class MxlLyricFont
{
	
	public static final String ELEM_NAME = "lyric-font";
	
	@MaybeNull private final String number;
	@MaybeNull private final String name;
	@NeverNull private final MxlFont font;
	
	
	public MxlLyricFont(String number, String name, MxlFont font)
	{
		this.number = number;
		this.name = name;
		this.font = font;
	}

	
	@MaybeNull public String getNumber()
	{
		return number;
	}

	
	@MaybeNull public String getName()
	{
		return name;
	}

	
	@NeverNull public MxlFont getFont()
	{
		return font;
	}


	@NeverNull public static MxlLyricFont read(Element e)
	{
		return new MxlLyricFont(
			attribute(e, "number"),
			attribute(e, "name"),
			MxlFont.read(e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addAttribute(e, "number", number);
		addAttribute(e, "name", name);
		font.write(e);
	}
	

}
