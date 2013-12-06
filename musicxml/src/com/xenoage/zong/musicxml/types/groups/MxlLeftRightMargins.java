package com.xenoage.zong.musicxml.types.groups;

import static com.xenoage.utils.xml.Parse.parseChildFloat;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML left-right-margins.
 * 
 * @author Andreas Wenger
 */
public final class MxlLeftRightMargins
{
	
	private final float leftMargin;
	private final float rightMargin;
	
	
	public MxlLeftRightMargins(float leftMargin, float rightMargin)
	{
		this.leftMargin = leftMargin;
		this.rightMargin = rightMargin;
	}

	
	public float getLeftMargin()
	{
		return leftMargin;
	}

	
	public float getRightMargin()
	{
		return rightMargin;
	}


	@NeverNull public static MxlLeftRightMargins read(Element e)
	{
		return new MxlLeftRightMargins(
			parseChildFloat(e, "left-margin"),
			parseChildFloat(e, "right-margin"));
	}
	
	
	public void write(String elementName, Element parent)
	{
		Element e = addElement(elementName, parent);
		addElement("left-margin", leftMargin, e);
		addElement("right-margin", rightMargin, e);
	}
	

}
