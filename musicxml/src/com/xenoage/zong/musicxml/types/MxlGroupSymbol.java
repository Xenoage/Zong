package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.enums.MxlGroupSymbolValue;


/**
 * MusicXML group-symbol.
 * 
 * @author Andreas Wenger
 */
public final class MxlGroupSymbol
{
	
	public static final String ELEM_NAME = "group-symbol";
	
	@NeverNull private final MxlGroupSymbolValue value;
	@NeverNull private final MxlPosition position;
	@MaybeNull private final MxlColor color;
	
	
	public MxlGroupSymbol(MxlGroupSymbolValue value, MxlPosition position, MxlColor color)
	{
		this.value = value;
		this.position = position;
		this.color = color;
	}

	
	@NeverNull public MxlGroupSymbolValue getValue()
	{
		return value;
	}

	
	@NeverNull public MxlPosition getPosition()
	{
		return position;
	}

	
	@MaybeNull public MxlColor getColor()
	{
		return color;
	}
	
	
	@NeverNull public static MxlGroupSymbol read(Element e)
	{
		return new MxlGroupSymbol(MxlGroupSymbolValue.read(e), MxlPosition.read(e), MxlColor.read(e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		value.write(e);
		position.write(e);
		if (color != null)
			color.write(e);
	}
	

}
