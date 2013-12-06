package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.core.music.direction.DynamicsType;
import com.xenoage.zong.musicxml.types.attributes.MxlPrintStyle;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.types.choice.MxlNotationsContent;
import com.xenoage.zong.musicxml.types.enums.MxlPlacement;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML dynamics.
 * 
 * Only the first child element is read.
 * Combinations are not allowed here.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly="")
public final class MxlDynamics
	implements MxlNotationsContent, MxlDirectionTypeContent
{
	
	public static final String ELEM_NAME = "dynamics";
	
	@NeverNull private final DynamicsType element;
	@NeverNull private final MxlPrintStyle printStyle;
	@MaybeNull private final MxlPlacement placement;

	
	public MxlDynamics(DynamicsType element, MxlPrintStyle printStyle, MxlPlacement placement)
	{
		this.element = element;
		this.printStyle = printStyle;
		this.placement = placement;
	}

	
	@NeverNull public DynamicsType getElement()
	{
		return element;
	}
	
	
	@NeverNull public MxlPrintStyle getPrintStyle()
	{
		return printStyle;
	}


	@MaybeNull public MxlPlacement getPlacement()
	{
		return placement;
	}


	@Override public MxlNotationsContentType getNotationsContentType()
	{
		return MxlNotationsContentType.Dynamics;
	}
	
	
	@Override public MxlDirectionTypeContentType getDirectionTypeContentType()
	{
		return MxlDirectionTypeContentType.Dynamics;
	}
	
	
	/**
	 * Reads the given element and returns it, or returns null if
	 * the element is not supported.
	 */
	@MaybeNull public static MxlDynamics read(Element e)
	{
		String childText = throwNull(elements(e), e).get(0).getTagName();
		DynamicsType element = null;
		for (DynamicsType type : DynamicsType.values())
		{
			if (type.name().equals(childText))
			{
				element = type;
				break;
			}
		}
		if (element != null)
		{
			return new MxlDynamics(element, MxlPrintStyle.read(e), MxlPlacement.read(e));
		}
		else
		{
			return null;
		}
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		addElement(element.name(), e);
		printStyle.write(e);
		if (placement != null)
			placement.write(e);
	}
	

}
