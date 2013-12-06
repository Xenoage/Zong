package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.enums.MxlMarginType;
import com.xenoage.zong.musicxml.types.groups.MxlAllMargins;


/**
 * MusicXML page-margins.
 * 
 * @author Andreas Wenger
 */
public final class MxlPageMargins
{
	
	public static final String ELEM_NAME = "page-margins";
	
	@NeverNull private final MxlAllMargins value;
	@MaybeNull private final MxlMarginType type;
	
	
	public MxlPageMargins(MxlAllMargins value, MxlMarginType type)
	{
		this.value = value;
		this.type = type;
	}

	
	@NeverNull public MxlAllMargins getValue()
	{
		return value;
	}

	
	@MaybeNull public MxlMarginType getType()
	{
		return type;
	}
	
	
	public float getLeftMargin()
	{
		return value.getLeftMargin();
	}

	
	public float getRightMargin()
	{
		return value.getRightMargin();
	}

	
	public float getTopMargin()
	{
		return value.getTopMargin();
	}

	
	public float getBottomMargin()
	{
		return value.getBottomMargin();
	}
	
	
	/**
	 * Default is {@link MxlMarginType#Both}.
	 */
	@NeverNull public MxlMarginType getTypeNotNull()
	{
		return (type != null ? type : MxlMarginType.Both);
	}


	@NeverNull public static MxlPageMargins read(Element e)
	{
		return new MxlPageMargins(MxlAllMargins.read(e), MxlMarginType.read(e));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		value.write(e);
		if (type != null)
			type.write(e);
	}
	

}
