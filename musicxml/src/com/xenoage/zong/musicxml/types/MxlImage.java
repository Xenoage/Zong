package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.attributes.MxlPosition;
import com.xenoage.zong.musicxml.types.choice.MxlCreditContent;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.enums.MxlLeftCenterRight;
import com.xenoage.zong.musicxml.types.enums.MxlVAlignImage;


/**
 * MusicXML image.
 * 
 * @author Andreas Wenger
 */
public final class MxlImage
	implements MxlCreditContent, MxlDirectionTypeContent
{
	
	private final String elemName;
	
	@NeverNull private final String source;
	@NeverNull private final String type;
	@NeverNull private final MxlPosition position;
	@MaybeNull private final MxlLeftCenterRight hAlign;
	@MaybeNull private final MxlVAlignImage vAlign;
	

	public MxlImage(String elemName, String source, String type, MxlPosition position,
		MxlLeftCenterRight hAlign, MxlVAlignImage vAlign)
	{
		this.elemName = elemName;
		this.source = source;
		this.type = type;
		this.position = position;
		this.hAlign = hAlign;
		this.vAlign = vAlign;
	}

	
	@NeverNull public String getSource()
	{
		return source;
	}

	
	@NeverNull public String getType()
	{
		return type;
	}

	
	@MaybeNull public MxlPosition getPosition()
	{
		return position;
	}

	
	@MaybeNull public MxlLeftCenterRight getHAlign()
	{
		return hAlign;
	}

	
	@MaybeNull public MxlVAlignImage getVAlign()
	{
		return vAlign;
	}


	@Override public MxlCreditContentType getCreditContentType()
	{
		return MxlCreditContentType.CreditImage;
	}
	
	
	@Override public MxlDirectionTypeContentType getDirectionTypeContentType()
	{
		return MxlDirectionTypeContentType.Image;
	}
	
	
	@NeverNull public static MxlImage read(Element e)
	{
		return new MxlImage(
			e.getNodeName(),
			throwNull(attribute(e, "source"), e),
			throwNull(attribute(e, "type"), e),
			MxlPosition.read(e),
			MxlLeftCenterRight.read(e, "halign"),
			MxlVAlignImage.read(e));
	}


	@Override public void write(Element parent)
	{
		Element e = addElement(elemName, parent);
		addAttribute(e, "source", source);
		addAttribute(e, "type", type);
		if (position != null)
			position.write(e);
		if (hAlign != null)
			hAlign.write(e, "halign");
		if (vAlign != null)
			vAlign.write(e);
	}

}
