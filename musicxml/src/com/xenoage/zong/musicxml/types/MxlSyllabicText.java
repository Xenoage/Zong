package com.xenoage.zong.musicxml.types;

import static com.xenoage.utils.base.EnumUtils.getEnumValue;
import static com.xenoage.utils.xml.XmlDataException.invalid;
import static com.xenoage.utils.xml.XmlDataException.throwNull;
import static com.xenoage.utils.xml.XMLReader.elements;
import static com.xenoage.utils.xml.XMLReader.text;
import static com.xenoage.utils.xml.XMLWriter.addElement;

import java.util.List;

import org.w3c.dom.Element;

import com.xenoage.utils.base.annotations.NeverNull;
import com.xenoage.zong.musicxml.types.choice.MxlLyricContent;
import com.xenoage.zong.musicxml.types.enums.MxlSyllabic;
import com.xenoage.zong.musicxml.util.IncompleteMusicXML;


/**
 * MusicXML syllabic and its text.
 * See lines 3312 and 3313 in musicxml.xsd (version 2.0).
 * Elisions are not handled.
 * 
 * @author Andreas Wenger
 */
@IncompleteMusicXML(partly="")
public final class MxlSyllabicText
	implements MxlLyricContent
{
	
	@NeverNull private final MxlSyllabic syllabic;
	@NeverNull private final MxlTextElementData text;
	
	private static final MxlSyllabic defaultSyllabic = MxlSyllabic.Single;
	
	
	public MxlSyllabicText(MxlSyllabic syllabic, MxlTextElementData text)
	{
		this.syllabic = syllabic;
		this.text = text;
	}

	
	/**
	 * Gets the {@link MxlSyllabic} element.
	 * Never returns null (since missing syllabic element in MusicXML
	 * means single, see {@link #read(Element)}).
	 */
	@NeverNull public MxlSyllabic getSyllabic()
	{
		return syllabic;
	}

	
	@NeverNull public MxlTextElementData getText()
	{
		return text;
	}
	
	
	@Override public MxlLyricContentType getLyricContentType()
	{
		return MxlLyricContentType.SyllabicText;
	}
	
	
	/**
	 * Reads the given syllabic text.
	 * If the syllabic element is missing, it is treated as {@link MxlSyllabic#Single}
	 * (according to MusicXML mailgroup, R. Kainhofer, 2010-04-08).
	 */
	public static MxlSyllabicText read(Element e)
	{
		List<Element> elements = elements(e);
		int index = 0;
		//the first element can be "syllabic"
		MxlSyllabic syllabic = defaultSyllabic;
		Element firstChild = elements.get(0);
		if (firstChild.getNodeName().equals("syllabic"))
		{
			syllabic = throwNull(getEnumValue(text(firstChild), MxlSyllabic.values()), firstChild);
			index++;
		}
		//next one must be "text"
		Element eText = elements.get(index);
		if (!eText.getNodeName().equals("text"))
			throw	invalid(e);
		MxlTextElementData text = MxlTextElementData.read(eText);
		return new MxlSyllabicText(syllabic, text);
	}
	
	
	@Override public void write(Element parent)
	{
		syllabic.write(parent);
		Element eText = addElement("text", parent);
		text.write(eText);
	}
	

}
