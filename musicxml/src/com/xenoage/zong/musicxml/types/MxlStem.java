package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.enums.MxlStemValue;


/**
 * MusicXML stem.
 * 
 * @author Andreas Wenger
 */
public final class MxlStem
{
	
	public static final String ELEM_NAME = "stem";
	
	@NeverNull private final MxlStemValue value;
	@NeverNull private final MxlPosition yPosition;
	@MaybeNull private final MxlColor color;
	
	
	public MxlStem(MxlStemValue value, MxlPosition yPosition, MxlColor color)
	{
		this.value = value;
		this.yPosition = yPosition;
		this.color = color;
	}


	@NeverNull public MxlStemValue getValue()
	{
		return value;
	}


	@NeverNull public MxlPosition getYPosition()
	{
		return yPosition;
	}
	
	
	@MaybeNull public MxlColor getColor()
	{
		return color;
	}
	
	
	@NeverNull public static MxlStem read(Element e)
	{
		return new MxlStem(MxlStemValue.read(e), MxlPosition.read(e), MxlColor.read(e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		value.write(e);
		yPosition.write(e);
		if (color != null)
			color.write(e);
	}

}
