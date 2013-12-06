package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.xml.XMLReader.attribute;
import static com.xenoage.utils.xml.XMLReader.element;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.MaybeNull;
import com.xenoage.utils.base.annotations.NeverEmpty;
import com.xenoage.zong.musicxml.types.choice.MxlLyricContent;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML lyric.
 * 
 * Consists of a {@link MxlLyricContent}.
 * Other information is ignored.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(missing="end-line,end-paragraph,editorial,name,justify,position," +
	"placement,color", children="")
public final class MxlLyric
{
	
	public static final String ELEM_NAME = "lyric";
	
	@NeverEmpty public final MxlLyricContent content;
	@MaybeNull public final String number;
	
	
	public MxlLyric(MxlLyricContent content, String number)
	{
		this.content = content;
		this.number = number;
	}
	
	
	/**
	 * Reads and returns the lyric content of the given element,
	 * or returns null if unsupported.
	 */
	@MaybeNull public static MxlLyric read(Element e)
	{
		String number = attribute(e, "number");
		MxlLyricContent content = null;
		Element firstChild = element(e);
		String n = firstChild.getNodeName();
		if (n.equals("syllabic") || n.equals("text"))
		{
			content = MxlSyllabicText.read(e);
		}
		else if (n.equals("extend"))
		{
			content = MxlExtend.read();
		}
		if (content != null)
		{
			return new MxlLyric(content, number);
		}
		else
		{
			return null;
		}
	}
	
	
	public void write(Element parent)
	{
		Element e = addElement(ELEM_NAME, parent);
		content.write(e);
	}
	

}
