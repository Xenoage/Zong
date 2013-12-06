package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML rest.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="display-step-octave")
public final class MxlRest
	implements MxlFullNoteContent
{
	
	public static final String XML_NAME = "rest";
	
	
	@Override public MxlFullNoteContentType getFullNoteContentType()
	{
		return MxlFullNoteContentType.Rest;
	}
	
	
	@NeverNull public static MxlRest read()
	{
		return new MxlRest();
	}
	
	
	@Override public void write(Element parent)
	{
		addElement(XML_NAME, parent);
	}

}
