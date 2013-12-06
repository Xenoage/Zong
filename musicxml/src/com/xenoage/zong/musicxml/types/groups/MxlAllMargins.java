package com.xenoage.zong.musicxml.types.groups;

import static com.xenoage.utils.xml.Parse.parseChildFloat;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML all-margins.
 * 
 * @author Andreas Wenger
 */
public final class MxlAllMargins
{
	
	private final float leftMargin;
	private final float rightMargin;
	private final float topMargin;
	private final float bottomMargin;
	
	
	public MxlAllMargins(float leftMargin, float rightMargin, float topMargin,
		float bottomMargin)
	{
		this.leftMargin = leftMargin;
		this.rightMargin = rightMargin;
		this.topMargin = topMargin;
		this.bottomMargin = bottomMargin;
	}

	
	public float getLeftMargin()
	{
		return leftMargin;
	}

	
	public float getRightMargin()
	{
		return rightMargin;
	}

	
	public float getTopMargin()
	{
		return topMargin;
	}

	
	public float getBottomMargin()
	{
		return bottomMargin;
	}
	
	
	@NeverNull public static MxlAllMargins read(Element e)
	{
		return new MxlAllMargins(
			parseChildFloat(e, "left-margin"),
			parseChildFloat(e, "right-margin"),
			parseChildFloat(e, "top-margin"),
			parseChildFloat(e, "bottom-margin"));
	}
	
	
	public void write(Element e)
	{
		addElement("left-margin", leftMargin, e);
		addElement("right-margin", rightMargin, e);
		addElement("top-margin", topMargin, e);
		addElement("bottom-margin", bottomMargin, e);
	}


}
