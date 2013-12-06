package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.zong.musicxml.types.choice.MxlLyricContent;


/**
 * MusicXML extend.
 * 
 * @author Andreas Wenger
 */
public final class MxlExtend
	implements MxlLyricContent
{
	
	public static final String ELEM_NAME = "extend";
	
	
	@Override public MxlLyricContentType getLyricContentType()
	{
		return MxlLyricContentType.Extend;
	}
	
	
	public static MxlExtend read()
	{
		return new MxlExtend();
	}
	
	
	@Override public void write(Element parent)
	{
		addElement(ELEM_NAME, parent);
	}
	

}
