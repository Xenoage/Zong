package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.enums.MxlYesNo;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML print-attributes.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="staff-spacing,blank-page,page-number")
public class MxlPrintAttributes
{
	
	@MaybeNull private final Boolean newSystem;
	@MaybeNull private final Boolean newPage;
	
	public static final MxlPrintAttributes empty = new MxlPrintAttributes(null, null);
	

	public MxlPrintAttributes(Boolean newSystem, Boolean newPage)
	{
		this.newSystem = newSystem;
		this.newPage = newPage;
	}
	

	@MaybeNull public Boolean getNewSystem()
	{
		return newSystem;
	}
	
	
	@MaybeNull public Boolean getNewPage()
	{
		return newPage;
	}


	@NeverNull public static MxlPrintAttributes read(Element e)
	{
		Boolean newSystem = MxlYesNo.readNull(attribute(e, "new-system"), e);
		Boolean newPage = MxlYesNo.readNull(attribute(e, "new-page"), e);
		if (newSystem != null || newPage != null)
			return new MxlPrintAttributes(newSystem, newPage);
		else
			return empty;
	}
	
	
	public void write(Element e)
	{
		if (this != empty)
		{
			if (newSystem != null)
				addAttribute(e, "new-system", MxlYesNo.write(newSystem));
			if (newPage != null)
				addAttribute(e, "new-page", MxlYesNo.write(newPage));
		}
	}
	
}
