package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlFullNoteContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML full-note.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(children="unpitched,rest")
public final class MxlFullNote
{
	
	private final boolean chord;
	@NeverNull private final MxlFullNoteContent content;
	
	
	public MxlFullNote(boolean chord, MxlFullNoteContent content)
	{
		this.chord = chord;
		this.content = content;
	}

	
	public boolean isChord()
	{
		return chord;
	}

	
	@NeverNull public MxlFullNoteContent getContent()
	{
		return content;
	}
	
	
	@NeverNull public static MxlFullNote read(Element e)
	{
		boolean chord = false;
		MxlFullNoteContent content = null;
		for (Element c : elements(e))
		{
			String n = c.getNodeName();
			if (n.equals("chord"))
			{
				chord = true;
			}
			else if (n.equals(MxlPitch.XML_NAME))
			{
				content = MxlPitch.read(c);
				break;
			}
			else if (n.equals(MxlUnpitched.XML_NAME))
			{
				content = MxlUnpitched.read();
				break;
			}
			else if (n.equals(MxlRest.XML_NAME))
			{
				content = MxlRest.read();
				break;
			}
		}
		throwNull(content, e);
		return new MxlFullNote(chord, content);
	}
	
	
	public void write(Element e)
	{
		if (chord)
			addElement("chord", e);
		content.write(e);
	}

}
