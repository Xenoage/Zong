package com.xenoage.zong.musicxml.types;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.utils.xml.XMLWriter;
import com.xenoage.zong.musicxml.types.choice.MxlDirectionTypeContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML words.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children="formatted-text")
public final class MxlWords
	implements MxlDirectionTypeContent
{
	
	public static final String ELEM_NAME = "words";
	
	@NeverNull private final MxlFormattedText formattedText;

	
	public MxlWords(MxlFormattedText formattedText)
	{
		this.formattedText = formattedText;
	}

	
	@NeverNull public MxlFormattedText getFormattedText()
	{
		return formattedText;
	}
	
	
	@Override public MxlDirectionTypeContentType getDirectionTypeContentType()
	{
		return MxlDirectionTypeContentType.Words;
	}
	
	
	@NeverNull public static MxlWords read(Element e)
	{
		return new MxlWords(MxlFormattedText.read(e));
	}
	
	
	@Override public void write(Element parent)
	{
		Element e = XMLWriter.addElement(ELEM_NAME, parent);
		formattedText.write(e);
	}

}
