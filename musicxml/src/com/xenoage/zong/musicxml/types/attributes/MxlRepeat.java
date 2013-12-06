package com.xenoage.zong.musicxml.types.attributes;

import static com.xenoage.utils.xml.Parse.parseAttrIntNull;
import static com.xenoage.utils.xml.XMLWriter.addAttribute;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.enums.MxlBackwardForward;


/**
 * MusicXML repeat.
 * 
 * @author Andreas Wenger
 */
public final class MxlRepeat
{
	
	public static final String ELEM_NAME = "repeat";
	
	@NeverNull private final MxlBackwardForward direction;
	@MaybeNull private final Integer times;
	
	
	public MxlRepeat(MxlBackwardForward direction, Integer times)
	{
		this.direction = direction;
		this.times = times;
	}

	
	@NeverNull public MxlBackwardForward getDirection()
	{
		return direction;
	}

	
	@MaybeNull public Integer getTimes()
	{
		return times;
	}
	
	
	@NeverNull public static MxlRepeat read(Element e)
	{
		return new MxlRepeat(MxlBackwardForward.read(e),
			parseAttrIntNull(e, "times"));
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		direction.write(e);
		addAttribute(e, "times", times);
	}
	
	
	

}
