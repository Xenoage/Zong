package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.elementText;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML work.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="opus")
public final class MxlWork
{
	
	public static final String ELEM_NAME = "work";
	public static final MxlWork empty = new MxlWork(null, null);
	
	@MaybeNull private final String workNumber;
	@MaybeNull private final String workTitle;
	
	
	public MxlWork(String workNumber, String workTitle)
	{
		this.workNumber = workNumber;
		this.workTitle = workTitle;
	}

	
	@MaybeNull public String getWorkNumber()
	{
		return workNumber;
	}

	
	@MaybeNull public String getWorkTitle()
	{
		return workTitle;
	}
	
	
	@NeverNull public static MxlWork read(Element e)
	{
		String workNumber = elementText(e, "work-number");
		String workTitle = elementText(e, "work-title");
		if (workNumber != null || workTitle != null)
			return new MxlWork(workNumber, workTitle);
		else
			return empty;
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		if (this != empty)
		{
			addElement("work-number", workNumber, e);
			addElement("work-title", workTitle, e);
		}
	}
	

}
