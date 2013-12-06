package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.Parse.parseFloat;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.groups.MxlLeftRightMargins;


/**
 * MusicXML system-layout.
 * 
 * @author Andreas Wenger
 */
public final class MxlSystemLayout
{
	
	public static final String ELEM_NAME = "system-layout";
	
	@MaybeNull private final MxlLeftRightMargins systemMargins;
	@MaybeNull private final Float systemDistance;
	@MaybeNull private final Float topSystemDistance;
	
	
	public MxlSystemLayout(MxlLeftRightMargins systemMargins, Float systemDistance,
		Float topSystemDistance)
	{
		this.systemMargins = systemMargins;
		this.systemDistance = systemDistance;
		this.topSystemDistance = topSystemDistance;
	}


	@MaybeNull public MxlLeftRightMargins getSystemMargins()
	{
		return systemMargins;
	}


	@MaybeNull public Float getSystemDistance()
	{
		return systemDistance;
	}


	@MaybeNull public Float getTopSystemDistance()
	{
		return topSystemDistance;
	}
	
	
	@NeverNull public static MxlSystemLayout read(Element e)
	{
		MxlLeftRightMargins systemMargins = null;
		Float systemDistance = null;
		Float topSystemDistance = null;
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			if (n.equals("system-margins"))
				systemMargins = MxlLeftRightMargins.read(c);
			else if (n.equals("system-distance"))
				systemDistance = parseFloat(c);
			else if (n.equals("top-system-distance"))
				topSystemDistance = parseFloat(c);
		}
		return new MxlSystemLayout(systemMargins, systemDistance, topSystemDistance);
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		if (systemMargins != null)
			systemMargins.write("system-margins", e);
		if (systemDistance != null)
			addElement("system-distance", systemDistance, e);
		if (topSystemDistance != null)
			addElement("top-system-distance", topSystemDistance, e);
	}
	
	

}
