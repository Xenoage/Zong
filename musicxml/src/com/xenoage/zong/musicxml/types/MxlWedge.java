package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.base.NullUtils.notNull;
import static com.xenoage.utils.xml.Parse.parseAttrIntNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlColor;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.enums.MxlWedgeType;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML wedge.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="spread")
public final class MxlWedge
	implements MxlDirectionTypeContent
{
	
	public static final String ELEM_NAME = "wedge";
	
	@NeverNull private final MxlWedgeType type;
	private final int number;
	@NeverNull private final MxlPosition position;
	@MaybeNull private final MxlColor color;
	
	private static final int defaultNumber = 1;

	
	public MxlWedge(MxlWedgeType type, int number, MxlPosition position, MxlColor color)
	{
		this.type = type;
		this.number = number;
		this.position = position;
		this.color = color;
	}

	
	@NeverNull public MxlWedgeType getType()
	{
		return type;
	}

	
	public int getNumber()
	{
		return number;
	}


	@NeverNull public MxlPosition getPosition()
	{
		return position;
	}
	
	
	@MaybeNull public MxlColor getColor()
	{
		return color;
	}


	@Override public MxlDirectionTypeContentType getDirectionTypeContentType()
	{
		return MxlDirectionTypeContentType.Wedge;
	}
	
	
	@NeverNull public static MxlWedge read(Element e)
	{
		return new MxlWedge(
			MxlWedgeType.read(attribute(e, "type"), e),
			notNull(parseAttrIntNull(e, "number"), defaultNumber),
			MxlPosition.read(e), MxlColor.read(e));
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addAttribute(e, "type", type.write());
		addAttribute(e, "number", number);
		position.write(e);
		if (color != null)
			color.write(e);
	}

}
