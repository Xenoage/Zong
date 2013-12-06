package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.enums.MxlGroupBarlineValue;


/**
 * MusicXML group-barline.
 * 
 * @author Andreas Wenger
 */
public final class MxlGroupBarline
{
	
	public static final String ELEM_NAME = "group-barline";
	
	@NeverNull private final MxlGroupBarlineValue value;
	@MaybeNull private final MxlColor color;
	
	
	public MxlGroupBarline(MxlGroupBarlineValue value, MxlColor color)
	{
		this.value = value;
		this.color = color;
	}

	
	@NeverNull public MxlGroupBarlineValue getValue()
	{
		return value;
	}

	
	@MaybeNull public MxlColor getColor()
	{
		return color;
	}
	
	
	@NeverNull public static MxlGroupBarline read(Element e)
	{
		return new MxlGroupBarline(MxlGroupBarlineValue.read(e), MxlColor.read(e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		value.write(e);
		if (color != null)
			color.write(e);
	}
	

}
