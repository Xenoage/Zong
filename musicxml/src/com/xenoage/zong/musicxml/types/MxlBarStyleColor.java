package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.enums.MxlBarStyle;


/**
 * MusicXML bar-style-color.
 * 
 * @author Andreas Wenger
 */
public final class MxlBarStyleColor
{
	
	public static final String ELEM_NAME = "bar-style";
	
	@NeverNull private final MxlBarStyle barStyle;
	@MaybeNull private final MxlColor color;
	
	
	public MxlBarStyleColor(MxlBarStyle barStyle, MxlColor color)
	{
		this.barStyle = barStyle;
		this.color = color;
	}

	
	@NeverNull public MxlBarStyle getBarStyle()
	{
		return barStyle;
	}

	
	@MaybeNull public MxlColor getColor()
	{
		return color;
	}
	
	
	@NeverNull public static MxlBarStyleColor read(Element e)
	{
		return new MxlBarStyleColor(MxlBarStyle.read(e), MxlColor.read(e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		barStyle.write(e);
		if (color != null)
			color.write(e);
	}
	

}
