package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.Parse.parseAttrIntNull;
import static com.xenoage.utils.xml.Parse.parseChildFloatNull;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;


/**
 * MusicXML staff-layout.
 * 
 * @author Andreas Wenger
 */
public final class MxlStaffLayout
{
	
	public static final String ELEM_NAME = "staff-layout";
	
	@MaybeNull private final Float staffDistance;
	@MaybeNull private final Integer number;
	
	private static final int defaultNumber = 1;


	public MxlStaffLayout(Float staffDistance, Integer number)
	{
		this.staffDistance = staffDistance;
		this.number = number;
	}

	
	@MaybeNull public Float getStaffDistance()
	{
		return staffDistance;
	}

	
	/**
	 * May return null. If within in the defaults element, this means
	 * "for all staves". Otherwise, use getNumberNotNull.
	 */
	@MaybeNull public Integer getNumber()
	{
		return number;
	}
	
	
	@NeverNull public int getNumberNotNull()
	{
		return (number != null ? number : defaultNumber);
	}


	@NeverNull public static MxlStaffLayout read(Element e)
	{
		return new MxlStaffLayout(
			parseChildFloatNull(e, "staff-distance"),
			parseAttrIntNull(e, "number"));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		if (staffDistance != null)
			addElement("staff-distance", staffDistance, e);
		addAttribute(e, "number", number);
	}
	
	

}
